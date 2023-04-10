package com.ossjk.qlh.study.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ossjk.qlh.study.entity.Feedback;
import com.ossjk.qlh.study.vo.NaviVO;
import com.ossjk.qlh.study.vo.PersonDataVo;
import com.ossjk.qlh.study.vo.StudentVO;
import com.ossjk.qlh.study.vo.TeacherClz;

import java.util.List;

public interface IFeedService extends IService<Feedback> {

    NaviVO selectNavi(String cid, String cur);

    PersonDataVo showData(String cid, String cur);

    List<TeacherClz> selectClass(String teacherId);

    List<Feedback> selectAll(String sid, String cur);

    Integer upDataFeed(String id, String score);

    List<StudentVO> selectStudent(String cid, String cur);
}
