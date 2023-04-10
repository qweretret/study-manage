package com.ossjk.qlh.stus.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ossjk.qlh.stus.entity.Student;
import com.ossjk.qlh.stus.mapper.StudentMapper;
import com.ossjk.qlh.stus.service.IStudentService;
import com.ossjk.qlh.stus.vo.StudentVO;

/**
 * Copyright 2022-06-08 QLH. Tech Ltd. All rights reserved.
 * 
 * @Package: com.ossjk.qlh.stus.service.impl
 * @ClassName: StudentServiceImpl
 * @Description: -服务实现类
 * @author: Rick.yang
 * @date: 2022-06-08 16:32:38
 */
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements IStudentService {

    @Override
    public List<StudentVO> interactionStu(String name) {
        return this.baseMapper.interactionStu(name + "%");
    }

    @Override
    public int revise(String id) {
        return this.baseMapper.revise(id);
    }

    @Override
    public List<Student> istuscx(String name) {
        return this.baseMapper.istuscx("%" + name + "%");
    }



    @Override
    public int updStuPwd(String deftpwd, String id) {
        return this.baseMapper.updStuPwd(deftpwd, id);
    }

    @Override
    public List<String> findClazzStuName(String cid){return this.baseMapper.findClazzStuName(cid); }

    @Override
    public List<Student> lookupClazzStu(String cid) {
        return this.baseMapper.listClazzStuName(cid);
    }

    @Override
    public List<Student> parseStudent(File fname) {
        // 解析excel文件
        try {
            FileInputStream in = new FileInputStream(fname);
            List<Student> list = ExcelImportUtil.importExcel(in, Student.class, new ImportParams());
            in.close();
            fname.delete();

            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public  String findSidbyClzNm_Snm(  String snm, String cnm){
        return this.baseMapper.findSidbyClzNm_Snm(cnm,snm);
    }

}
