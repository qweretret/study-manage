package com.ossjk.qlh.study.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ossjk.qlh.study.entity.Tuisongzuoye;
import com.ossjk.qlh.study.mapper.TuisongzuoyeMapper;
import com.ossjk.qlh.study.service.ITuisongzuoyeService;
import com.ossjk.qlh.study.service.vo.Tuisongzuoye2Vo;
import org.springframework.stereotype.Service;

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
public class TuisongzuoyeServiceImpl extends ServiceImpl<TuisongzuoyeMapper, Tuisongzuoye> implements ITuisongzuoyeService {

    @Override
    public Page<Tuisongzuoye2Vo> list(Page page, QueryWrapper<Tuisongzuoye2Vo> ew) {
        return this.baseMapper.list(page,ew);
    }


}
