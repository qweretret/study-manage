package com.ossjk.qlh.study.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ossjk.core.base.controller.BaseController;
import com.ossjk.core.system.api.ISystemCommonApi;
import com.ossjk.core.system.dto.LoginUser;
import com.ossjk.core.vo.ResponseBean;
import com.ossjk.qlh.edu.entity.Clazz;
import com.ossjk.qlh.edu.service.IClazzService;
import com.ossjk.qlh.study.entity.Feedback;
import com.ossjk.qlh.study.service.IFeedbackService;
import com.ossjk.qlh.study.service.vo.CourseVo;
import com.ossjk.qlh.study.service.vo.FeedbackVo;
import com.ossjk.qlh.system.entity.Student;
import com.ossjk.qlh.system.service.IStudentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.ibatis.logging.stdout.StdOutImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

/**
 * Copyright  2022-07-05 QLH. Tech Ltd. All rights reserved.
 * 
 * @Package: com.ossjk.qlh.study.controller
 * @ClassName: FeedbackController
 * @Description: Feedback-控制器
 * @author: Rick.yang
 * @date:  2022-07-05 16:48:09 
 */
@Api(tags = "总结表")
@RestController
@RequestMapping("/study/feedback")
public class FeedbackController extends BaseController {
	
	@Autowired
	private IFeedbackService iFeedbackService;
	@Autowired
	private IStudentService iStudentService;
	@Autowired
	private IClazzService iClazzService;
	@Autowired
	private ISystemCommonApi iSystemCommonApi;

	@Autowired
	private ISystemCommonApi systemCommonApi;


	@ApiOperation(value = "列表")
	// @RequiresPermissions("")
	@GetMapping("/list")
	public ResponseBean<Page<FeedbackVo>> list(Page page, FeedbackVo feedbackVo,
													 HttpServletRequest request) {
		String token = this.getToekn(request);
		LoginUser me = systemCommonApi.getCacheLoginUser(token);
		QueryWrapper<FeedbackVo> queryWrapper = new QueryWrapper<FeedbackVo>().orderByAsc("`readst`");
		if (StrUtil.isNotBlank(feedbackVo.getSname())) {
			queryWrapper.like("sname", "%" + feedbackVo.getSname() + "%");
		}
		if (StrUtil.isNotBlank(feedbackVo.getCname())) {
			queryWrapper.eq("cname", feedbackVo.getCname());
		}
		System.out.println(feedbackVo.getReadst());
		if (StrUtil.isNotBlank(feedbackVo.getReadst())) {
			queryWrapper.eq("readst", feedbackVo.getReadst());
		}
			queryWrapper.eq("sid",me.getId());
		return ResponseBean.Success(iFeedbackService.selectVo(page, queryWrapper));
	}

	@ApiOperation(value = "学生小程序每日总结功能")
	// @RequiresPermissions("")
	@GetMapping("/listStuxiao")
	public ResponseBean<List<FeedbackVo>> listStuxiao(HttpServletRequest request,
												@ApiParam(value = "状态数`0~4`-默认3已读并合格",required = true) @RequestParam(name = "state") Integer state) {
		String token = this.getToekn(request);
		LoginUser me = systemCommonApi.getCacheLoginUser(token);
		QueryWrapper<FeedbackVo> queryWrapper = new QueryWrapper<FeedbackVo>();
		switch (state){
			//查询全部
			case 0:
				break;
			//查询已读
			case 1:
				queryWrapper.isNotNull("score");
				break;
			//查询未读
			case 2:
				queryWrapper.isNull("score");
				break;
			//查询已读，并合格
			case 3:
				queryWrapper.isNotNull("tid");
				queryWrapper.ge("score",3);
				break;
			//查询已读，并不合格
			case 4:
				queryWrapper.isNotNull("tid");
				queryWrapper.lt("score",3);
				break;
		}
		queryWrapper.eq("sid",me.getId());
		return ResponseBean.Success(iFeedbackService.listStu(queryWrapper));
	}


//	@ApiOperation(value = "感应name列表")
//	//@RequiresPermissions("")
//	@GetMapping("/listIntraNm")
//	public ResponseBean<List<Feedback>> listIntraName(@ApiParam(value = "sname") @RequestParam(name = "sname") String sname) {
//		return ResponseBean.Success(iFeedbackService.interaSname(sname) );
//	}

