package com.ossjk.qlh.study.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ossjk.qlh.study.entity.Tuisongzuoye;
import com.ossjk.qlh.study.service.vo.Tuisongzuoye2Vo;
import org.apache.ibatis.annotations.Select;

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
    @Select("SELECT a.`name` as classname,c.name as `sname`,d.name as `tname`,b.id,b.thejobtype,b.content,b.subtime,b.chetime" +
            " FROM clazz a,tuisongzuoye b,student c,user d ${ew.customSqlSegment} and a.id = c.cid and" +
            " b.sid = c.id and b.sid = c.id and b.tid = d.id and whethertopush != 0 order by (b.subtime is null) desc,b.chetime desc")
    Page<Tuisongzuoye2Vo> list(Page page, QueryWrapper<Tuisongzuoye2Vo> ew);

}
