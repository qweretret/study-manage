package com.ossjk.qlh.exams.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ossjk.qlh.edu.vo.ClazzVO;
import com.ossjk.qlh.exams.entity.Exams;
import com.ossjk.qlh.exams.entity.Question;
import com.ossjk.qlh.exams.vo.ExamsVo;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Copyright  2022-06-14 QLH. Tech Ltd. All rights reserved.
 * 
 * @Package: com.ossjk.qlh.exams.mapper
 * @ClassName: ExamsMapper
 * @Description: 考试表-Mapper
 * @author: 
 * @date:  2022-06-14 11:31:17 
 */
public interface ExamsMapper extends BaseMapper<Exams> {

    @Select("select q.*,c.name as cname,e.name as exampName from exams q left join clazz c on q.cid=c.id " +
            "left join exampaper e on q.expid=e.id ${ew.customSqlSegment}")
    Page<ExamsVo>  selectExamVo(Page page, QueryWrapper<Exams> ew);

}
