package com.ossjk.qlh.edu.service;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ossjk.qlh.edu.entity.Clazz;
import com.ossjk.qlh.edu.vo.ClazzVO;

/**
 * Copyright 2022-06-08 QLH. Tech Ltd. All rights reserved.
 * 
 * @Package: com.ossjk.qlh.edu.service
 * @ClassName: ClazzService
 * @Description: 班级表-服务类接口
 * @author: Rick.yang
 * @date: 2022-06-08 19:34:03
 */
public interface IClazzService extends IService<Clazz> {

    Page<ClazzVO> pageVO(Page<ClazzVO> page, QueryWrapper<Clazz> ew);

    List<ClazzVO> ListVO(QueryWrapper<ClazzVO> ew);

    Page<ClazzVO> pageVOByTchrID(Page<ClazzVO> page, String tchrId, QueryWrapper<Clazz> ew);

    //查询一年范围内的班级
    List<Clazz> listOneYearClz(  Date bgnDay,Date endDay  );
}
