package com.ossjk.qlh.interview.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ossjk.qlh.interview.entity.Interview;
import com.ossjk.qlh.interview.mapper.InterviewMapper;
import com.ossjk.qlh.interview.service.IInterviewService;
import com.ossjk.qlh.interview.vo.InterviewVo;
import com.ossjk.qlh.study.vo.TeacherClz;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Copyright  2022-06-06 QLH. Tech Ltd. All rights reserved.
 * 
 * @Package: com.ossjk.qlh.study3.service.impl
 * @ClassName: MianshiServiceImpl
 * @Description: 面试表-服务实现类
 * @author: Rick.yang
 * @date:  2022-06-06 14:48:32 
 */
@Service
public class InterviewServiceImpl extends ServiceImpl<InterviewMapper, Interview> implements IInterviewService {

    @Override
    public List<TeacherClz> selectClass(String teacherId) {
        return this.baseMapper.selectClass(teacherId);
    }

    @Override
    public List<InterviewVo> selectInterview(String cid) {
        return this.baseMapper.selectInterview(cid);
    }

}
