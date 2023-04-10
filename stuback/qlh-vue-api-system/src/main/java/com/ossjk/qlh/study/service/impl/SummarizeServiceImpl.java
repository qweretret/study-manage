package com.ossjk.qlh.study.service.impl;

import java.util.Date;
import java.util.List;

import com.ossjk.qlh.study.service.vo.DictionaryDataVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ossjk.qlh.study.entity.Summarize;
import com.ossjk.qlh.study.mapper.SummarizeMapper;
import com.ossjk.qlh.study.service.ISummarizeService;
import com.ossjk.qlh.study.service.vo.SummarizeVo;

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

    @Override
    public List<SummarizeVo> selectVoList(QueryWrapper<SummarizeVo> qw) {
        return this.baseMapper.selectVoList(qw);
    }

    @Override
    public SummarizeVo selectVoById(String id) {
        return this.baseMapper.selectVoById(id);
    };

    @Override
    public Page<SummarizeVo> selectWxVo(Page page, String sid,String readst) {
        return this.baseMapper.selectWxVo(page, sid,readst);
    }

    @Override
    public DictionaryDataVo dictionarydata(String a1, String a2, String a3, String a4, String a5, String a6) {
        return this.baseMapper.dictionarydata(a1, a2, a3, a4, a5, a6);
    }
    @Override
    public Summarize subtime(String sid,Date subtime){
        return this.baseMapper.subtime(sid,subtime);
    }

}
