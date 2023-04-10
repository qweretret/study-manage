package com.ossjk.qlh.study.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ossjk.qlh.study.entity.Tuisongzuoye;
import com.ossjk.qlh.study.service.vo.Tuisongzuoye2Vo;

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

}
