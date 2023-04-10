package com.ossjk.qlh.study.service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ossjk.qlh.study.entity.Summarize;
import com.ossjk.qlh.study.vo.SummarizeVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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

    //小程序-查询指定学生的数据,并查看
    List<SummarizeVo> modifyAndView(String subtime,String id,String sid);

    //日期时间和查询绑在一起
    SummarizeVo dateDisplay(String id,String subtime);

    SummarizeVo selectVoById(String id);

    //学生自我反馈的总数
    List<SummarizeVo> getclazzvaluetion(String cid,String subtime);

    //显示班级全部学生提交情况
    SummarizeVo getcircumstance(String subtime,String cid);

}
