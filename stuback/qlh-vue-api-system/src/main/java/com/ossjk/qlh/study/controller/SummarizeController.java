package com.ossjk.qlh.study.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ossjk.core.base.controller.BaseController;
import com.ossjk.core.system.api.ISystemCommonApi;
import com.ossjk.core.system.dto.LoginUser;
import com.ossjk.core.vo.ResponseBean;
import com.ossjk.qlh.edu.entity.Clazz;
import com.ossjk.qlh.edu.service.IClazzService;
import com.ossjk.qlh.study.entity.Summarize;
import com.ossjk.qlh.study.service.ISummarizeService;
import com.ossjk.qlh.study.service.vo.DictionaryDataVo;
import com.ossjk.qlh.study.service.vo.SummarizeVo;
import com.ossjk.qlh.system.entity.Student;
import com.ossjk.qlh.system.service.IStudentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Copyright 2022-06-13 QLH. Tech Ltd. All rights reserved.
 * 
 * @Package: com.ossjk.qlh.xuexi.controller
 * @ClassName: SummarizeController
 * @Description: -控制器
 * @author: Rick.yang
 * @date: 2022-06-13 18:03:21
 */
@Api(tags = "自我反馈")
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

    @ApiOperation(value = "微信列表")
    // @RequiresPermissions("")
    @GetMapping("/wxList")
    public ResponseBean<Page<SummarizeVo>> wxList(Page page, @ApiParam(value = "状态（0-未读，1-已读）") @RequestParam(name = "readst")String readst,
                                                  HttpServletRequest request) {
        LoginUser me = iSystemCommonApi.refreshTokenCacheLoginUser(this.getToekn(request));
        String roleStr = me.getRoleCode().iterator().next();
        if(!("0".equals(readst) || "1".equals(readst))) {
            readst = "3";
        }
        return ResponseBean.Success(iSummarizeService.selectWxVo(page, me.getId(),readst));
    }
    @ApiOperation(value = "微信添加")
    // @RequiresPermissions("")
    @PostMapping("/wxSave")
    public ResponseBean wxSave(@RequestBody Summarize record,HttpServletRequest request) {
        String token = this.getToekn(request);
        LoginUser me = iSystemCommonApi.getCacheLoginUser(token);
        record.setSubtime(new Date());
        if(iSummarizeService.subtime(me.getId(),record.getSubtime())!=null){
            return ResponseBean.Fail("今天已反馈，无法再次反馈！");
        }
        if(!("0".equals(record.getA1())  || "0".equals(record.getA2())  || "0".equals(record.getA3())  || "0".equals(record.getA4())  || "0".equals(record.getA5())  || "0".equals(record.getA6())  ||  "".equals(record.getContent()))){
            record.setSid(me.getId());
            DictionaryDataVo dictionaryDataVo = iSummarizeService.dictionarydata(record.getA1(),record.getA2(),record.getA3(),record.getA4(),record.getA5(),record.getA6());
            record.setA1(dictionaryDataVo.getA1());
            record.setA2(dictionaryDataVo.getA2());
            record.setA3(dictionaryDataVo.getA3());
            record.setA4(dictionaryDataVo.getA4());
            record.setA5(dictionaryDataVo.getA5());
            record.setA6(dictionaryDataVo.getA6());
            record.setSubtime(new Date());
            if (iSummarizeService.save(record)) {
                return ResponseBean.Success();
            } else {
                return ResponseBean.Fail("查询报错！");
            }
        }else {
            return ResponseBean.create(1002, "内容不能为空", null);
        }

    }

    @ApiOperation(value = "列表")
    // @RequiresPermissions("")
    @GetMapping("/list")
    public ResponseBean<Page<SummarizeVo>> list(Page page, SummarizeVo summarizeVo, HttpServletRequest request) {

        LoginUser me = iSystemCommonApi.refreshTokenCacheLoginUser(this.getToekn(request));
        String roleStr = me.getRoleCode().iterator().next();
        QueryWrapper<SummarizeVo> queryWrapper = new QueryWrapper<SummarizeVo>().orderByDesc("`subtime`");
        System.out.println(summarizeVo.getReadst());
        if (StrUtil.isNotBlank(summarizeVo.getReadst())) {
            queryWrapper.eq("readst", summarizeVo.getReadst());
        }
        queryWrapper.eq("sid", me.getId());
        return ResponseBean.Success(iSummarizeService.selectVo(page, queryWrapper));
    }


    @ApiOperation(value = "添加")
    // @RequiresPermissions("")
    @PostMapping("/save")
    public ResponseBean save(@RequestBody Summarize record,HttpServletRequest request) {
        String token = this.getToekn(request);
        LoginUser me = iSystemCommonApi.getCacheLoginUser(token);
        record.setSubtime(new Date());
        if(iSummarizeService.subtime(me.getId(),record.getSubtime())!=null){
            return ResponseBean.Fail("今天已反馈，无法再次反馈！");
        }
        record.setSid(me.getId());
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
    // @RequiresPermissions("")
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
    public ResponseBean<SummarizeVo> toUpdate(@ApiParam(value = "反馈表id") @RequestParam(name = "id") String id) {
        System.out.println(id);
        SummarizeVo summarizeVos = iSummarizeService.selectVoById(id);
        System.out.println(summarizeVos);
        if (ObjectUtil.isNotNull(summarizeVos)) {
            return ResponseBean.Success(summarizeVos);
        } else {
            return ResponseBean.Fail();
        }
    }

    @ApiOperation(value = "删除")
    // @RequiresPermissions("")
    @DeleteMapping("/remove")
    public ResponseBean remove(@ApiParam(value = "反馈表id") @RequestParam(name = "ids") String[] ids) {
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
    public ResponseBean updateReadst(@ApiParam(value = "ids") @RequestParam(name = "ids") String[] ids) {
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        UpdateWrapper<Summarize> updateWrapper = new UpdateWrapper<Summarize>().in("id", ids).set("readst", 1)
                .set("chetime", time);
        ;
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
    public ResponseBean updateReadst2(@ApiParam(value = "id") @RequestParam(name = "id") String id) {
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
