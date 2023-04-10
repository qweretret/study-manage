package com.ossjk.qlh.study.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ossjk.qlh.study.entity.Feedback;
import com.ossjk.qlh.study.vo.NaviVO;
import com.ossjk.qlh.study.vo.PersonDataVo;
import com.ossjk.qlh.study.vo.StudentVO;
import com.ossjk.qlh.study.vo.TeacherClz;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.mapstruct.Mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Mapper
public interface FeedMapper extends BaseMapper<Feedback> {

    @Select(" select IFNULL(per,'null') as per,IFNULL(nxt,'null') as nxt from " +
            "   (select min(left(subtime,11)) as nxt  ,1 as cur  from feedback where " +
            "   sid IN(select id from student WHERE cid=#{cid}) and left(subtime,11)>#{cur}) a," +
            "   (select max(left(subtime,11)) as per ,1 as cur from feedback where " +
            "   sid IN(select id from student WHERE cid=#{cid}) and left(subtime,11)<#{cur}) b" +
            "  where a.cur = b.cur ")
    NaviVO selectNavi(@Param("cid")String cid, @Param("cur")String cur);

    @Select("select Count(lname) as let from student WHERE cid=#{cid}")
    Integer selectAllStudent(@Param("cid")String cid);

    @Select("select cid from student WHERE sid=#{sid}")
    String selectcid(@Param("sid")String sid);

    @Select("select Count(distinct(sid)) as yesSubmit,IFNULL(SUM(score>=3),0) as pass,IFNULL(SUM(score<3),0) as nopass from feedback " +
            "WHERE sid IN(select id from student WHERE cid=#{cid}) " +
            "and left(subtime,11)=#{cur}")
    PersonDataVo selectSubmit(@Param("cid")String cid, @Param("cur")String cur);

    @Select("select tid as teacherId,name as teacherClz,id as cid from clazz WHERE tid=#{teacherId}")
    List<TeacherClz> selectClass(@Param("teacherId")String teacherId);

    @Select("select feedback.id,feedback.cid,name,sid,courseid,feedback.crtm,title,score,content from feedback  " +
            "JOIN student on student.id=feedback.sid " +
            "where sid=#{sid} and left(subtime,11)=#{cur} ")
    List<Feedback> selectAll(@Param("sid")String sid, @Param("cur")String cur);

    @Update("UPDATE feedback SET score=#{score},readst=1 WHERE id=#{id}")
    Integer upDataFeed(@Param("id")String id, @Param("score")String score);

    @Select("SELECT id,name from student WHERE cid=#{cid}")
    List<Map<String,String>> selectStudent(@Param("cid")String cid);

    @Select("SELECT count(content)as num " +
            "from feedback WHERE sid=#{sid} and left(subtime,11)=#{cur}")
    String selectNum(@Param("sid")String sid,@Param("cur")String cur);

}
