package com.ossjk.qlh.study.mapper;

import java.util.Date;
import java.util.List;

import com.ossjk.qlh.study.service.vo.DictionaryDataVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ossjk.qlh.study.entity.Summarize;
import com.ossjk.qlh.study.service.vo.SummarizeVo;

/**
 * Copyright 2022-06-13 QLH. Tech Ltd. All rights reserved.
 * 
 * @Package: com.ossjk.qlh.xuexi.mapper
 * @ClassName: SummarizeMapper
 * @Description: -Mapper
 * @author: Rick.yang
 * @date: 2022-06-13 18:03:21
 */
public interface SummarizeMapper extends BaseMapper<Summarize> {

    @Select("SELECT * FROM `v_summarize` ${ew.customSqlSegment}")
    Page<SummarizeVo> selectVo(Page page, QueryWrapper<SummarizeVo> ew);

    @Select("SELECT s.`name` sname,t.`name` tname,c.`name` cname,c2.`name` course,`summarize`.* FROM `summarize`,`course` c2,`user` t,`clazz` c,`student` s WHERE c.`cid`=c2.`id` AND c.`id`=s.`cid` AND c.`tid`=t.`id` AND `summarize`.`sid`=s.`id` ${ew.customSqlSegment}")
    List<SummarizeVo> selectVoList(QueryWrapper<SummarizeVo> ew);

    @Select("SELECT s.`name` sname,t.`name`tname,c.`name` cname,c2.`name` course,`summarize`.* FROM `summarize`,"
            + "`course` c2,`user` t,`clazz` c,`student` s WHERE c.`cid`=c2.`id` AND c.`id`=s.`cid` AND c.`tid`=t.`id`  AND `summarize`.`sid`=s.`id` AND summarize.id=#{id}")
    SummarizeVo selectVoById(@Param("id") String id);
    @Select("select `s`.`name` AS `sname`,`t`.`name` AS `tname`,`c`.`name` AS `cname`,`c2`.`name` AS `course`,`summarize`.`id` AS `id`,`summarize`.`sid` AS `sid`,`summarize`.`cid` AS `cid`,`summarize`.`tid` AS `tid`,`summarize`.`courseId` AS `courseId`,`summarize`.`content` AS `content`,`summarize`.`readst` AS `readst`,`summarize`.`subtime` AS `subtime`,`summarize`.`chetime` AS `chetime`," +
            "(SELECT distinct sort FROM `dictionarydata` d WHERE d.dkey = `summarize`.`a1` and d.dkey = d.dvalue) As a1," +
            "(SELECT distinct sort FROM `dictionarydata` d WHERE d.dkey = `summarize`.`a2` and d.dkey = d.dvalue) As a2," +
            "(SELECT distinct sort FROM `dictionarydata` d WHERE d.dkey = `summarize`.`a3` and d.dkey = d.dvalue) As a3," +
            "(SELECT distinct sort FROM `dictionarydata` d WHERE d.dkey = `summarize`.`a4` and d.dkey = d.dvalue) As a4," +
            "(SELECT distinct sort FROM `dictionarydata` d WHERE d.dkey = `summarize`.`a5` and d.dkey = d.dvalue) As a5," +
            "(SELECT distinct sort FROM `dictionarydata` d WHERE d.dkey = `summarize`.`a6` and d.dkey = d.dvalue) As a6," +
            "`summarize`.`crtm` AS `crtm`,`summarize`.`crer` AS `crer`,`summarize`.`mder` AS `mder`,`summarize`.`mdtm` AS `mdtm` from ((((`summarize` `summarize` join `course` `c2`) join `user` `t`) join `clazz` `c`) join `student` `s`)  where ((`c`.`cid` = `c2`.`id`) and (`c`.`id` = `s`.`cid`) and (convert(`c`.`tid` using utf8mb4) = `t`.`id`) and (`summarize`.`sid` = `s`.`id`)" +
            " and if(2>#{readst}, `summarize`.`sid` = #{sid} and readst=#{readst},`summarize`.`sid` = #{sid}))")
    Page<SummarizeVo> selectWxVo(Page page,String sid,String readst);

    @Select("select d1.dkey a1,d2.dkey a2,d3.dkey a3 ,d4.dkey a4 ,d5.dkey a5 ,d6.dkey a6 from `dictionarydata` d1 ,`dictionarydata` d2 ,`dictionarydata` d3 ,`dictionarydata` d4 ,`dictionarydata` d5 ,`dictionarydata` d6 " +
            "WHERE d1.did = '19d834a589898b5d45af39be0ee7a84c' and d1.sort = #{a1} " +
            "and d2.did = '7161619f5ee1e7122d69285131260acd' and d2.sort = #{a2} " +
            "and d3.did = 'fa52f10d19a44c9d71002de3f7f30126' and d3.sort = #{a3} " +
            "and d4.did = 'bb4f0ac9bd74ab9527d1e3322086b8de' and d4.sort = #{a4} " +
            "and d5.did = 'de706b2a0bf2c2c2ea2d232a74aadbcc' and d5.sort = #{a5} " +
            "and d6.did = 'eac58c0897dc10b8c570e8351b94df9c' and d6.sort = #{a6} ")
    DictionaryDataVo dictionarydata(String a1, String a2, String a3, String a4, String a5, String a6);

    @Select("select subtime from summarize WHERE sid = #{sid} and LEFT(subtime,10) = LEFT(#{subtime},10)")
    Summarize subtime(String sid,Date subtime);

}
