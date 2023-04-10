package com.ossjk.qlh.study.service;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ossjk.qlh.study.entity.Selfevaluation;
import com.ossjk.qlh.study.entity.Summarize;
import com.ossjk.qlh.study.vo.SelfevaluationVo;
import com.ossjk.qlh.stus.entity.Student;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * Copyright 2022-06-14 QLH. Tech Ltd. All rights reserved.
 * 
 * @Package: com.ossjk.qlh.xuexi.service
 * @ClassName: SelfevaluationService
 * @Description: 自我评价表-服务类接口
 * @author: Rick.yang
 * @date: 2022-06-14 17:12:17
 */
public interface ISelfevaluationService extends IService<Selfevaluation> {

    Page<SelfevaluationVo> selectVo(Page page, QueryWrapper<SelfevaluationVo> qw);

    List<SelfevaluationVo> selectVo(QueryWrapper<SelfevaluationVo> qw);

    //小程序-自我评价的时间显示
    SelfevaluationVo getSelfevaluetion(String cid,String subtime);

    //学生每日反馈的总数
    List<SelfevaluationVo> getclazzvaluetion(String subtime,String cid);

    //显示班级全部学生提交情况
    SelfevaluationVo getcircumstance(String subtime,String cid);


    //学生个人自我评价信息
    SelfevaluationVo getByvalue(String subtime,String tid,String sid);

}
