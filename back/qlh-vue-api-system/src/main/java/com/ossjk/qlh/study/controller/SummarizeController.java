package com.ossjk.qlh.study.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import com.ossjk.qlh.study.vo.SelfevaluationVo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ossjk.core.base.controller.BaseController;
import com.ossjk.core.system.api.ISystemCommonApi;
import com.ossjk.core.system.dto.LoginUser;
import com.ossjk.core.vo.ResponseBean;
import com.ossjk.qlh.edu.entity.Clazz;
import com.ossjk.qlh.edu.service.IClazzService;
import com.ossjk.qlh.edu.vo.ClazzVO;
import com.ossjk.qlh.study.entity.Summarize;
import com.ossjk.qlh.study.service.ISummarizeService;
import com.ossjk.qlh.study.vo.SummarizeVo;
import com.ossjk.qlh.stus.entity.Student;
import com.ossjk.qlh.stus.service.IStudentService;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * Copyright 2022-06-13 QLH. Tech Ltd. All rights reserved.
 * 
 * @Package: com.ossjk.qlh.xuexi.controller
 * @ClassName: SummarizeController
 * @Description: -控制器
 * @author: Rick.yang
 * @date: 2022-06-13 18:03:21
 */
@Api(tags = "每日反馈")
@RestController
@RequestMapping("/study/summarize")
public class SummarizeController extends BaseController {

    @Autowired
    private ISummarizeService iSummarizeService;

    @Autowired
    private IStudentService iStudentService;

    @Autowired
    private IClazzService iClazzService;

    @Autowired
    private ISystemCommonApi iSystemCommonApi;

    @ApiOperation(value = "列表")
    // @RequiresPermissions("")
    @GetMapping("/list")
    public ResponseBean<Page<SummarizeVo>> list(Page page, SummarizeVo summarizeVo, HttpServletRequest request) {

        LoginUser me = iSystemCommonApi.refreshTokenCacheLoginUser(this.getToekn(request));
        String roleStr = me.getRoleCode().iterator().next();
        QueryWrapper<SummarizeVo> queryWrapper = new QueryWrapper<SummarizeVo>().orderByAsc("`readst`");
        if (StrUtil.isNotBlank(summarizeVo.getSname())) {
            queryWrapper.like("sname", "%" + summarizeVo.getSname() + "%");
        }
        if (StrUtil.isNotBlank(summarizeVo.getCname())) {
            queryWrapper.eq("cname", summarizeVo.getCname());
        }
        System.out.println(summarizeVo.getReadst());
        if (StrUtil.isNotBlank(summarizeVo.getReadst())) {
            queryWrapper.eq("readst", summarizeVo.getReadst());
        }
        if ("tea".equals(roleStr)) {
            queryWrapper.eq("tname", me.getName());
        }
        return ResponseBean.Success(iSummarizeService.selectVo(page, queryWrapper));
    }

    /**
     * cname列表
     *
     * @param request 请求.ge("stucnt", 0)
     * @return {@link ResponseBean}<{@link Map}<{@link String}, {@link Object}>>
     */
    @ApiOperation(value = "列出班级")
    // @RequiresPermissions("")
    @GetMapping("/listCname")
    public ResponseBean<Map<String, Object>> listCname(Page page, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        LoginUser me = iSystemCommonApi.refreshTokenCacheLoginUser(this.getToekn(request));
        String roleStr = me.getRoleCode().iterator().next();
        QueryWrapper<Clazz> queryWrapper = new QueryWrapper<Clazz>().select("DISTINCT id,`name`").ge("stucnt", 0);
        if ("tea".equals(roleStr)) {
            queryWrapper.eq("tid", me.getId());
        }
        Page<ClazzVO> clazzList = iClazzService.pageVO(page, queryWrapper);
        map.put("clazzList", clazzList.getRecords());
        return ResponseBean.Success(map);
    }

    @ApiOperation(value = "添加")
    @RequiresPermissions("002233001")
    @PostMapping("/save")
    public ResponseBean save(@RequestBody Summarize record) {
        SaveOrUpdate(record);
        if (iSummarizeService.save(record)) {
            return ResponseBean.Success();
        } else {
            return ResponseBean.Fail();
        }
    }

    private void SaveOrUpdate(@RequestBody Summarize record) {
        Student student = iStudentService.getById(record.getSid());
        Clazz clazz = iClazzService.getById(student.getCid());
        record.setCid(clazz.getId());
        record.setCourseid(clazz.getCid());
        record.setTid(clazz.getTid());
    }

    @ApiOperation(value = "编辑")
    @RequiresPermissions("002233005")
    @PutMapping("/update")
    public ResponseBean update(@RequestBody Summarize record) {
        SaveOrUpdate(record);
        if (iSummarizeService.updateById(record)) {
            return ResponseBean.Success();
        } else {
            return ResponseBean.Fail();
        }
    }

