package com.ossjk.qlh.study.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ossjk.config.mvc.ResourceMappersProperties;
import com.ossjk.config.wxminiapp.WxMaProperties;
import com.ossjk.core.base.controller.BaseController;
import com.ossjk.core.system.api.ISystemCommonApi;
import com.ossjk.core.system.dto.LoginUser;
import com.ossjk.core.vo.ResponseBean;
import com.ossjk.myUtil.Conversation;
import com.ossjk.qlh.study.entity.Tuisongzuoye;
import com.ossjk.qlh.study.service.ITuisongzuoyeService;
import com.ossjk.qlh.study.vo.ClassVO;
import com.ossjk.qlh.study.vo.StudentVO;
import com.ossjk.qlh.study.vo.TeacherVO;
import com.ossjk.qlh.study.vo.Tuisongzuoye2Vo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.sf.jsqlparser.statement.select.Select;
import org.apache.ibatis.annotations.Insert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Copyright  2022-08-05 QLH. Tech Ltd. All rights reserved.
 * 
 * @Package: com.ossjk.qlh.xueli.controller
 * @ClassName: TuisongzuoyeController
 * @Description: -控制器
 * @author: Rick.yang
 * @date:  2022-08-05 14:40:29 
 */
@Api(tags = "学生签收推送作业信息")
@RestController
@RequestMapping("/lky/tuisongzuoye")
public class TuisongzuoyeController extends BaseController {
	
	@Autowired
	private ITuisongzuoyeService iTuisongzuoyeService;

	@Autowired
	private ISystemCommonApi systemCommonApi;

    @Autowired
	private WxMaProperties wxp;

	@ApiOperation(value = "列表")
	//@RequiresPermissions("")
	@GetMapping("/list")
	public ResponseBean<Page<Tuisongzuoye2Vo>> list(Page page,@ApiParam(value = "状态数字-默认0") @RequestParam(name = "whethertopush", required = false) String whethertopush,
													@ApiParam(value = "作业类型-默认全部",required = true)@RequestParam(value = "thejobtype",required = false)String thejobtype,HttpServletRequest request) {

		String token = this.getToekn(request);
		LoginUser uer = systemCommonApi.getCacheLoginUser(token);

		QueryWrapper<Tuisongzuoye2Vo> lqw = new QueryWrapper<>();
		lqw.eq("b.tid",uer.getId())
			.eq(thejobtype !=null && !thejobtype.equals("null"),"thejobtype",thejobtype)
		   .eq( Integer.parseInt(whethertopush) < 2, "whethertopush", whethertopush)
		   .between( Integer.parseInt(whethertopush) > 1, "Whethertopush", 0,1);
		return ResponseBean.Success(iTuisongzuoyeService.list(page,lqw));
	}

	@ApiOperation(value = "小程序查询全部")
	//@RequiresPermissions("")
	@GetMapping("/list2")
	public ResponseBean<List<Tuisongzuoye2Vo>> list2(HttpServletRequest request,
													 @ApiParam(value = "班级ID",required = true)@RequestParam(value = "cid",required = false)String cid) {
		String token = this.getToekn(request);
		LoginUser uer = systemCommonApi.getCacheLoginUser(token);
		return ResponseBean.Success(iTuisongzuoyeService.list2(uer.getId(),cid));
	}

	@ApiOperation(value = "小程序查询学生个人")
	//@RequiresPermissions("")
	@GetMapping("/list3")
	public ResponseBean<Page<Tuisongzuoye2Vo>> list3(Page page,HttpServletRequest request,
													 @ApiParam(value = "学生ID",required = true)@RequestParam(value = "id",required = false)String id,
													 @ApiParam(value = "状态数字-默认0") @RequestParam(name = "whethertopush", required = false) String whethertopush,
													 @ApiParam(value = "作业类型-默认全部",required = true)@RequestParam(value = "thejobtype",required = false)String thejobtype) {
		String token = this.getToekn(request);
		LoginUser uer = systemCommonApi.getCacheLoginUser(token);
		QueryWrapper<Tuisongzuoye2Vo> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("b.sid",id)
				.eq("b.tid",uer.getId())
				.eq(thejobtype !=null && !thejobtype.equals("null"),"thejobtype",thejobtype)
				.eq( Integer.parseInt(whethertopush) < 2, "whethertopush", whethertopush)
				.between( Integer.parseInt(whethertopush) > 1, "Whethertopush", 0,1);
		return ResponseBean.Success(iTuisongzuoyeService.list3(page,queryWrapper));

	}

