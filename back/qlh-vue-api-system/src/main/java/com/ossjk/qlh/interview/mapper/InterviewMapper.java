package com.ossjk.qlh.interview.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ossjk.qlh.interview.entity.Interview;
import com.ossjk.qlh.interview.vo.InterviewVo;
import com.ossjk.qlh.study.vo.TeacherClz;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Copyright  2022-06-06 QLH. Tech Ltd. All rights reserved.
 * 
 * @Package: com.ossjk.qlh.study3.mapper
 * @ClassName: MianshiMapper
 * @Description: 面试表-Mapper
 * @author: Rick.yang
 * @date:  2022-06-06 14:48:32 
 */
public interface InterviewMapper extends BaseMapper<Interview> {

    @Select("select tid as teacherId,name as teacherClz,id as cid from clazz WHERE tid=#{teacherId}")
    List<TeacherClz> selectClass(@Param("teacherId")String teacherId);

    @Select("SELECT stuid as sid,cid,head,name as sname,IFNULL(SUM(msresult='offer'),0) as offer " +
            ",IFNULL(SUM(msresult='fail'),0) as fail,COUNT(stuid) as let " +
            "from student JOIN interview ON student.id=interview.stuid  " +
            "WHERE cid=#{cid} GROUP BY stuid")
    List<InterviewVo> selectInterview(@Param("cid")String cid);


}
