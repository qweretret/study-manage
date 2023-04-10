package com.ossjk.qlh.study.service.impl;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ossjk.qlh.study.entity.Summarize;
import com.ossjk.qlh.study.mapper.SummarizeMapper;
import com.ossjk.qlh.study.service.ISummarizeService;
import com.ossjk.qlh.study.vo.SummarizeVo;

/**
 * Copyright 2022-06-13 QLH. Tech Ltd. All rights reserved.
 * 
 * @Package: com.ossjk.qlh.xuexi.service.impl
 * @ClassName: SummarizeServiceImpl
 * @Description: -服务实现类
 * @author: Rick.yang
 * @date: 2022-06-13 18:03:21
 */
@Service
public class SummarizeServiceImpl extends ServiceImpl<SummarizeMapper, Summarize> implements ISummarizeService {

    @Override
    public Page<SummarizeVo> selectVo(Page page, QueryWrapper<SummarizeVo> qw) {
        return this.baseMapper.selectVo(page, qw);
    }

    //小程序-查询指定学生的数据,并查看
    @Override
    public List<SummarizeVo> modifyAndView(String subtime, String id,String sid) {
        return this.baseMapper.modifyAndView(subtime,id,sid);
    }

    @Override
    public SummarizeVo dateDisplay(String id, String subtime) {
        return this.baseMapper.dateDisplay(id,subtime);
    }

    @Override
    public List<SummarizeVo> selectVoList(QueryWrapper<SummarizeVo> qw) {
        return this.baseMapper.selectVoList(qw);
    }

    @Override
    public SummarizeVo selectVoById(String id) {
        return this.baseMapper.selectVoById(id);
    }

    //学生自我反馈的总数
    @Override
    public List<SummarizeVo> getclazzvaluetion(String id, String subtime) {
        return this.baseMapper.getclazzvaluetion(id,subtime);
    }

    //显示班级全部学生提交情况
    @Override
    public SummarizeVo getcircumstance(String subtime, String cid) {
        return this.baseMapper.getcircumstance(subtime,cid);
    }

}
