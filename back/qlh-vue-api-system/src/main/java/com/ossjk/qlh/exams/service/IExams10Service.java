package com.ossjk.qlh.exams.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ossjk.qlh.exams.entity.Exams;
import com.ossjk.qlh.exams.vo2.Exams10Vo;

import java.util.List;

public interface IExams10Service extends IService<Exams> {
    List<Exams10Vo> listExams(String stuid);
}
