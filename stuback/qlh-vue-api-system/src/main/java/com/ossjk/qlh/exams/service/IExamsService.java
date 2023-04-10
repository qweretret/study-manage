package com.ossjk.qlh.exams.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ossjk.qlh.exams.entity.Exampaper;
import com.ossjk.qlh.exams.entity.Exams;
import com.ossjk.qlh.exams.vo.ExamsVo;

/**
 * Copyright  2022-07-05 QLH. Tech Ltd. All rights reserved.
 * 
 * @Package: com.ossjk.qlh.exams.service
 * @ClassName: ExamsService
 * @Description:  考试表-服务类接口
 * @author: Rick.wang
 * @date:  2022-07-05 09:57:29 
 */
public interface IExamsService extends IService<Exams> {

    Page<ExamsVo> selectExamVo(Page page, QueryWrapper<Exams> queryWrapper);
    String selectContent(String expid);

    String selectQuestionContent(String id);

    Exampaper selectExampaperById(String id);


}