    @ApiOperation(value = "去编辑")
    // @RequiresPermissions("")
    @GetMapping("/toUpdate")
    public ResponseBean<SummarizeVo> toUpdate(@ApiParam(value = "id") @RequestParam(name = "id") String id) {
        SummarizeVo summarizeVos = iSummarizeService.selectVoById(id);
        if (ObjectUtil.isNotNull(summarizeVos)) {
            return ResponseBean.Success(summarizeVos);
        } else {
            return ResponseBean.Fail();
        }
    }

    @ApiOperation(value = "删除")
    @RequiresPermissions("002233006")
    @DeleteMapping("/remove")
    public ResponseBean remove(@ApiParam(value = "ids") @RequestParam(name = "ids") String[] ids) {
        if (iSummarizeService.removeByIds(Arrays.asList(ids))) {
            return ResponseBean.Success();
        } else {
            return ResponseBean.Fail();
        }
    }

    /**
     * 批量修改已读状态为已读 0未读1已读
     * 
     * @param ids
     * @return
     */
    @ApiOperation(value = "修改已读状态为已读")
    // @RequiresPermissions("")
    @DeleteMapping("/updateReadst")
    public ResponseBean updateReadst(@ApiParam(value = "ids-数据ID") @RequestParam(name = "ids") String[] ids) {
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        UpdateWrapper<Summarize> updateWrapper = new UpdateWrapper<Summarize>().in("id", ids).set("readst", 1)
                .set("chetime", time);
        boolean f = iSummarizeService.update(updateWrapper);
        if (f) {
            return ResponseBean.Success();
        } else {
            return ResponseBean.Fail();
        }
    }

    /**
     * 修改已读状态为已读 0未读1已读
     * 
     * @param id
     * @return
     */
    @ApiOperation(value = "修改已读状态为已读")
    // @RequiresPermissions("")
    @DeleteMapping("/updateReadst2")
    public ResponseBean updateReadst2(@ApiParam(value = "id-数据ID") @RequestParam(name = "id") String id) {
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        UpdateWrapper<Summarize> updateWrapper = new UpdateWrapper<Summarize>().eq("id", id).set("readst", 1)
                .set("chetime", time);
        boolean f = iSummarizeService.update(updateWrapper);
        if (f) {
            return ResponseBean.Success();
        } else {
            return ResponseBean.Fail();
        }
    }

    @ApiOperation(value = "小程序-查询指定学生的数据,并查看")
    @GetMapping("/modifyAndView")
    public ResponseBean<List<SummarizeVo>> modifyAndView(@ApiParam(value = "id-班级ID") @RequestParam(name = "cid", required = true)String cid,
                                                         @ApiParam(value = "指定的日期时间") @RequestParam(name = "subtime", required = true)String subtime,
                                                         @ApiParam(value = "id-学生ID") @RequestParam(name = "sid", required = true)String sid){
        List<SummarizeVo> dateCid = iSummarizeService.modifyAndView(subtime, cid,sid);
        return ResponseBean.Success(dateCid);
    }

    @ApiOperation(value = "小程序-日期时间和查询绑在一起")
    @GetMapping("/dateDisplay")
    public ResponseBean<SummarizeVo> dateDisplay(@ApiParam(value = "id-班级ID") @RequestParam(name = "cid", required = true)String cid,
                                                 @ApiParam(value = "指定的日期时间") @RequestParam(name = "subtime", required = true)String subtime){
        SummarizeVo summarizeVo = iSummarizeService.dateDisplay(cid, subtime);
        summarizeVo.setTheCurrentTime(subtime);
        return ResponseBean.Success(summarizeVo);
    }

    @ApiOperation(value = "小程序-显示班级全部学生提交情况")
    @GetMapping("/getcircumstance")
    public ResponseBean<SummarizeVo> getcircumstance(@ApiParam(value = "cid-班级ID") @RequestParam(name = "cid") String cid,
                                                     @ApiParam(value = "subtime-时间") @RequestParam(name = "subtime") String subtime) {

        SummarizeVo getclazzvaluetion = iSummarizeService.getcircumstance(subtime,cid);
        return ResponseBean.Success(getclazzvaluetion);
    }

    @ApiOperation(value = "小程序-学生自我反馈的总数")
    @GetMapping("/getclazzvaluetion")
    public ResponseBean<List<SummarizeVo>> getclazzvaluetion(@ApiParam(value = "cid-班级ID") @RequestParam(name = "cid") String cid,
                                                             @ApiParam(value = "subtime-时间") @RequestParam(name = "subtime") String subtime) {
        List<SummarizeVo> getclazzvaluetion = iSummarizeService.getclazzvaluetion(cid,subtime);
        return ResponseBean.Success(getclazzvaluetion);
    }

    /**
     * 修改已读状态为已读 0未读1已读
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "小程序-修改已读状态为已读")
    // @RequiresPermissions("")
    @GetMapping("/updatelist")
    public ResponseBean updatelist(@ApiParam(value = "id-数据ID") @RequestParam(name = "id") String id) {
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        UpdateWrapper<Summarize> updateWrapper = new UpdateWrapper<Summarize>().eq("id", id).set("readst", 1)
                .set("chetime", time);
        boolean f = iSummarizeService.update(updateWrapper);
        if (f) {
            return ResponseBean.Success();
        } else {
            return ResponseBean.Fail();
        }
    }
}
