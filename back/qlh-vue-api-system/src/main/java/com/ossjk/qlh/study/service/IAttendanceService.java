package com.ossjk.qlh.study.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ossjk.qlh.study.dto.AttendanceDTO;
import com.ossjk.qlh.study.entity.Attendance;
import com.ossjk.qlh.study.vo.NaviVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * Copyright  2022-09-27 QLH. Tech Ltd. All rights reserved.
 * 
 * @Package: com.ossjk.qlh.study.service
 * @ClassName: AttendanceService
 * @Description:  -服务类接口
 * @author: flame
 * @date:  2022-09-27 16:21:34 
 */
public interface IAttendanceService extends IService<Attendance> {

    List<AttendanceDTO> parseAttendance(File file);

    List<AttendanceDTO> findStuDtoByMth(String mth, String sid   );

    String importDatasBiz(List<Attendance> records);

    List<AttendanceDTO> findStuDtoByClz(  String kqdate,  String cid   );

    String maxDateInClazz(  String mth,String cid  );

    NaviVO  navi( String kqdate, String cid   );

    String[] oneYearMth(  String bgn,    String end );
}
