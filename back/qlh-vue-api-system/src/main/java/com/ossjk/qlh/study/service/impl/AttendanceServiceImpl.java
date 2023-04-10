package com.ossjk.qlh.study.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ossjk.core.vo.ResponseBean;
import com.ossjk.myUtil.StringUtil;
import com.ossjk.qlh.study.dto.AttendanceDTO;
import com.ossjk.qlh.study.entity.Attendance;
import com.ossjk.qlh.study.mapper.AttendanceMapper;
import com.ossjk.qlh.study.service.IAttendanceService;
import com.ossjk.qlh.study.vo.NaviVO;
import com.ossjk.qlh.stus.service.IStudentService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Copyright  2022-09-27 QLH. Tech Ltd. All rights reserved.
 * 
 * @Package: com.ossjk.qlh.study.service.impl
 * @ClassName: AttendanceServiceImpl
 * @Description: -服务实现类
 * @author: flame
 * @date:  2022-09-27 16:21:34 
 */
@Service
public class AttendanceServiceImpl extends ServiceImpl<AttendanceMapper, Attendance> implements IAttendanceService {


    //学生
    @Autowired
    private IStudentService studentService;


    @Transactional
    public String importDatasBiz(List<Attendance> records){

        int sucCnt = 0 , failCnt = 0;
        Attendance one = null ;
        Calendar clr=  Calendar.getInstance();
        for(Attendance li : records){
            one = this.baseMapper.findOneByStuidKqdate( li.getSid(),li.getKqdate());
            if(ObjectUtil.isNotEmpty(one)){
                  this.baseMapper.deleteById(one.getId());
            }
            li.setResult("异常");
            //初步判定
            if(li.getChkin()==null || li.getChkout()==null){
                li.setResult("异常");
            }else {
                int inNum =  Integer.parseInt( li.getChkin().replaceAll(":","") ) ;
                int outNum =   Integer.parseInt( li.getChkout().replaceAll(":","") ) ;

                clr.setTime(li.getKqdate());
                int week = clr.get(Calendar.DAY_OF_WEEK);
                if(week ==0){  //星期天
                    week = 7;
                }

                if(   inNum > 930 ){
                    li.setResult("迟到");
                }

                if(  week<5 ){

                    if(  outNum > 1800  ){  //周1-4 此处应该是21点
                        li.setResult("正常");
                    }else  {
                        li.setResult("早退");
                    }
                }else{

                    if(  outNum > 1730  ){  //周5-6-7 此处应该是17:30点
                        li.setResult("正常");
                    }else  {
                        li.setResult("早退");
                    }
                }

            }

            save(li);
            sucCnt++;
        }

        failCnt =   records.size() - sucCnt;
        return  "成功插入"+ failCnt +"条,  失败"+ failCnt+"条";
    }

    public List<AttendanceDTO> parseAttendance(File file){

        InputStream in = null;
        //4.结果
        List<AttendanceDTO> resluts = new ArrayList<>();
        try {
            in = new FileInputStream("D:\\imgs\\attendance\\"+file.getName());

            // 1. 输入流中获取工作簿
            HSSFWorkbook workbook = new HSSFWorkbook(in);
            // 2. 在工作簿中获取目标工作表
            Sheet sheet = workbook.getSheet("考勤记录");
            // 3.读取的循环变量
            Row row = null;
            Row row2 = null;
            Cell cell = null;
            int rowMax = sheet.getLastRowNum();
            int cellStart = 0;
            int colEnd = 0;

            //读取
            row = sheet.getRow(2);
            String tmp[] = row.getCell(2).getStringCellValue().split(" ~ ");
            String bgn = tmp[0];
            String end = tmp[1];

            //月内的日期范围
            Calendar clr = Calendar.getInstance();
            clr.setTime(StringUtil.smt2.parse(bgn));
            cellStart = clr.get(Calendar.DAY_OF_MONTH) - 1;
            clr.setTime(StringUtil.smt2.parse(end));
            colEnd = clr.get(Calendar.DAY_OF_MONTH) - 1;
            // 员工数据
            AttendanceDTO att = null;
            String tmpStr = null;
            String mth = bgn.substring(0,8);

            //学生id
            String sid = null;
            for (int r = 4; r <= rowMax; r += 2) {
                row = sheet.getRow(r);
                //下一个学生 考勤数据
                row2 = sheet.getRow(r+1);
                //取得学生id
                sid = this.studentService.findSidbyClzNm_Snm(row.getCell(10).getStringCellValue(),row.getCell(20).getStringCellValue());
                System.out.print (row.getCell(10).getStringCellValue()+"     "+sid+"      ");
                System.out.println(row.getCell(20).getStringCellValue());
                if(sid == null){
                    //导入失败：数据库没录入学生

                }else{
                  for (int i = cellStart; i <= colEnd; i++) {

                    att = new AttendanceDTO();
                    //暂存name到id、班级到stuid  ，实际开发时通过  name、班级 去数据库换学生id
                    att.setSid(sid);

                    att.setStuname(row.getCell(10).getStringCellValue());
                    att.setClzname(row.getCell(20).getStringCellValue());
                    //设置日期
                    att.setKqdate(  StringUtil.smt2.parse(mth+((i+1)<10?("0"+(i+1)):(i+1)) )  );

                    tmpStr = row2.getCell(i).getStringCellValue();

                    if (tmpStr != null && tmpStr.length() > 0) {
                        if (tmpStr.length() == 5) {
                            //16点以前是来的时间
                            att.setChkin(tmpStr);
                            //16点以后是来的时间
                            //att.setChkout(tmpStr);
                        } else {
                            att.setChkin(tmpStr.substring(0, 5));
                            att.setChkout(tmpStr.substring(tmpStr.length() - 5));
                        }
                        resluts.add(att);
                    }
                  }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                new File("D:\\imgs\\attendance\\" + file.getName()).delete();
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return resluts;
    }

    @Override
    public List<AttendanceDTO> findStuDtoByMth(String mth, String sid   ){
        return this.baseMapper.findStuDtoByMth(mth,   sid );
    }

    @Override
    public List<AttendanceDTO> findStuDtoByClz(  String kqdate,  String cid   ) {
        return this.baseMapper.findStuDtoByClz(kqdate,    cid);
    }

    @Override
    public String maxDateInClazz(  String mth, String cid  ){
        return this.baseMapper.maxDateInClazzByMth( mth,cid);
    }

    @Override
    public String[] oneYearMth(  String bgn,    String end ){
        return this.baseMapper.oneYearMth(bgn,end);
    }


    @Override
    public NaviVO navi(  String kqdate, String cid   ) {
        NaviVO nvo = new NaviVO();
        if(kqdate!=null){
            String mth  = kqdate.substring(0,7);
            nvo.setCur(kqdate);
            String[] datas = this.baseMapper.allDaysInMth(cid,mth);
            nvo.setMthDays( datas );
            nvo.setPer(this.baseMapper.naviNext(cid, mth ,  kqdate));
            nvo.setPer(this.baseMapper.naviPre(cid, mth  ,  kqdate));
        }
       return nvo;
    }
}
