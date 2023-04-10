package com.ossjk.qlh.study.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ossjk.core.base.controller.BaseController;
import com.ossjk.core.system.api.ISystemCommonApi;
import com.ossjk.core.system.dto.LoginUser;
import com.ossjk.core.system.dto.UploadDto;
import com.ossjk.core.vo.ResponseBean;
import com.ossjk.qlh.study.entity.Myclass;
import com.ossjk.qlh.study.service.IMyclassService;
import com.ossjk.qlh.system.entity.Student;
import com.ossjk.qlh.system.service.IStudentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright 2022-06-16 QLH. Tech Ltd. All rights reserved.
 * 
 * @Package: com.ossjk.qlh.xuexi.controller
 * @ClassName: MyclassController
 * @Description: -控制器
 * @author: Rick.yang
 * @date: 2022-06-16 17:38:14
 */
@Api(tags = "我的班级")
@RestController
@RequestMapping("/study/myclass")
public class MyclassController extends BaseController {

    @Autowired
    private ISystemCommonApi systemCommonApi;

    @Autowired
    private IMyclassService iMyclassService;

    @Autowired
    private IStudentService studentService;

    @ApiOperation(value = "返回学生班级")
    // @RequiresPermissions("")
    @GetMapping("/listCname")
    public ResponseBean<Myclass> listCname(HttpServletRequest request) {
        LoginUser user = systemCommonApi.getCacheLoginUser(this.getToekn(request));
        return ResponseBean.Success(iMyclassService.getOneClass(user.getId()));
    }

    @ApiOperation(value = "返回学生集合")
    @GetMapping("/getStudentList")
    public ResponseBean<List<Student>> getStudentList(HttpServletRequest request) {
        LoginUser user = systemCommonApi.getCacheLoginUser(this.getToekn(request));
        return  iMyclassService.getStudentList(iMyclassService.getStudent(user.getId()).getCid());
    }

    @ApiOperation(value = "返回学生和教室")
    @GetMapping("/getStudent")
    public ResponseBean<List<Object>> getStudent(HttpServletRequest request) {
        LoginUser user = systemCommonApi.getCacheLoginUser(this.getToekn(request));
        List<Object> list = new ArrayList<>();
        // 获取学生信息
        list.add(iMyclassService.getStudent(user.getId()));
        // 获取班级信息
        list.add(iMyclassService.getOneClass(user.getId()));
        return ResponseBean.Success(list);
    }

    @ApiOperation(value = "修改头像")
    @PostMapping("/getPicture")
    public void getPicture(HttpServletRequest request, @ApiParam(value = "文件对象",required = true)MultipartFile file, @ApiParam(value = "上传模式")UploadDto uploadDto) throws IOException {
        LoginUser user = systemCommonApi.getCacheLoginUser(this.getToekn(request));
        iMyclassService.updatePicture(file,uploadDto,user.getId());
    }

    @ApiOperation(value = "微信修改头像")
    @PostMapping("/wxGetPicture")
    public void wxGetPicture(HttpServletRequest request, @ApiParam(value = "Base64文件",required = true)String base) throws IOException {
        LoginUser user = systemCommonApi.getCacheLoginUser(this.getToekn(request));
        Student student = new Student();
        student.setId(user.getId());
        student.setHead(base);
        studentService.updateById(student);
    }
}
