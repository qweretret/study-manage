package com.ossjk.qlh.exams.controller;

import java.util.Arrays;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ossjk.core.base.controller.BaseController;
import com.ossjk.core.vo.ResponseBean;
import com.ossjk.qlh.exams.entity.Exampaper;
import com.ossjk.qlh.exams.service.IExampaperService;

import cn.hutool.core.util.ObjectUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * Copyright 2022-06-14 QLH. Tech Ltd. All rights reserved.
 * 
 * @Package: com.ossjk.qlh.exams.controller
 * @ClassName: ExampaperController
 * @Description: Exampaper-控制器
 * @author:
 * @date: 2022-06-14 11:31:17
 */
@Api(tags = "试卷")
@RestController
@RequestMapping("/exams/exampaper")
@Slf4j
public class ExampaperController extends BaseController {

    @Autowired
    private IExampaperService iExampaperService;

    @ApiOperation(value = "列表")
    // @RequiresPermissions("")
    @GetMapping("/list")
    public ResponseBean<Page<Exampaper>> list(Page page,@ApiParam(value = "试卷名称") @RequestParam(name = "name",required = false) String name) {

        QueryWrapper<Exampaper> queryWrapper = new QueryWrapper<Exampaper>();


        //根据是否有传参数过来进行不同操作

        if(null==name)//无参
        {
            //根据创建时间降序排序
            queryWrapper.orderByDesc("crtm");
            return ResponseBean.Success(iExampaperService.page(page, queryWrapper));
        }
        else{// 有参
            //根据输入的试卷名称进行模糊查询
            queryWrapper.like("name",name);
            //根据创建时间降序排序
            queryWrapper.orderByDesc("crtm");
            return ResponseBean.Success(iExampaperService.page(page, queryWrapper));
        }

    }

    @ApiOperation(value = "列表")
    // @RequiresPermissions("")
    @GetMapping("/list2")
    public ResponseBean<List<Exampaper>> list(Exampaper page) {
        QueryWrapper<Exampaper> queryWrapper = new QueryWrapper<Exampaper>().eq("kid", page.getKid());
        return ResponseBean.Success(iExampaperService.list(queryWrapper));
    }

    @ApiOperation(value = "添加")
    // @RequiresPermissions("")
    @PostMapping("/save")
    public ResponseBean save(@RequestBody Exampaper record) {
        if (iExampaperService.save(record)) {
            return ResponseBean.Success();
        } else {
            return ResponseBean.Fail();
        }
    }

    @ApiOperation(value = "去编辑")
    // @RequiresPermissions("")
    @GetMapping("/toUpdate")
    public ResponseBean<Exampaper> toUpdate(@ApiParam(value = "id") @RequestParam(name = "id") String id) {
        Exampaper exampaper = iExampaperService.getById(id);
        if (ObjectUtil.isNotNull(exampaper)) {
            return ResponseBean.Success(exampaper);
        } else {
            return ResponseBean.Fail();
        }
    }

    @ApiOperation(value = "编辑")
    // @RequiresPermissions("")
    @PutMapping("/update")
    public ResponseBean update(@RequestBody Exampaper record) {
        if (iExampaperService.updateById(record)) {
            return ResponseBean.Success();
        } else {
            return ResponseBean.Fail();
        }
    }

    @ApiOperation(value = "删除")
    // @RequiresPermissions("")
    @DeleteMapping("/remove")
    public ResponseBean remove(@ApiParam(value = "ids") @RequestParam(name = "ids") String[] ids) {
        if (iExampaperService.removeByIds(Arrays.asList(ids))) {
            return ResponseBean.Success();
        } else {
            return ResponseBean.Fail();
        }
    }
}
