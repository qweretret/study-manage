package com.ossjk.qlh.exams.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ossjk.qlh.exams.entity.Exams;
import com.ossjk.qlh.exams.mapper.Exams10Mapper;
import com.ossjk.qlh.exams.mapper.ExamsMapper;
import com.ossjk.qlh.exams.service.IExams10Service;
import com.ossjk.qlh.exams.vo2.Exams10Vo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class Exams10ServiceImpl  extends ServiceImpl<ExamsMapper, Exams> implements IExams10Service {


    @Resource
    private Exams10Mapper exams10Mapper;

    @Override
    public List<Exams10Vo> listExams(String stuid) {
        List<Exams10Vo> exams10 =exams10Mapper.listExams(stuid);
        List<Exams10Vo> examsFilter = exams10.stream().filter(e -> e.getSid().contains(e.getStuname())).collect(Collectors.toList());
        return examsFilter;
    }
}
