package com.ossjk.qlh.study.controller;


import cn.hutool.core.util.StrUtil;
import com.ossjk.core.base.controller.BaseController;
import com.ossjk.core.system.api.ISystemCommonApi;
import com.ossjk.core.system.dto.LoginUser;
import com.ossjk.core.vo.ResponseBean;
import com.ossjk.qlh.study.entity.Feedback;
import com.ossjk.qlh.study.service.IFeedService;
import com.ossjk.qlh.study.vo.NaviVO;
import com.ossjk.qlh.study.vo.PersonDataVo;
import com.ossjk.qlh.study.vo.StudentVO;
import com.ossjk.qlh.study.vo.TeacherClz;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Api(tags = "小程序每日总结")
@RestController
@RequestMapping("/teacherApplet/feed")
public class FeedController extends BaseController {

    @Autowired
    private IFeedService iFeedService;

    @Autowired
    private ISystemCommonApi systemCommonApi;

    @ApiOperation(value = "班级下拉框")
    // @RequiresPermissions("")
    @GetMapping("/listDown")
    public ResponseBean<List<TeacherClz>> dropDownBox(HttpServletRequest request) {
        String token = this.getToekn(request);
        LoginUser uer = systemCommonApi.getCacheLoginUser(token);
        return ResponseBean.Success(iFeedService.selectClass(uer.getId()));
    }

    @ApiOperation(value = "导航显示")
    // @RequiresPermissions("")
    @GetMapping("/list")
    public ResponseBean<NaviVO> lookDate(@ApiParam(value = "班级id") @RequestParam(name = "cid") String sid,
                                         @ApiParam(value = "当前时间") @RequestParam(name = "cur") String cur) {
        if (StrUtil.isNotBlank(cur)){
            NaviVO naviVO = iFeedService.selectNavi(sid,cur);
            if(naviVO != null){
                naviVO.setCur(cur);
                System.out.println(naviVO);
                if(StrUtil.isNotEmpty(sid)){
                    return ResponseBean.Success(naviVO);
                }else {
                    return ResponseBean.Fail("班级id为空");
                }
            }else {
                return ResponseBean.Success("{cur:"+cur+"}");
            }
        }else {
            return ResponseBean.Fail("当前日期为空");
        }
    }

    @ApiOperation(value = "显示提交情况")
    // @RequiresPermissions("")
    @GetMapping("/listPerson")
    public ResponseBean<PersonDataVo> lookPerson(HttpServletRequest request,
                                            @ApiParam(value = "班级id") @RequestParam(name = "cid") String cid,
                                            @ApiParam(value = "当前时间") @RequestParam(name = "cur") String cur) {
        return ResponseBean.Success(iFeedService.showData(cid,cur));
    }

    @ApiOperation(value = "列出总结")
    // @RequiresPermissions("")
    @GetMapping("/lookData")
    public ResponseBean<List<Feedback>> lookData(HttpServletRequest request,
                                                 @ApiParam(value = "学生id") @RequestParam(name = "sid") String sid,
                                                 @ApiParam(value = "当前时间") @RequestParam(name = "cur") String cur) {
        return ResponseBean.Success(iFeedService.selectAll(sid,cur));
    }

    @ApiOperation(value = "老师评价")
    // @RequiresPermissions("")
    @GetMapping("/evaluate")
    public ResponseBean<Integer> evaluate(@ApiParam(value = "总结id") @RequestParam(name = "id") String id,
                                             @ApiParam(value = "评分") @RequestParam(name = "score") String score) {

        return ResponseBean.Success(iFeedService.upDataFeed(id,score));
    }

    @ApiOperation(value = "显示学生")
    // @RequiresPermissions("")
    @GetMapping("/lookStudent")
    public ResponseBean<List<StudentVO>> lookStudent(@ApiParam(value = "班级id") @RequestParam(name = "cid") String cid,
                                                     @ApiParam(value = "当前时间") @RequestParam(name = "cur") String cur) {

        List<StudentVO> student = iFeedService.selectStudent(cid,cur);
        if(StrUtil.isNotEmpty(cur)){
            return ResponseBean.Success(student);
        }else {
            return ResponseBean.Fail("当前日期为空");
        }
    }

}
