package com.ossjk.qlh.study.mapper;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import com.ossjk.qlh.study.vo.SelfevaluationVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ossjk.qlh.study.entity.Summarize;
import com.ossjk.qlh.study.vo.SummarizeVo;

/**
 * Copyright 2022-06-13 QLH. Tech Ltd. All rights reserved.
 * 
 * @Package: com.ossjk.qlh.xuexi.mapper
 * @ClassName: FeedMapper
 * @Description: -Mapper
 * @author: Rick.yang
 * @date: 2022-06-13 18:03:21
 */
public interface SummarizeMapper extends BaseMapper<Summarize> {
    //
    // @Select("SELECT su.*,s.name as sname,c.name as cname,t.name as tname,co.name
    // as course from summarize su "
    // + "left join teacher t on su.tid=t.id left join student s on su.sid=s.id "
    // + "left join clazz c on su.cid=c.id left join course co on su.courseId=co.id
    // ${ew.customSqlSegment}")
    @Select("SELECT * FROM `v_summarize` ${ew.customSqlSegment}")
    Page<SummarizeVo> selectVo(Page page, QueryWrapper<SummarizeVo> ew);

    @Select("SELECT s.`name` sname,t.`name` tname,c.`name` cname,c2.`name` course,`summarize`.* FROM `summarize`,`course` c2,`user` t,`clazz` c,`student` s WHERE c.`cid`=c2.`id` AND c.`id`=s.`cid` AND c.`tid`=t.`id` AND `summarize`.`sid`=s.`id` ${ew.customSqlSegment}")
    List<SummarizeVo> selectVoList(QueryWrapper<SummarizeVo> ew);

    @Select("SELECT s.`name` sname,t.`name`tname,c.`name` cname,c2.`name` course,`summarize`.* FROM `summarize`,"
            + "`course` c2,`user` t,`clazz` c,`student` s WHERE c.`cid`=c2.`id` AND c.`id`=s.`cid` AND c.`tid`=t.`id`  AND `summarize`.`sid`=s.`id` AND summarize.id=#{id}")
    SummarizeVo selectVoById(@Param("id") String id);

    @Select("select distinct sid from summarize where cid=#{cid} and date(subtime)=#{date};")
    List<Summarize> selectSubmitStudent(LocalDate date,String cid);

    //小程序-查询指定学生的数据,并查看
    @Select("select a.id,a.sid,a.sname,a.cname,a.course,a.content,a.readst,a.subtime," +
            "a.chetime,a.a1,a.a2,a.a3,a.a4,a.a5,a.a6 from v_summarize a,student b,clazz c " +
            "where a.sid = b.id and c.id = b.cid and left(subtime,10) = left(#{subtime},10) and c.id = #{id} and a.sid = #{sid}")
    List<SummarizeVo> modifyAndView(@Param("subtime")String subtime,@Param("id")String id,@Param("sid")String sid);

    //日期时间和查询绑在一起
    @Select("(select ifnull(b.theDayBefore,'null') as theDayBefore,ifnull(a.theDayAfterThat,'null') as theDayAfterThat from (select min(left(a.subtime,10)) as theDayAfterThat ,1 as cur  from summarize a,student b,clazz c where " +
            "a.sid = b.id and c.id = b.cid and c.id= #{id} and left(a.subtime,10)>left(#{subtime},10)) a," +
            "(select max(left(a.subtime,10)) as theDayBefore ,1 as cur from summarize a,student b,clazz c where " +
            "a.sid = b.id and c.id = b.cid and c.id= #{id} and left(a.subtime,10)<left(#{subtime},10)) b " +
            "where a.cur = b.cur)")
    SummarizeVo dateDisplay(@Param("id")String id,@Param("subtime")String subtime);

    //学生自我反馈的总数
    @Select("select a.id as sid,a.name as sname,b.subtime,b.chetime from student a LEFT JOIN summarize b on " +
            "a.cid = #{cid} and left(b.subtime,10) = left(#{subtime},10) and  a.id = b.sid where a.cid = #{cid}")
    List<SummarizeVo> getclazzvaluetion(@Param("cid")String id,@Param("subtime")String subtime);

    //显示班级全部学生提交情况
    @Select("select count(a.id) as totalnumberofpeople,count(b.subtime) as submitted " +
            "from student a " +
            "LEFT JOIN summarize b on " +
            "a.cid = #{cid} and " +
            "left(b.subtime,10) = left(#{subtime},10) and  " +
            "a.id = b.sid where a.cid = #{cid}")
    SummarizeVo getcircumstance(@Param("subtime")String subtime, @Param("cid")String cid);

    @Select("select subtime from summarize WHERE sid = #{sid} and LEFT(subtime,10) = LEFT(#{subtime},10)")
    Summarize subtime(@Param("sid") String sid,@Param("subtime") Date subtime);
}
