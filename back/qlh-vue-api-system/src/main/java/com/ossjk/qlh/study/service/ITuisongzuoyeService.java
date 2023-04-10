package com.ossjk.qlh.study.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ossjk.qlh.study.entity.Tuisongzuoye;
import com.ossjk.qlh.study.vo.ClassVO;
import com.ossjk.qlh.study.vo.StudentVO;
import com.ossjk.qlh.study.vo.TeacherVO;
import com.ossjk.qlh.study.vo.Tuisongzuoye2Vo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Copyright  2022-08-05 QLH. Tech Ltd. All rights reserved.
 * 
 * @Package: com.ossjk.qlh.xueli.service
 * @ClassName: TuisongzuoyeService
 * @Description:  -服务类接口
 * @author: Rick.yang
 * @date:  2022-08-05 14:40:29 
 */
public interface ITuisongzuoyeService extends IService<Tuisongzuoye> {

    Page<Tuisongzuoye2Vo> list(Page page, QueryWrapper<Tuisongzuoye2Vo> ew);

    List<Tuisongzuoye2Vo> list2(String id,String cid);

    Page<Tuisongzuoye2Vo> list3(Page page,QueryWrapper<Tuisongzuoye2Vo> ew);

    Tuisongzuoye getByIdTs(String id);

    List<ClassVO> interactionCsn(String id);

    List<StudentVO> interactionStuid(@Param("id") String id);

    List<TeacherVO> interactionTcr(String id);

    //查询指定推送的学生ID里的openid
    Tuisongzuoye2Vo getOpenidStu(String id);

    //查询推送的信息并结合学生的openid
    Tuisongzuoye2Vo getOneStu(String id);

    String selectStuName(String id);

    //头像获取
    String gethead(String id);

    //推送到学生小程序上显示---只查老师名字
    String gettname(String id);

}
