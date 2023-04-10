package com.ossjk.qlh.exams.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ossjk.core.base.controller.BaseController;
import com.ossjk.core.vo.ResponseBean;
import com.ossjk.qlh.exams.entity.Answers;
import com.ossjk.qlh.exams.service.IAnswersService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * Copyright  2022-06-14 QLH. Tech Ltd. All rights reserved.
 * 
 * @Package: com.ossjk.qlh.exams.controller
 * @ClassName: AnswersController
 * @Description: Answers-控制器
 * @author: 
 * @date:  2022-06-14 11:31:17 
 */
@Api(tags = "答卷（题）表")
@RestController
@RequestMapping("/exams/answers")
public class AnswersController extends BaseController {
	
	@Autowired
	private IAnswersService iAnswersService;

	@ApiOperation(value = "列表")
//	//@RequiresPermissions("")
	@GetMapping("/list")
	public ResponseBean<Page<Answers>> list(Page page) {
		QueryWrapper<Answers> queryWrapper = new QueryWrapper<Answers>();
		return ResponseBean.Success(iAnswersService.page(page,queryWrapper));
	}

	@ApiOperation(value = "添加")
//	//@RequiresPermissions("")
	@PostMapping("/save")
	public ResponseBean save(@RequestBody Answers record) {
		if (iAnswersService.save(record)) {
			return ResponseBean.Success();
		} else {
			return ResponseBean.Fail();
		}
	}

	@ApiOperation(value = "去编辑")
	//@RequiresPermissions("")
	@GetMapping("/toUpdate")
	public ResponseBean<Answers> toUpdate(@ApiParam(value = "id") @RequestParam(name = "id") String id) {
		Answers answers = iAnswersService.getById(id);
		if (ObjectUtil.isNotNull(answers)) {
			return ResponseBean.Success(answers);
		} else {
			return ResponseBean.Fail();
		}
	}


	@ApiOperation(value = "编辑")
	//@RequiresPermissions("")
	@PutMapping("/update")
	public ResponseBean update(@RequestBody Answers record) {
		if (iAnswersService.updateById(record)) {
			return ResponseBean.Success();
		} else {
			return ResponseBean.Fail();
		}
	}

	@ApiOperation(value = "删除")
	//@RequiresPermissions("")
	@DeleteMapping("/remove")
	public ResponseBean remove(@ApiParam(value = "ids") @RequestParam(name = "ids") String[] ids) {
		if (iAnswersService.removeByIds(Arrays.asList(ids))) {
			return ResponseBean.Success();
		} else {
			return ResponseBean.Fail();
		}
	}
}
