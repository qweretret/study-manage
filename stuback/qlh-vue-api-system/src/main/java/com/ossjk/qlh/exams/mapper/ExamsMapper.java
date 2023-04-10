package com.ossjk.qlh.exams.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ossjk.qlh.exams.entity.Exampaper;
import com.ossjk.qlh.exams.entity.Exams;
import com.ossjk.qlh.exams.vo.ExamsVo;
import com.ossjk.qlh.exams.vo.QuestionVO;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Copyright  2022-07-05 QLH. Tech Ltd. All rights reserved.
 * 
 * @Package: com.ossjk.qlh.exams.mapper
 * @ClassName: ExamsMapper
 * @Description: 考试表-Mapper
 * @author: Rick.wang
 * @date:  2022-07-05 09:57:29 
 */
public interface ExamsMapper extends BaseMapper<Exams> {

    @Select("select exams.*, " +
            "student.id as stuid,student.name as stuname, " +
            "exampaper.name as expname ,exampaper.duration as exduration,exampaper.type as extype,exampaper.score as exscores , " +
            "answers.score as anscore " +
            "FROM student " +
            "inner join exams on student.cid = exams.cid " +
            "inner join exampaper on exams.expid = exampaper.id  " +
            "left join answers on exams.id = answers.kid " +
            "${ew.customSqlSegment}")
    Page<ExamsVo> selectExamVo(Page page, QueryWrapper<Exams> ew);
    @Select("select content from exampaper where id=#{expid};")
    String selectContent(String expid);

    @Select("select content from question where id =#{id};")
    String selectQuestionContent(String id);

    @Select("select * from exampaper where id =#{id};")
    Exampaper selectExampaperById(String id);
}
