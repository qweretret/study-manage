package com.ossjk.qlh.system.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ossjk.core.system.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ossjk.config.jwttoken.JwtTokenUtil;
import com.ossjk.config.redis.RedisUtil;
import com.ossjk.config.sms.SmsServer;
import com.ossjk.core.constant.CacheConstant;
import com.ossjk.core.exception.QlhRunTimeException;
import com.ossjk.core.system.api.ISystemCommonApi;
import com.ossjk.core.vo.ResponseBean;
import com.ossjk.qlh.sms.entity.Sms;
import com.ossjk.qlh.sms.service.ISmsService;
import com.ossjk.qlh.system.entity.Department;
import com.ossjk.qlh.system.entity.Dictionary;
import com.ossjk.qlh.system.entity.Loginrecord;
import com.ossjk.qlh.system.entity.Permission;
import com.ossjk.qlh.system.entity.Role;
import com.ossjk.qlh.system.entity.User;
import com.ossjk.qlh.system.service.IDepartmentService;
import com.ossjk.qlh.system.service.IDictionaryService;
import com.ossjk.qlh.system.service.ILoginrecordService;
import com.ossjk.qlh.system.service.IPermissionService;
import com.ossjk.qlh.system.service.IRoleService;
import com.ossjk.qlh.system.service.IUserService;
import com.ossjk.qlh.system.vo.UserVo;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import cn.hutool.extra.servlet.ServletUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SystemCommonApiImpl implements ISystemCommonApi {

	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	@Autowired
	private IUserService iUserService;
	@Autowired
	private IDepartmentService iDepartmentService;
	@Autowired
	private IRoleService iRoleService;
	@Autowired
	private IPermissionService iPermissionService;
	@Autowired
	private IDictionaryService iDictionaryService;
	@Autowired
	private RedisUtil redisUtil;
	@Autowired
	private ISmsService iSmsService;
	@Autowired
	private SmsServer smsServer;
	@Autowired
	private MailAccount mailAccount;
	@Autowired
	private ILoginrecordService iLoginrecordService;

	@Override
	public String generateToken(String uid) {
		return jwtTokenUtil.generateToken(uid);
	}

	@Override
	public LoginUser getLoginUser(String token) {
		// 从token获取uid
		String uid = jwtTokenUtil.getUserIdFromToken(token);
		UserVo userVo = iUserService.getVoById(uid);
		// User转LoginUser
		LoginUser loginUser = BeanUtil.copyProperties(userVo, LoginUser.class);
		// 查找用户关联的部门
		List<Department> departments = iDepartmentService.listByUid(loginUser.getId());
		if (ObjectUtil.isNotEmpty(departments)) {
			// 转换标识
			Set<String> departmentCode = departments.stream().map((department) -> {
				return department.getCode();
			}).collect(Collectors.toSet());
			// 获取用户关联的子部门
			List<Department> allDepartments = new ArrayList<Department>();
			for (Department department : departments) {
				allDepartments.addAll(iDepartmentService.listChildrenById(department.getId()));
			}
			// 转换标识
			Set<String> departmentCodes = allDepartments.stream().map((department) -> {
				return department.getCode();
			}).collect(Collectors.toSet());

			loginUser.setDepartmentCode(departmentCode);
			loginUser.setDepartmentCodes(departmentCodes);
		}

		// 查找用户关联的角色
		List<Role> roles = iRoleService.listByUid(loginUser.getId());
		if (ObjectUtil.isNotEmpty(roles)) {
			// 转换标识
			Set<String> roleCode = roles.stream().map((role) -> {
				return role.getCode();
			}).collect(Collectors.toSet());
			// 获取用户关联的子部门
			List<Role> allRoles = new ArrayList<Role>();
			for (Role role : roles) {
				allRoles.addAll(iRoleService.listChildrenById(role.getId()));
			}
			// 转换标识
			Set<String> roleCodes = allRoles.stream().map((role) -> {
				return role.getCode();
			}).collect(Collectors.toSet());

			loginUser.setRoleCode(roleCode);
			loginUser.setRoleCodes(roleCodes);
		}

		// 查找用户关联的权限
		List<Permission> permissions = iPermissionService.listByUid(loginUser.getId());
		if (ObjectUtil.isNotEmpty(permissions)) {
			// 转换标识
			Set<String> permissionCode = permissions.stream().map((permission) -> {
				return permission.getCode();
			}).collect(Collectors.toSet());

			List<PermissionMenu> permissionMenus = permissions.stream().filter((permission) -> {
				return StrUtil.equals(permission.getType().toString(), "1") || StrUtil.equals(permission.getType().toString(), "2");
			}).map((permission) -> {
				return BeanUtil.copyProperties(permission, PermissionMenu.class);
			}).collect(Collectors.toList());
			loginUser.setPermissionCode(permissionCode);
			loginUser.setPermissionMenus(permissionMenus);
		}

		return loginUser;

	}

	@Override
	public LoginUser getCacheLoginUser(String token) {
		String cacheKey = this.getCacheTokenKey(token);
		// 尝试从redis拿
		LoginUser loginUser = (LoginUser) redisUtil.get(cacheKey);
		if (ObjectUtil.isEmpty(loginUser)) {
			// 拿不成功，从数据库查询
			this.refreshTokenCacheLoginUser(token);
		}
		return loginUser;
	}

	@Override
	public LoginUser getCacheLoginUserAndRemoveOther(String token) {
		// 先删除已有token
		this.removeCacheLoginUserByUid(jwtTokenUtil.getUserIdFromToken(token));
		// 重新生成
		return this.getCacheLoginUser(token);
	}

	@Override
	public LoginUser refreshTokenCacheLoginUser(String token) {

		String cacheKey = this.getCacheTokenKey(token);

		if (!jwtTokenUtil.isTokenExpired(token)) {
			// 拿不成功，从数据库查询
			LoginUser loginUser = this.getLoginUser(token);
			redisUtil.set(cacheKey, loginUser, jwtTokenUtil.getExpiredMilliseconFromToken(token), TimeUnit.MILLISECONDS);
			return loginUser;
		}
		log.error("token:" + token + ",失效了。");
		this.removeCacheLoginUserByToken(token);
		return null;

	}

	@Override
	public void removeCacheLoginUserByToken(String token) {
		String cacheKey = this.getCacheTokenKey(token);
		redisUtil.del(cacheKey);
	}

	@Override
	public void removeCacheLoginUserByUid(String uid) {
		String cachePrefix = this.getCacheTokenPrefix(uid);
		redisUtil.vagueDel(cachePrefix + "*");
	}

	@Override
	public String getCacheTokenKey(String token) {
		return CacheConstant.generateCacheJwtToken(jwtTokenUtil.getUserIdFromToken(token), token);
	}

	@Override
	public String getCacheTokenPrefix(String uid) {
		return CacheConstant.generateCacheJwtTokenPrefix(uid);
	}

	@Override
	public String refreshTokenAndCacheLoginUser(String token) {
		String newToken = this.refreshToken(token);
		this.getCacheLoginUser(token);
		return newToken;
	}

	@Override
	public String refreshToken(String token) {
		String uid = jwtTokenUtil.getUserIdFromToken(token);
		this.removeCacheLoginUserByToken(token);
		return this.generateToken(uid);
	}

	@Override
	@Cacheable(cacheNames = CacheConstant.CACHE_DICTIONARY_SYSTEMSETTING_PREFIX)
	public SystemSetting getSystemSetting() {
		// 类型：1-选项、2-属性值、3-系统属性、4-邮箱设置
		List<Dictionary> dictionaries = iDictionaryService.list(new QueryWrapper<Dictionary>().eq("type", 3).orderByAsc("id"));
		if (ObjectUtil.isNotEmpty(dictionaries)) {
			Map<String, Object> map = new HashMap();
			for (Dictionary dictionary : dictionaries) {
				String dkey = dictionary.getDkey();
				String dvalue = dictionary.getDvalue();
				map.put(dkey, dvalue);
			}
			return BeanUtil.toBean(map, SystemSetting.class);
		}
		return null;
	}

	@Override
	@Cacheable(cacheNames = CacheConstant.CACHE_DICTIONARY_EMAILSETTING_PREFIX)
	public EmailSetting getEmailSetting() {
		// 类型：1-选项、2-属性值、3-系统属性、4-邮箱设置
		List<Dictionary> dictionaries = iDictionaryService.list(new QueryWrapper<Dictionary>().eq("type", 4).orderByAsc("id"));
		if (ObjectUtil.isNotEmpty(dictionaries)) {
			Map<String, Object> map = new HashMap();
			for (Dictionary dictionary : dictionaries) {
				String dkey = dictionary.getDkey();
				String dvalue = dictionary.getDvalue();
				map.put(dkey, dvalue);
			}
			return BeanUtil.toBean(map, EmailSetting.class);
		}
		return null;
	}

	@Override
	@CacheEvict(value = { CacheConstant.CACHE_DICTIONARY_SYSTEMSETTING_PREFIX }, allEntries = true)
	public Boolean updateSystemSetting(SystemSetting systemSetting) {
		Map<String, Object> beanMap = BeanUtil.beanToMap(systemSetting);
		if (ObjectUtil.isNotEmpty(beanMap)) {
			Set<String> keys = beanMap.keySet();
			for (String key : keys) {
				// 类型：1-选项、2-属性值、3-系统属性、4-邮箱设置
				Dictionary dictionary = iDictionaryService.getOne(new QueryWrapper<Dictionary>().eq("dkey", key).eq("type", 3));
				if (ObjectUtil.isEmpty(dictionary)) {
					throw new QlhRunTimeException("系统设置:【" + key + "】找不到，更新失败。");
				}
				dictionary.setDvalue(beanMap.get(key).toString());
				iDictionaryService.updateById(dictionary);
			}
			return true;
		}

		return false;
	}

	@Override
	@CacheEvict(value = { CacheConstant.CACHE_DICTIONARY_EMAILSETTING_PREFIX }, allEntries = true)
	public Boolean updateEmailSetting(EmailSetting emailSetting) {
		Map<String, Object> beanMap = BeanUtil.beanToMap(emailSetting);
		if (ObjectUtil.isNotEmpty(beanMap)) {
			Set<String> keys = beanMap.keySet();
			for (String key : keys) {
				// 类型：1-选项、2-属性值、3-系统属性、4-邮箱设置
				Dictionary dictionary = iDictionaryService.getOne(new QueryWrapper<Dictionary>().eq("dkey", key).eq("type", 4));
				if (ObjectUtil.isEmpty(dictionary)) {
					throw new QlhRunTimeException("邮箱设置:【" + key + "】找不到，更新失败。");
				}
				dictionary.setDvalue(beanMap.get(key).toString());
				iDictionaryService.updateById(dictionary);
			}
			return true;
		}
		return false;
	}

	@Override
	public String getUidByToken(String token) {
		return jwtTokenUtil.getUserIdFromToken(token);
	}

	@Override
	public List<SmsDto> getUnReadSms(String uid) {
		QueryWrapper<Sms> queryWrapper = new QueryWrapper<Sms>();
		// 阅读状态:1-未读、2-已读
		queryWrapper.eq("rstate", 1);
		queryWrapper.eq("rid", uid);
		queryWrapper.orderByDesc("stime");
		return iSmsService.list(queryWrapper).stream().map(sms -> {
			return BeanUtil.copyProperties(sms, SmsDto.class);
		}).collect(Collectors.toList());
	}

	@Override
	public void notifyUpdateSms(String uid) {
		List<SmsDto> smsDtos = getUnReadSms(uid);
		smsServer.sendMessageByUid(uid, JSON.toJSONString(smsDtos));
	}

	@Override
	public String sendEmail(String to, String subject, String content, boolean isHtml, File... files) {
		return MailUtil.send(mailAccount, to, subject, content, isHtml, files);
	}

	@Override
	public String sendEmail(Collection<String> tos, String subject, String content, boolean isHtml, File... files) {
		return MailUtil.send(mailAccount, tos, subject, content, isHtml, files);
	}



	@Override
	public ResponseBean<String> login(String name, String pwd) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		// 生成登录日志
		Loginrecord loginrecord = new Loginrecord();
		loginrecord.setUseragent(ServletUtil.getHeader(request, "User-Agent", "UTF-8"));
		Date date = new Date();
		loginrecord.setLogintime(date);
		// 登录是否成功 1-成功 2-失败
		loginrecord.setLoginresult(2);
		loginrecord.setIp(ServletUtil.getClientIP(request));
		User user = iUserService.getOne(new QueryWrapper<User>().eq("lname", name).or().eq("mobile", name).or().eq("email", name));
		if (ObjectUtil.isNotEmpty(user)) {
			loginrecord.setUid(user.getId());
			loginrecord.setCrer(user.getId());
			loginrecord.setMder(user.getId());
			if (ObjectUtil.equal(user.getIsdisable(), 2)) {
				if (StrUtil.equals(user.getPwd(), SecureUtil.md5(pwd))) {
					// 生成token
					String token = this.generateToken(user.getId());
					// 存放token信息到redis
					this.getCacheLoginUser(token);
					// iSystemCommonApi.getCacheLoginUserAndRemoveOther(token);
					// 登录是否成功 1-成功 2-失败
					loginrecord.setLoginresult(1);
					iLoginrecordService.save(loginrecord);
					return ResponseBean.Success("登录成功。", token);
				} else {
					iLoginrecordService.save(loginrecord);
					return ResponseBean.Fail("密码错误。");
				}
			} else {
				iLoginrecordService.save(loginrecord);
				return ResponseBean.Fail("帐号禁用，请联系管理员。");
			}
		} else {
			return ResponseBean.Fail("帐号不存在。");
		}
	}

	@Override
	public  ResponseBean<String>  login(WechatBindDto wechatBindDto) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		// 生成登录日志
		Loginrecord loginrecord = new Loginrecord();
		loginrecord.setUseragent(ServletUtil.getHeader(request, "User-Agent", "UTF-8"));
		Date date = new Date();
		loginrecord.setLogintime(date);
		loginrecord.setIp(ServletUtil.getClientIP(request));
		User user = iUserService.getOne(new QueryWrapper<User>().eq("lname", wechatBindDto.getName()));
		if (wechatBindDto.getName() == null && wechatBindDto != null) {
			//尝试openID去登录
			User user2 = iUserService.getOne(new QueryWrapper<User>().eq("wxappopenid", wechatBindDto.getOpenid()).eq("wxappuid", wechatBindDto.getUnionid()));
			if (ObjectUtil.isEmpty(user2)) {
				return ResponseBean.Fail("该用户没绑定，请先绑定账号");
			} else {
				String token = this.generateToken(user2.getId());
				this.getCacheLoginUser(token);
				return ResponseBean.Success("登录成功", token);
			}
		}
		if (wechatBindDto.getName() != null && ObjectUtil.isNotEmpty(user)) {
			loginrecord.setUid(user.getId());
			loginrecord.setCrer(user.getId());
			loginrecord.setMder(user.getId());
			if (ObjectUtil.equal(user.getIsdisable(), 2)) {
				if (StrUtil.equals(user.getPwd(), SecureUtil.md5(wechatBindDto.getPwd()))) {
						UpdateWrapper<User> UpdateWrapper = new UpdateWrapper<User>();
						UpdateWrapper.set("wxappopenid", wechatBindDto.getOpenid())
								.set("wxappuid", wechatBindDto.getUnionid())
								.eq("lname", wechatBindDto.getName())
								.eq("pwd", SecureUtil.md5(wechatBindDto.getPwd()));
						boolean update = iUserService.update(UpdateWrapper);
						if (update) {
							// 生成token
							String token = this.generateToken(user.getId());
							// 存放token信息到redis
							this.getCacheLoginUser(token);
							// iSystemCommonApi.getCacheLoginUserAndRemoveOther(token);
							// 登录是否成功 1-成功 2-失败
							loginrecord.setLoginresult(1);
							iLoginrecordService.save(loginrecord);
							return ResponseBean.Success("登录成功。", token);
						} else {
							return ResponseBean.Fail("登录错误,请重试");
						}
					} else {
						return ResponseBean.Fail("登录错误,请重试");
					}
				} else {
					return ResponseBean.Fail("登录错误,请重试");
		}} else {
			return ResponseBean.Fail("登录错误请重试");
		}
	}

	@Override
	@Transactional
	public  ResponseBean<String>  wxBind(WechatBindDto wechatBindDto) {

		String name =wechatBindDto.getName();
		String pwd = wechatBindDto.getPwd();

		QueryWrapper<User> queryWrapper = new QueryWrapper();

		if(StrUtil.isNotBlank(name) && StrUtil.isNotBlank(pwd)  ){
			queryWrapper.eq("lname", name);
			queryWrapper.eq("pwd", pwd);

			User usr = iUserService.getOne(queryWrapper);
			if (ObjectUtil.isNotEmpty(usr)) {
				// 绑定现有的
				usr.setWxappuid(wechatBindDto.getUnionid());
				usr.setWxappopenid(wechatBindDto.getOpenid());
				iUserService.saveOrUpdate(usr);

				String token = this.generateToken(usr.getId());
				// 存放token信息到redis
				this.getCacheLoginUser(token);
				return   ResponseBean.Success(token) ;
			}
		}

		return   ResponseBean.Success("fail") ;



	}

}
