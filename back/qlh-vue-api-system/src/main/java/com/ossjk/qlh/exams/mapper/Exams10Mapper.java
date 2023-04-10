package com.ossjk.qlh.exams.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ossjk.qlh.exams.entity.Exams;
import com.ossjk.qlh.exams.vo2.Exams10Vo;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface Exams10Mapper extends BaseMapper<Exams> {

    @Select("select exams.*,student.id as stuid,student.name as stuname FROM student inner join exams on student.cid = exams.cid " +
            "where student.id = #{stuid}")
    List<Exams10Vo> listExams(String stuid);
}
