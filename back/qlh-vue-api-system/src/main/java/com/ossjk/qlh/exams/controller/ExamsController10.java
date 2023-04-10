package com.ossjk.qlh.exams.controller;


import com.ossjk.core.vo.ResponseBean;
import com.ossjk.qlh.exams.entity.Question;
import com.ossjk.qlh.exams.service.IExams10Service;
import com.ossjk.qlh.exams.vo2.Exams10Vo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "考试试卷")
@RestController
@RequestMapping("/exams/exams10")
public class    ExamsController10 {

    @Autowired
    private IExams10Service iExams10Service;

    @ApiOperation(value = "查询试卷")
    //@RequiresPermissions("")
    @GetMapping("/listExams")
    public ResponseBean<List<Exams10Vo>> listExams(@ApiParam(value = "stuid") @RequestParam(name = "stuid") String stuid) {
        List<Exams10Vo> exams10 = iExams10Service.listExams(stuid);
//        if (ObjectUtil.isNotNull(exams)) {
//            return ResponseBean.Success(exams);
//        } else {
//            return ResponseBean.Fail();
//        }
        return ResponseBean.Success(exams10);
    }

}
