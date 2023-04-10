package com.ossjk.qlh.study.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ossjk.config.mvc.ResourceMappersProperties;
import com.ossjk.core.base.controller.BaseController;
import com.ossjk.core.vo.ResponseBean;
import com.ossjk.myUtil.StringUtil;
import com.ossjk.qlh.edu.entity.Clazz;
import com.ossjk.qlh.edu.service.IClazzService;
import com.ossjk.qlh.study.dto.AttendanceDTO;
import com.ossjk.qlh.study.entity.Attendance;
import com.ossjk.qlh.study.service.IAttendanceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Copyright  2022-09-27 QLH. Tech Ltd. All rights reserved.
 *
 * @Package: com.ossjk.qlh.study.controller
 * @ClassName: AttendanceController
 * @Description: -控制器
 * @author: flame
 * @date: 2022-09-27 16:21:34
 */
@Api(tags = "学生考勤")
@RestController
@RequestMapping("/study/attendance")
public class AttendanceController extends BaseController {

    @Autowired
    private IAttendanceService iAttendanceService;
    @Autowired
    private IClazzService iClazzService;
    @Autowired
    private ResourceMappersProperties ymlUri;

    @ApiOperation(value = "按学生查询考勤列表")
//	@RequiresPermissions("")
    @GetMapping("/list")
    public ResponseBean<Map> list( @ApiParam(value = "学生id",required = true) @RequestParam(name = "sid", required = true) String sid,
                                   @ApiParam(value = "月份") @RequestParam(name = "mth", required = false) String mth) {
         //返回data
         Map  datas = new HashMap();

         if(StrUtil.isBlank(mth)  ){
             //默认本月   更好的逻辑是数据库中该班最大的月份
             mth = StringUtil.smt3.format(new Date());
         }

        datas.put("data",this.iAttendanceService.findStuDtoByMth(mth,sid));

         //导航的月份
        //datas.put("naviMth",this.iAttendanceService.findStuDtoByMth(mth,sid));

        return ResponseBean.Success(datas);
    }

    @ApiOperation(value = "按开班时间列出每年培训[type=3] 和 一年内有考勤的月份")
    // @RequiresPermissions("")
    @GetMapping("/list3")
    public ResponseBean<Map> list3(@ApiParam(value = "preYear") @RequestParam(name = "preYear",required = false) Integer preYear ) {

        Map map = new HashMap();
        //当前年
        Calendar calr = Calendar.getInstance();
        Date end,bgn;
        if (ObjectUtil.isEmpty(preYear)   ) {
            end =  calr.getTime();
            calr.add(Calendar.DAY_OF_MONTH, -365 );
            bgn =  calr.getTime();
        }else{
            calr.add(Calendar.DAY_OF_MONTH,preYear*(-365));
            end =  calr.getTime();
            preYear++;
            calr.add(Calendar.DAY_OF_MONTH,preYear*(-365));
            bgn =  calr.getTime();
        }
        map.put("clzs",iClazzService.listOneYearClz(bgn,end));

        map.put("mths",iAttendanceService.oneYearMth(StringUtil.smt2.format(bgn),StringUtil.smt2.format(end)));

        return ResponseBean.Success(map);
    }

    @ApiOperation(value = "按班级查询考勤列表")
//	@RequiresPermissions("")
    @GetMapping("/list2")
    public ResponseBean<Map> list2( @ApiParam(value = "班级id",required = true) @RequestParam(name = "cid", required = true) String cid,
                                    @ApiParam(value = "日期") @RequestParam(name = "kqdate", required = false) String kqdate,
                                    @ApiParam(value = "月份") @RequestParam(name = "mth", required = true) String mth) {
        //返回data
        Map  datas = new HashMap();

         //默认当天   更好的逻辑是数据库中该班最大的月份
         kqdate =  this.iAttendanceService.maxDateInClazz(mth,cid);
         if(kqdate == null){
             datas.put("data",null);
             datas.put("naviMth",null);
             return ResponseBean.Success(datas);
         }

        datas.put("data",this.iAttendanceService.findStuDtoByClz(kqdate,cid));

        //导航的月份
        datas.put("naviMth",this.iAttendanceService.navi(kqdate,cid));

        return ResponseBean.Success(datas);
    }

    @ApiOperation(value = "添加")
//	@RequiresPermissions("")
    @PostMapping("/save")
    public ResponseBean save(@RequestBody Attendance record) {
        if (iAttendanceService.save(record)) {
            return ResponseBean.Success();
        } else {
            return ResponseBean.Fail();
        }
    }

    @ApiOperation(value = "去编辑")
//	@RequiresPermissions("")
    @GetMapping("/toUpdate")
    public ResponseBean<Attendance> toUpdate(@ApiParam(value = "id") @RequestParam(name = "id") String id) {
        Attendance attendance = iAttendanceService.getById(id);
        if (ObjectUtil.isNotNull(attendance)) {
            return ResponseBean.Success(attendance);
        } else {
            return ResponseBean.Fail();
        }
    }


    @ApiOperation(value = "编辑")
   // @RequiresPermissions("")
    @PutMapping("/update")
    public ResponseBean update(@RequestBody Attendance record) {
        record.setCfmdate(     new Date());
        if (iAttendanceService.updateById(record)) {
            return ResponseBean.Success();
        } else {
            return ResponseBean.Fail();
        }
    }

    @ApiOperation(value = "删除")
  //  @RequiresPermissions("")
    @DeleteMapping("/remove")
    public ResponseBean remove(@ApiParam(value = "ids") @RequestParam(name = "ids") String[] ids) {
        if (iAttendanceService.removeByIds(Arrays.asList(ids))) {
            return ResponseBean.Success();
        } else {
            return ResponseBean.Fail();
        }
    }

    @ApiOperation(value = "解析数据")
    @GetMapping("/toParse")
    public ResponseBean<List<AttendanceDTO>> toParse(@ApiParam(value = "fname") @RequestParam(name = "fname") String fname) {
        //取得上传地址
        List<ResourceMappersProperties.ResourceMapper> resourceMapperList = ymlUri.getMappers();
        Map<String, String> uriMap = resourceMapperList.stream().collect(Collectors.toMap(ResourceMappersProperties.ResourceMapper::getUri, ResourceMappersProperties.ResourceMapper::getFile));
        //解析给前端回显
        List<AttendanceDTO> eqs = iAttendanceService.parseAttendance(new File(uriMap.get("/statics/attendance"), fname));
        System.out.println(eqs);
        if (!eqs.isEmpty() && eqs.size() > 0) {
            return ResponseBean.Success(eqs);
        } else {
            return ResponseBean.Fail();
        }

    }

    @ApiOperation(value = "完成导入")
    //@RequiresPermissions("")
    @PostMapping("/toComfirm2")
    @Transactional
    public ResponseBean importDatas(@RequestBody List<Attendance> records) {
        return ResponseBean.Success( iAttendanceService.importDatasBiz(records)  );
    }
}
