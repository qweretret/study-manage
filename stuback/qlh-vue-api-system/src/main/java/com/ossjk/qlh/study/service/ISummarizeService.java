package com.ossjk.qlh.study.service;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ossjk.qlh.study.entity.Summarize;
import com.ossjk.qlh.study.service.vo.DictionaryDataVo;
import com.ossjk.qlh.study.service.vo.SummarizeVo;

/**
 * Copyright 2022-06-13 QLH. Tech Ltd. All rights reserved.
 * 
 * @Package: com.ossjk.qlh.xuexi.service
 * @ClassName: SummarizeService
 * @Description: -服务类接口
 * @author: Rick.yang
 * @date: 2022-06-13 18:03:21
 */
public interface ISummarizeService extends IService<Summarize> {

    Page<SummarizeVo> selectVo(Page page, QueryWrapper<SummarizeVo> qw);

    List<SummarizeVo> selectVoList(QueryWrapper<SummarizeVo> qw);

    SummarizeVo selectVoById(String id);
    Page<SummarizeVo> selectWxVo(Page page, String sid,String readst);

    DictionaryDataVo dictionarydata(String a1, String a2, String a3, String a4, String a5, String a6);
    Summarize subtime(String sid, Date subtime);
}
