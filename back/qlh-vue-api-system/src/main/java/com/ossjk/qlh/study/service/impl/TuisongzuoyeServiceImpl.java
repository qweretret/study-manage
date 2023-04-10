package com.ossjk.qlh.study.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.ossjk.qlh.study.entity.Tuisongzuoye;
import com.ossjk.qlh.study.mapper.TuisongzuoyeMapper;
import com.ossjk.qlh.study.service.ITuisongzuoyeService;
import com.ossjk.qlh.study.vo.ClassVO;
import com.ossjk.qlh.study.vo.StudentVO;

import com.ossjk.qlh.study.vo.TeacherVO;
import com.ossjk.qlh.study.vo.Tuisongzuoye2Vo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.List;

/**
 * Copyright  2022-08-05 QLH. Tech Ltd. All rights reserved.
 * 
 * @Package: com.ossjk.qlh.lky.service.impl
 * @ClassName: TuisongzuoyeServiceImpl
 * @Description: -服务实现类
 * @author: Rick.yang
 * @date:  2022-08-05 14:40:29 
 */
@Service
@EnableTransactionManagement
public class TuisongzuoyeServiceImpl extends ServiceImpl<TuisongzuoyeMapper, Tuisongzuoye> implements ITuisongzuoyeService {

    @Override
    public Page<Tuisongzuoye2Vo> list(Page page, QueryWrapper<Tuisongzuoye2Vo> ew) {
        return this.baseMapper.list(page,ew);
    }

    @Override
    public List<Tuisongzuoye2Vo> list2(String id,String cid) {
        return this.baseMapper.list2(id,cid);
    }

    @Override
    public Page<Tuisongzuoye2Vo> list3(Page page,QueryWrapper<Tuisongzuoye2Vo> ew) {
        return this.baseMapper.list3(page,ew);
    }

    @Override
    public Tuisongzuoye getByIdTs(String id) {
        return this.baseMapper.getByIdTs(id);
    }

    @Override
    public List<ClassVO> interactionCsn(String id) {
        return this.baseMapper.interactionCsn(id);
    }

    @Override
    public List<StudentVO> interactionStuid(String id) {
        return this.baseMapper.interactionStuid(id);
    }

    @Override
    public List<TeacherVO> interactionTcr(String id) {
        return this.baseMapper.interactionTcr(id);
    }

    //查询指定推送的学生ID里的openid
    public Tuisongzuoye2Vo getOpenidStu(String id){
        return this.baseMapper.getOpenidStu(id);
    }
    //查询推送的信息并结合学生的openid
    public Tuisongzuoye2Vo getOneStu(String id){
        return this.baseMapper.getOneStu(id);
    }

    public String selectStuName(String id){return this.baseMapper.selectStuName(id);};

    @Override
    public String gethead(String id) {
        return this.baseMapper.gethead(id);
    }

    //推送到学生小程序上显示---只查老师名字
    @Override
    public String gettname(String id) {
        return this.baseMapper.gettname(id);
    }
}
