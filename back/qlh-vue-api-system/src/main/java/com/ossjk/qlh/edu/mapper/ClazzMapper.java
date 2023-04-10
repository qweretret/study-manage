package com.ossjk.qlh.edu.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ossjk.qlh.edu.entity.Clazz;
import com.ossjk.qlh.edu.vo.ClazzVO;

/**
 * Copyright 2022-06-08 QLH. Tech Ltd. All rights reserved.
 * 
 * @Package: com.ossjk.qlh.edu.mapper
 * @ClassName: ClazzMapper
 * @Description: 班级表-Mapper
 * @author: Rick.yang
 * @date: 2022-06-08 19:34:03
 */
public interface ClazzMapper extends BaseMapper<Clazz> {

    @Select(" select * from v_clazz ${ew.customSqlSegment} ")
    Page<ClazzVO> pageVO(Page<ClazzVO> page, QueryWrapper<Clazz> ew);

    @Select(" select * from v_clazz ${ew.customSqlSegment} ")
    List<ClazzVO> getList(QueryWrapper<ClazzVO> ew);

    @Select(" select distinct c.id,c.name  from clazz c,student s " +
            "   where c.types ='培训班' and c.id = s.cid and c.crtm between #{bgn} and #{end}  order by c.crtm desc ")
    List<Clazz> listClzByYear(@Param("bgn") Date bgn,@Param("end")  Date end );
}
