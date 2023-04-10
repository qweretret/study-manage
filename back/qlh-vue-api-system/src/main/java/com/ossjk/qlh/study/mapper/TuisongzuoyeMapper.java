package com.ossjk.qlh.study.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ossjk.qlh.study.entity.Tuisongzuoye;
import com.ossjk.qlh.study.vo.ClassVO;
import com.ossjk.qlh.study.vo.StudentVO;
import com.ossjk.qlh.study.vo.TeacherVO;
import com.ossjk.qlh.study.vo.Tuisongzuoye2Vo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import java.util.List;

/**
 * Copyright  2022-08-05 QLH. Tech Ltd. All rights reserved.
 * 
 * @Package: com.ossjk.qlh.xueli.mapper
 * @ClassName: TuisongzuoyeMapper
 * @Description: -Mapper
 * @author: Rick.yang
 * @date:  2022-08-05 14:40:29 
 */
public interface TuisongzuoyeMapper extends BaseMapper<Tuisongzuoye> {

    //查询（主）
    @Select("SELECT a.id as classid,a.`name` as classname,c.name as `sname`,d.name as `tname`,b.*" +
            "FROM clazz a,tuisongzuoye b,student c,user d ${ew.customSqlSegment} and a.id = c.cid and " +
            "b.sid = c.id and b.sid = c.id and b.tid = d.id order by crtm desc ")
    Page<Tuisongzuoye2Vo> list(Page page, QueryWrapper<Tuisongzuoye2Vo> ew);

    //小程序查询
    @Select("select z.*,x.`nop`, case y.`tnouq` is null when true then 0 else y.`tnouq` end as `tnouq`," +
            "case s.`signed` is null when true then 0 else s.`signed` end as `signed`," +
            "case d.`notsigned` is null when true then 0 else d.`notsigned` end as `notsigned` " +
            "from (SELECT b.sid,c.`name` as sname,count(b.sid) as `aq` " +
            "FROM clazz a,tuisongzuoye b,student c,user d where a.id = c.cid and " +
            "b.sid = c.id and b.sid = c.id and b.tid = d.id and b.tid = #{id} and a.id = #{cid} GROUP  BY b.sid,c.name) z " +
            "left join (SELECT b.sid,count(b.sid) as `nop` " +
            "FROM clazz a,tuisongzuoye b,student c,user d where a.id = c.cid and " +
            "b.sid = c.id and b.sid = c.id and b.tid = d.id and b.tid = #{id} and a.id = #{cid} and (b.chetime is not null) GROUP BY b.sid,c.name) x on z.sid = x.sid " +
            "left join (SELECT b.sid,count(b.sid ) as `tnouq` " +
            "FROM clazz a,tuisongzuoye b,student c,user d where a.id = c.cid and " +
            "b.sid = c.id and b.sid = c.id and b.tid = d.id and b.tid = #{id} and a.id = #{cid} and (b.chetime is null) GROUP BY b.sid,c.name) y on z.sid = y.sid " +
            "left join (SELECT b.sid,count(b.sid) as `signed` " +
            "FROM clazz a,tuisongzuoye b,student c,user d where a.id = c.cid and " +
            "b.sid = c.id and b.sid = c.id and b.tid = d.id and b.tid = #{id} and a.id = #{cid} and (b.chetime is not null) and  (b.subtime is not null) " +
            "GROUP BY b.sid,c.name) s on x.sid = s.sid " +
            "left join (SELECT b.sid,count(b.sid) as `notsigned` " +
            "FROM clazz a,tuisongzuoye b,student c,user d where a.id = c.cid and " +
            "b.sid = c.id and b.sid = c.id and b.tid = d.id and b.tid = #{id} and a.id = #{cid} and (b.chetime is not null) and  (b.subtime is null) " +
            "GROUP BY b.sid,c.name) d on x.sid = d.sid")
    List<Tuisongzuoye2Vo> list2(@Param("id") String id,@Param("cid") String cid);

    //小程序查询学生个人
    @Select("SELECT a.id as classid,a.`name` as classname,c.name as `sname`,d.name as `tname`,b.* " +
            "FROM clazz a,tuisongzuoye b,student c,user d ${ew.customSqlSegment} and a.id = c.cid and " +
            "b.sid = c.id and b.sid = c.id and b.tid = d.id order by crtm desc ")
    Page<Tuisongzuoye2Vo> list3(Page page,@Param("ew") QueryWrapper<Tuisongzuoye2Vo> ew);

    //编辑
    @Select("SELECT a.id as classid,a.`name` as classname,c.name as `sname`,d.name as `tname`,b.*" +
            "FROM clazz a,tuisongzuoye b,student c,user d where a.id = c.cid and " +
            "b.sid = c.id and b.sid = c.id and b.tid = d.id and b.id = #{id}")
    Tuisongzuoye2Vo getByIdTs(@Param("id") String id);

    //班级信息-含小程序
    @Select("select  id,name as value from clazz where tid=#{id}")
    List<ClassVO> interactionCsn(@Param("id") String id);

    //学生信息
    @Select("select a.id,a.name as value from student a,clazz b where a.cid = b.id and b.id = #{id}")
    List<StudentVO> interactionStuid(@Param("id") String id);

    //老师信息
    @Select("select  a.id,a.name as value from user a,clazz b where a.id = b.tid and b.id = #{id}")
    List<TeacherVO> interactionTcr(@Param("id") String id);

    //查询指定推送的学生ID里的openid
    @Select("select wxappopenid,name as sname from student where id = #{id}")
    Tuisongzuoye2Vo getOpenidStu(@Param("id") String id);

    //查询推送的信息并结合学生的openid
    @Select("SELECT d.name as `tname`,b.* " +
            "FROM clazz a,tuisongzuoye b,student c,user d where a.id = c.cid and " +
            "b.sid = c.id and b.sid = c.id and b.tid = d.id and b.id = #{id}")
    Tuisongzuoye2Vo getOneStu(@Param("id") String id);

    //查询指定推送的学生ID里的name
    @Select("select name from student where id = #{id}")
    String selectStuName(@Param("id") String id);

    //头像
    @Select("select head from user where id = #{id}")
    String gethead(@Param("id")String id);

    //推送到学生小程序上显示---只查老师名字
    @Select("select name from user where id = #{id}")
    String gettname(@Param("id") String id);
}
