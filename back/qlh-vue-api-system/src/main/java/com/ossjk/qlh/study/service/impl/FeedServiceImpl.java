package com.ossjk.qlh.study.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ossjk.qlh.study.entity.Feedback;
import com.ossjk.qlh.study.mapper.FeedMapper;
import com.ossjk.qlh.study.service.IFeedService;
import com.ossjk.qlh.study.vo.NaviVO;
import com.ossjk.qlh.study.vo.PersonDataVo;
import com.ossjk.qlh.study.vo.StudentVO;
import com.ossjk.qlh.study.vo.TeacherClz;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class FeedServiceImpl extends ServiceImpl<FeedMapper, Feedback> implements IFeedService {

    @Override
    public NaviVO selectNavi(String cid, String cur){return this.baseMapper.selectNavi(cid,cur);}

    @Override
    public PersonDataVo showData(String cid, String cur) {
        PersonDataVo personDataVo;
        if(this.baseMapper.selectSubmit(cid,cur) != null){
            personDataVo = this.baseMapper.selectSubmit(cid,cur);
            personDataVo.setAllPerson(this.baseMapper.selectAllStudent(cid));
        }else {
            personDataVo = new PersonDataVo();
            personDataVo.setAllPerson(this.baseMapper.selectAllStudent(cid));
        }

        return personDataVo;
    }

    @Override
    public List<TeacherClz> selectClass(String teacherId) {
        return this.baseMapper.selectClass(teacherId);
    }

    @Override
    public List<Feedback> selectAll(String sid, String cur) {
        return this.baseMapper.selectAll(sid,cur);
    }

    @Override
    public Integer upDataFeed(String id, String score){
        return this.baseMapper.upDataFeed(id,score);
    }

    @Override
    public List<StudentVO> selectStudent(String cid, String cur) {
        List<StudentVO> list = new ArrayList<>();
        for (Map<String,String> e:this.baseMapper.selectStudent(cid)) {
            StudentVO studentVO = new StudentVO();
            studentVO.setId(e.get("id"));
            studentVO.setValue(e.get("name"));
            studentVO.setNum(this.baseMapper.selectNum(e.get("id"),cur));
            studentVO.setTime(cur);
            list.add(studentVO);
        }
        return list;
    }
}
