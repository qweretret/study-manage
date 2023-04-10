package com.ossjk.qlh.study.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ossjk.qlh.edu.entity.Clazz;
import com.ossjk.qlh.study.dto.AttendanceDTO;
import com.ossjk.qlh.study.entity.Attendance;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Date;
import java.util.List;

/**
 * Copyright  2022-09-27 QLH. Tech Ltd. All rights reserved.
 * 
 * @Package: com.ossjk.qlh.study.mapper
 * @ClassName: AttendanceMapper
 * @Description: -Mapper
 * @author: flame
 * @date:  2022-09-27 16:21:34 
 */
public interface AttendanceMapper extends BaseMapper<Attendance> {

    @Select("select a.*,s.`name` as stuname,c.name as clzname from attendance a  " +
            "  left join student  s on a.stuid = s.id left join clazz   c  on s.cid = c.id   " +
            "  where left(a.kqdate,7) =#{mth} and s.id=#{sid}  order by kqdate desc ")
    List<AttendanceDTO> findStuDtoByMth(@Param("mth") String mth, @Param("sid")String sid   );

    @Select(" select a.*,s.name from attendance a  left join student s on s.id =a.stuid  " +
            " where s.cid =#{cid} and a.kqdate=#{kqdate} order by s.id ")
    List<AttendanceDTO> findStuDtoByClz(@Param("kqdate") String kqdate, @Param("cid")String cid   );

    @Select("select * from attendance  where stuid =#{stuid} and kqdate=#{kqdate} ")
    Attendance findOneByStuidKqdate(@Param("stuid")  String stuid,@Param("kqdate")  Date kqdate );


    ///------>  导航
    @Select( "  select max(a.kqdate) from attendance a  left join student s on s.id = a.stuid " +
            " where left(a.kqdate,7) = #{mth} and s.cid =#{cid} ")
    String maxDateInClazzByMth( @Param("mth") String mth,@Param("cid") String cid  );


    @Select( " select distinct a.kqdate from attendance a  left join student s on s.id = a.stuid " +
                  " where s.cid =#{cid} and  left( a.kqdate,7 ) = #{mth}  order by a.kqdate desc ")
    String[] allDaysInMth( @Param("cid") String cid,@Param("mth") String mth );

    @Select("select min(x.kqdate) from " +
            "   (select distinct a.kqdate from attendance a  left join student s on s.id =a.stuid" +
            " where   s.cid =#{cid} and  left( a.kqdate,7 ) = #{mth}  order by a.kqdate desc) x where x.kqdate> #{kqdate} ")
    String naviNext( @Param("cid") String cid,@Param("mth") String mth, @Param("kqdate")String kqdate );

    @Select("select max(x.kqdate) from" +
            "  (select distinct a.kqdate from attendance a  left join student s on s.id =a.stuid " +
            " where   s.cid =#{cid} and  left( a.kqdate,7 ) = #{mth}  order by a.kqdate desc) x where x.kqdate< #{kqdate} ")
    String naviPre( @Param("cid") String cid,@Param("mth") String mth, @Param("kqdate")String kqdate );

    @Select( " select a.kqdate from ( select distinct left(kqdate,7) kqdate from attendance  where kqdate between #{bgn} and #{end}  ) a order by a.kqdate desc ")
    String[] oneYearMth( @Param("bgn") String bgn,@Param("end")  String end );

    /// 导航   <-----

}