	@ApiOperation(value = "列表")
	// @RequiresPermissions("")
	@GetMapping("/list2")
	public ResponseBean<List<FeedbackVo>> list2(Page page, FeedbackVo feedbackVo) {
		QueryWrapper<FeedbackVo> queryWrapper = new QueryWrapper<FeedbackVo>();

		if (StrUtil.isNotBlank(feedbackVo.getCname())) {
			queryWrapper.eq("c.name", feedbackVo.getCname());
		}
		return ResponseBean.Success(iFeedbackService.selectVoList(queryWrapper));
	}

//	@ApiOperation(value = "列出班级")
//	// @RequiresPermissions("")
//	@GetMapping("/listCname")
//	public ResponseBean<Map<String, Object>> listCname() {
//		Map<String, Object> map = new HashMap<>();
//		QueryWrapper<Clazz> queryWrapper = new QueryWrapper<Clazz>().select("DISTINCT id,`name`");
//		List<Clazz> clazzList = iClazzService.list(queryWrapper);
//		map.put("clazzList", clazzList);
//		return ResponseBean.Success(map);
//	}


//	@ApiOperation(value = "添加DTO")
//	//@RequiresPermissions("")
//	@PostMapping("/saveDTO")
//	public ResponseBean saveDTO(@RequestBody FeedbackDTO dto , HttpServletRequest request ) {
//		String token = this.getToekn(request);
//		LoginUser me = systemCommonApi.getCacheLoginUser(token);
//		System.out.println(dto);
//		dto.getFb().setSid(me.getId());
//
//		if (iFeedbackService.saveDTO(dto.getFb(), dto.getStu(),dto.getCla())){
//			return ResponseBean.Success();
//		} else {
//			return ResponseBean.Fail();
//		}
//	}

	@ApiOperation(value = "添加")
	// @RequiresPermissions("")
	@PostMapping("/save")
	public ResponseBean save(@RequestBody Feedback record, HttpServletRequest request) {
		String token = this.getToekn(request);
		LoginUser me = iSystemCommonApi.getCacheLoginUser(token);
		Feedback bydate = iFeedbackService.getBydate(me.getId(), record.getSubtime());
		if(bydate == null){
			record.setSid(me.getId());
			if (iFeedbackService.save(record)) {
				return ResponseBean.Success();
			} else {
				return ResponseBean.Fail();
			}
		}else{
			return ResponseBean.Fail("一天只能推一条总结");
		}
	}


	private void SaveOrUpdate(@RequestBody Feedback record) {
		Student student = iStudentService.getById(record.getSid());
		Clazz clazz = iClazzService.getById(student.getCid());
//		Course course = iClazzService.getById()
		record.setCid(clazz.getId());
		record.setTid(clazz.getTid());
	}




	@ApiOperation(value = "去编辑")
//	@RequiresPermissions("")
	@GetMapping("/toUpdate")
	public ResponseBean<Feedback> toUpdate(@ApiParam(value = "id") @RequestParam(name = "id") String id) {
		Feedback feedback = iFeedbackService.getById(id);
		if (ObjectUtil.isNotNull(feedback)) {
			return ResponseBean.Success(feedback);
		} else {
			return ResponseBean.Fail();
		}
	}


	@ApiOperation(value = "编辑")
//	@RequiresPermissions("")
	@PutMapping("/update")
	public ResponseBean update(@RequestBody Feedback record) {
		if (iFeedbackService.updateById(record)) {
			return ResponseBean.Success();
		} else {
			return ResponseBean.Fail();
		}
	}

	@ApiOperation(value = "删除")
//	@RequiresPermissions("")
	@DeleteMapping("/remove")
	public ResponseBean remove(@ApiParam(value = "ids") @RequestParam(name = "ids") String[] ids) {
		if (iFeedbackService.removeByIds(Arrays.asList(ids))) {
			return ResponseBean.Success();
		} else {
			return ResponseBean.Fail();
		}
	}
}
