package com.ossjk.qlh.study.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ossjk.core.system.dto.UploadDto;
import com.ossjk.core.vo.ResponseBean;
import com.ossjk.qlh.study.entity.Myclass;
import com.ossjk.qlh.study.mapper.MyclassMapper;
import com.ossjk.qlh.study.service.IMyclassService;
import com.ossjk.qlh.study.service.vo.MyclassVo;
import com.ossjk.qlh.system.entity.Student;
import com.ossjk.qlh.system.mapper.StudentMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.Base64;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Copyright  2022-06-16 QLH. Tech Ltd. All rights reserved.
 * 
 * @Package: com.ossjk.qlh.xuexi.service.impl
 * @ClassName: MyclassServiceImpl
 * @Description: -服务实现类
 * @author: Rick.yang
 * @date:  2022-06-16 17:38:14 
 */
@Service
public class MyclassServiceImpl extends ServiceImpl<MyclassMapper, Myclass> implements IMyclassService {

    @Resource
    private MyclassMapper myclassMapper;

    @Resource
    private StudentMapper studentMapper;

    @Override
    public ResponseBean<List<Student>> getStudentList(String cid) {
        QueryWrapper<Student> studentQueryWrapper = new QueryWrapper<>();
        studentQueryWrapper.eq("cid",cid);
        List<Student> students = studentMapper.selectList(studentQueryWrapper);
        return ResponseBean.Success(students);

    }

    public Student getStudent(String sid) {
        return studentMapper.selectById(sid);
    }

    public Boolean updatePicture(MultipartFile file, UploadDto uploadDto,String id) throws IOException {
        Student student = new Student();
        student.setId(id);
        student.setHead(fileToBase64(file.getInputStream(),uploadDto));
        studentMapper.updateById(student);
        return false;
    }

    /**
     * 转换成Base64字符串
     * @param in 图片字节流
     * @return String
     */
    public static String fileToBase64(InputStream in,UploadDto uploadDto) {
        byte[] data = null;
        try {
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Base64.Encoder encoder = Base64.getEncoder();
        String base64Str = encoder.encodeToString(data);
        return "data:image/jpeg;"+uploadDto.getModel()+","+base64Str;
    }

    /**
     * 根据学生cid获取班级
     */
    public Myclass getOneClass(String sid){
        return myclassMapper.getOneClass(studentMapper.selectById(sid).getCid());
    }
}