	@ApiOperation(value = "查询班级名字与ID")
	//@RequiresPermissions("")
	@GetMapping("/listClass")
	public ResponseBean<List<ClassVO>> listClass(HttpServletRequest request) {
		String token = this.getToekn(request);
		LoginUser uer = systemCommonApi.getCacheLoginUser(token);
		return ResponseBean.Success(iTuisongzuoyeService.interactionCsn(uer.getId()));
	}

	@ApiOperation(value = "查询学生名字与ID")
	//@RequiresPermissions("")
	@GetMapping("/listStuid")
	public ResponseBean<List<StudentVO>> listStuid(@ApiParam(value = "cid") @RequestParam(name = "cid", required = false) String id) {
		return ResponseBean.Success(iTuisongzuoyeService.interactionStuid(id));
	}

	@ApiOperation(value = "查询班级对应老师名字与ID")
	@GetMapping("/intnTcr")
	public ResponseBean<List<TeacherVO>> list3(
			@ApiParam(value = "cid") @RequestParam(name = "cid", required = false) String tid) {
		return ResponseBean.Success(iTuisongzuoyeService.interactionTcr(tid));
	}

	@ApiOperation(value = "推送该信息含小程序")
	//@RequiresPermissions("")
	@GetMapping("/tuisong")
	public ResponseBean tuisong(@ApiParam(value = "aid目标信息ID-数组") @RequestParam(name = "aid", required = true) String[] aid) {
		//读取yaml的配置数据
		boolean updatetuisong = false;
		List<WxMaProperties.Config> resourceMapperList = wxp.getConfigs();
		Map<String, String> uriMap = resourceMapperList.stream()
				.collect(Collectors.toMap(WxMaProperties.Config::getAppid, WxMaProperties.Config::getSecret));

		Conversation conversation = new Conversation(uriMap.keySet().toArray(new String[2])[0],uriMap.values().toArray(new String[2])[0]);
		for(String aid2 : aid){
			Tuisongzuoye2Vo oneStu = iTuisongzuoyeService.getOneStu(aid2);
			Tuisongzuoye2Vo openidStu = iTuisongzuoyeService.getOpenidStu(oneStu.getSid());
			oneStu.setWxappopenid(openidStu.getWxappopenid());
			if(openidStu.getWxappopenid() != null){
				String s = conversation.sendVlogCompleteTemplateMsg(oneStu);
				System.out.println(s);
				if(s.contains("\"errmsg\":\"ok\"")){
					String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
					UpdateWrapper<Tuisongzuoye> updateWrapper = new UpdateWrapper<Tuisongzuoye>().eq("id", aid2).set("whethertopush", 1)
							.set("chetime", time);
					updatetuisong = iTuisongzuoyeService.update(updateWrapper);
				}
			}else{
				return ResponseBean.Fail("该学生没有登陆小程序,无法推送信息");
			}
		}
		if (updatetuisong) {
			return ResponseBean.Success();
		} else {
			return ResponseBean.Fail();
		}
	}

	@ApiOperation(value = "添加")
	//@RequiresPermissions("")
	@PostMapping("/save")
	public ResponseBean save(@RequestBody Tuisongzuoye[] sio) {
		for(Tuisongzuoye i: sio){
			i.setCrtm(new Date());
			i.setWhethertopush(0);
		}
		boolean b = iTuisongzuoyeService.saveOrUpdateBatch(Arrays.asList(sio));
		if (b) {
			return ResponseBean.Success();
		} else {
			return ResponseBean.Fail();
		}
	}

	@ApiOperation(value = "去编辑")
	//@RequiresPermissions("")
	@GetMapping("/toUpdate")
	public ResponseBean<Tuisongzuoye> toUpdate(@ApiParam(value = "推送的信息id") @RequestParam(name = "id") String id) {
		//Tuisongzuoye tuisongzuoye = iTuisongzuoyeService.getById(id);
		Tuisongzuoye byIdTs = iTuisongzuoyeService.getByIdTs(id);
		if (ObjectUtil.isNotNull(byIdTs)) {
			return ResponseBean.Success(byIdTs);
		} else {
			return ResponseBean.Fail();
		}
	}


	@ApiOperation(value = "编辑")
	//@RequiresPermissions("")
	@PutMapping("/update")
	public ResponseBean update(@RequestBody Tuisongzuoye record) {
		record.setCrtm(new Date());
		if (iTuisongzuoyeService.updateById(record)) {
			return ResponseBean.Success();
		} else {
			return ResponseBean.Fail();
		}
	}

