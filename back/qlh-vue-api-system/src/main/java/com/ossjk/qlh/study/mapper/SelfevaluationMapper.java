package com.ossjk.qlh.study.mapper;

import java.time.LocalDate;
import java.util.List;

import com.ossjk.qlh.study.entity.Summarize;
import com.ossjk.qlh.stus.entity.Student;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ossjk.qlh.study.entity.Selfevaluation;
import com.ossjk.qlh.study.vo.SelfevaluationVo;

/**
 * Copyright 2022-06-14 QLH. Tech Ltd. All rights reserved.
 * 
 * @Package: com.ossjk.qlh.xuexi.mapper
 * @ClassName: SelfevaluationMapper
 * @Description: 自我评价表-Mapper
 * @author: Rick.yang
 * @date: 2022-06-14 17:12:17
 */
public interface SelfevaluationMapper extends BaseMapper<Selfevaluation> {

    @Select("SELECT * from v_selfevaluation ${ew.customSqlSegment}")
    Page<SelfevaluationVo> selectVo(Page page, QueryWrapper<SelfevaluationVo> ew);

    @Select("SELECT * from v_selfevaluation ${ew.customSqlSegment}")
    List<SelfevaluationVo> selectVo(QueryWrapper<SelfevaluationVo> ew);


    @Select("select distinct sid from selfevaluation where cid=#{cid} and date(subtime)=#{date};")
    List<Selfevaluation> selectSubmitStudent(LocalDate date, String cid);

    //小程序-自我评价的时间显示
    @Select("(select ifnull(theDayBefore,'null') as theDayBefore,ifnull(theDayAfterThat,'null') as theDayAfterThat from (select min(left(a.subtime,10)) as theDayAfterThat," +
            "1 as cur from selfevaluation a where cid = #{cid} and left(a.subtime,10)>left(#{subtime},10)) a ," +
            "(select max(left(a.subtime,10)) as theDayBefore,1 as cur from  selfevaluation a where " +
            "cid = #{cid} and left(a.subtime,10)<left(#{subtime},10)) b where  a.cur = b.cur)")
    SelfevaluationVo getSelfevaluetion(@Param("cid") String cid, @Param("subtime")String subtime);

    //学生自我评价的总数
    @Select("select a.id as sid,a.name as sname,b.subtime as subtime,b.readtime from student a LEFT JOIN selfevaluation b on " +
            "a.cid = #{cid} and left(b.subtime,10) = left(#{subtime},10) and  a.id = b.sid where " +
            "a.cid = #{cid}")
    List<SelfevaluationVo> getclazzvaluetion(@Param("subtime")String subtime,@Param("cid")String cid);

    //显示班级全部学生提交情况
    @Select("select count(b.subtime) as submitted,count(a.id) as totalnumberofpeople " +
            "from student a " +
            "LEFT JOIN selfevaluation b on " +
            "a.cid = #{cid} and " +
            "left(b.subtime,10) = left(#{subtime},10) and  " +
            "a.id = b.sid where a.cid = #{cid}")
    SelfevaluationVo getcircumstance(@Param("subtime")String subtime,@Param("cid")String cid);

    //学生个人自我评价信息
    @Select("select b.name as sname,a.* from selfevaluation a,student b where left(a.subtime,10) = " +
            "left(#{subtime},10) and a.tid = #{tid} and a.sid = #{sid} and a.sid = b.id")
    SelfevaluationVo getByvalue(@Param("subtime")String subtime,@Param("tid")String tid,@Param("sid")String sid);

}
