package com.ossjk.qlh.study.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ossjk.core.system.dto.UploadDto;
import com.ossjk.core.vo.ResponseBean;
import com.ossjk.qlh.study.entity.Myclass;
import com.ossjk.qlh.study.service.vo.MyclassVo;
import com.ossjk.qlh.system.entity.Student;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Copyright  2022-06-16 QLH. Tech Ltd. All rights reserved.
 * 
 * @Package: com.ossjk.qlh.xuexi.service
 * @ClassName: MyclassService
 * @Description:  -服务类接口
 * @author: Rick.yang
 * @date:  2022-06-16 17:38:14 
 */
public interface IMyclassService extends IService<Myclass> {

    /**
     * 根据班级id获取全部学生
     */
    ResponseBean<List<Student>> getStudentList(String cid);

    /**
     * 根据学生id获取学生信息
     */
    Student getStudent(String sid);

    /**
     * 转换成Base64字符串
     * @param in 图片字节流
     * @return String
     */
    Boolean updatePicture(MultipartFile in, UploadDto uploadDto,String id) throws IOException;

    /**
     * 根据学生id获取班级
     */
    Myclass getOneClass(String sid);
}