	@ApiOperation(value = "删除")
	//@RequiresPermissions("")
	@DeleteMapping("/remove")
	public ResponseBean remove(@ApiParam(value = "ids") @RequestParam(name = "ids",required = false) String[] ids) {
		if (iTuisongzuoyeService.removeByIds(Arrays.asList(ids))) {
			return ResponseBean.Success();
		} else {
			return ResponseBean.Fail();
		}
	}

	@ApiOperation(value = "提交作业并推送")
	//@RequiresPermissions("")
	@PostMapping("/submittedToPush")
	public ResponseBean submittedToPush(@RequestBody Tuisongzuoye[] sio) {

		//读取yaml的配置数据
		List<WxMaProperties.Config> resourceMapperList = wxp.getConfigs();
		Map<String, String> uriMap = resourceMapperList.stream()
				.collect(Collectors.toMap(WxMaProperties.Config::getAppid, WxMaProperties.Config::getSecret));
		Conversation conversation = new Conversation(uriMap.keySet().toArray(new String[1])[0],uriMap.values().toArray(new String[1])[0]);
		for(Tuisongzuoye i: sio){
			if (i.getSid() != null) {
				boolean b = iTuisongzuoyeService.save(i);
				String id = i.getId();
				Tuisongzuoye2Vo oneStu = iTuisongzuoyeService.getOneStu(id);
				Tuisongzuoye2Vo openidStu = iTuisongzuoyeService.getOpenidStu(oneStu.getSid());
				oneStu.setWxappopenid(openidStu.getWxappopenid());
				if(openidStu != null){
					String s = conversation.sendVlogCompleteTemplateMsg(oneStu);
					System.out.println(s);
					if(s.contains("ok")){
						String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
						UpdateWrapper<Tuisongzuoye> updateWrapper = new UpdateWrapper<Tuisongzuoye>().eq("id", id).set("whethertopush", 1)
								.set("chetime", time);
						iTuisongzuoyeService.update(updateWrapper);
					}else {
						return ResponseBean.Fail(this.iTuisongzuoyeService.selectStuName(i.getSid())+"推送失败！");
					}
				}else {
					return ResponseBean.Fail(this.iTuisongzuoyeService.selectStuName(i.getSid())+"没有登陆小程序,无法推送信息");
				}
			} else {
				return ResponseBean.Fail("学生id为空");
			}
		}

			return ResponseBean.Success("推送成功！");
	}


	@ApiOperation(value = "提交作业并推送2")
	//@RequiresPermissions("")
	@PostMapping("/submittedToPush2")
	public ResponseBean submittedToPush2(@RequestBody Tuisongzuoye[] sio) {
		int i = 0;
		List<WxMaProperties.Config> resourceMapperList = wxp.getConfigs();
		Map<String, String> uriMap = resourceMapperList.stream()
				.collect(Collectors.toMap(WxMaProperties.Config::getAppid, WxMaProperties.Config::getSecret));

		Conversation conversation = new Conversation(uriMap.keySet().toArray(new String[2])[0], uriMap.values().toArray(new String[2])[0]);
		if (sio != null) {
			List<String> list = new ArrayList<>();;
			for (Tuisongzuoye li : sio) {
				Tuisongzuoye2Vo openidStu = iTuisongzuoyeService.getOpenidStu(li.getSid());
				if (openidStu.getWxappopenid() == null) {
					list.add(openidStu.getSname());
				}
			}
			if(list != null && !list.isEmpty()){
				return ResponseBean.Fail(list+ "---" + "该学生没有登陆小程序,无法推送");
			}
			for (Tuisongzuoye li2 : sio) {
				Tuisongzuoye2Vo stu1 = iTuisongzuoyeService.getOpenidStu(li2.getSid());
				String gettname = iTuisongzuoyeService.gettname(li2.getTid());
				stu1.setTname(gettname);
				stu1.setThejobtype(li2.getThejobtype());
				stu1.setContent(li2.getContent());
				String s = conversation.sendVlogCompleteTemplateMsg2(stu1);
				if(s.contains("43101")){
					return ResponseBean.Fail("该学生没有同意消息推送");
				}
				if (s.contains("\"errmsg\":\"ok\"")) {
					li2.setWhethertopush(1);
					li2.setChetime(new Date());
					boolean save = iTuisongzuoyeService.save(li2);
					if(save){
						list.add(stu1.getSname());
					}else{
						return ResponseBean.Fail(stu1.getSname()+"-"+"数据插入出错,已停止"+"---"+list+"插入成功");
					}
				}else{
					return ResponseBean.Fail(stu1.getSname()+"---"+"推送失败,已停止"+"---"+list+"推送成功");
				}
				i++;
			}
			return ResponseBean.Success("已成功推送"+i+"条");
		}
			return ResponseBean.Success();
	}
}
