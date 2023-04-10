package com.ossjk.qlh.study.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ossjk.qlh.study.entity.Myclass;
import com.ossjk.qlh.study.service.vo.FeedbackVo;
import com.ossjk.qlh.study.service.vo.MyclassVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Copyright 2022-06-16 QLH. Tech Ltd. All rights reserved.
 * 
 * @Package: com.ossjk.qlh.xuexi.mapper
 * @ClassName: MyclassMapper
 * @Description: -Mapper
 * @author: Rick.yang
 * @date: 2022-06-16 17:38:14
 */
public interface MyclassMapper extends BaseMapper<Myclass> {

    @Select("select v.* FROM v_clazz v WHERE v.id= #{cid}")
    Myclass getOneClass(String cid);

}
