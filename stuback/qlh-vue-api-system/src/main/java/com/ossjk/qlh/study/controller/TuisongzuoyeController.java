package com.ossjk.qlh.study.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ossjk.core.base.controller.BaseController;
import com.ossjk.core.system.api.ISystemCommonApi;
import com.ossjk.core.system.dto.LoginUser;
import com.ossjk.core.vo.ResponseBean;
import com.ossjk.qlh.study.entity.Tuisongzuoye;
import com.ossjk.qlh.study.service.ITuisongzuoyeService;
import com.ossjk.qlh.study.service.vo.Tuisongzuoye2Vo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;

/**
 * Copyright  2022-08-05 QLH. Tech Ltd. All rights reserved.
 * 
 * @Package: com.ossjk.qlh.xueli.controller
 * @ClassName: TuisongzuoyeController
 * @Description: -控制器
 * @author: Rick.yang
 * @date:  2022-08-05 14:40:29 
 */
@Api(tags = "学生作业信息签收")
@RestController
@RequestMapping("/lky/tuisongzuoye")
public class TuisongzuoyeController extends BaseController {
	
	@Autowired
	private ITuisongzuoyeService iTuisongzuoyeService;

	@Autowired
	private ISystemCommonApi systemCommonApi;

	@ApiOperation(value = "作业列表展示-含小程序")
	//@RequiresPermissions("")
	@GetMapping("/list")
	public ResponseBean<Page<Tuisongzuoye2Vo>> list(@ApiParam(value = "状态-默认0未签收",required = true)@RequestParam("whethertopush") String whethertopush,
													@ApiParam(value = "作业类型-默认全部",required = true)@RequestParam(value = "thejobtype",required = false)String thejobtype,Page page, HttpServletRequest request) {
		String token = this.getToekn(request);
		LoginUser uer = systemCommonApi.getCacheLoginUser(token);

		QueryWrapper<Tuisongzuoye2Vo> lqw = new QueryWrapper<>();
		     lqw.eq(uer.getId() != null,"sid",uer.getId())
				.eq(thejobtype !=null && !thejobtype.equals("null"),"thejobtype",thejobtype)
				.isNull(Integer.parseInt(whethertopush) == 0,"subtime")
				.isNotNull(Integer.parseInt(whethertopush) == 1,"subtime");
		return ResponseBean.Success(iTuisongzuoyeService.list(page,lqw));
	}

	@ApiOperation(value = "去签收-作业ID-含小程序")
	//@RequiresPermissions("")
	@GetMapping("/qianshou")
	public ResponseBean qianshou(@ApiParam(value = "签收的信息id",required = true) @RequestParam(name = "aid") String id) {
		UpdateWrapper<Tuisongzuoye> qw = new UpdateWrapper<Tuisongzuoye>();
		qw.eq("id",id);
		qw.set("subtime",new Date());
		boolean update = iTuisongzuoyeService.update(qw);
		if (update) {
			return ResponseBean.Success();
		} else {
			return ResponseBean.Fail();
		}
	}

	@ApiOperation(value = "删除-待定")
	//@RequiresPermissions("")
	@DeleteMapping("/remove")
	public ResponseBean remove(@ApiParam(value = "ids",required = true) @RequestParam(name = "ids") String[] ids) {
		if (iTuisongzuoyeService.removeByIds(Arrays.asList(ids))) {
			return ResponseBean.Success();
		} else {
			return ResponseBean.Fail();
		}
	}
}
