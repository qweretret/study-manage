package com.ossjk.qlh.stus.controller;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.util.ObjectUtil;
import com.ossjk.config.mvc.ResourceMappersProperties;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ossjk.core.base.controller.BaseController;
import com.ossjk.core.system.api.ISystemCommonApi;
import com.ossjk.core.system.dto.LoginUser;
import com.ossjk.core.vo.ResponseBean;
import com.ossjk.qlh.edu.controller.ClazzController;
import com.ossjk.qlh.edu.entity.Clazz;
import com.ossjk.qlh.edu.service.IClazzService;
import com.ossjk.qlh.stus.entity.Student;
import com.ossjk.qlh.stus.service.IStudentService;
import com.ossjk.qlh.stus.vo.StudentVO;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * Copyright 2022-06-08 QLH. Tech Ltd. All rights reserved.
 *
 * @Package: com.ossjk.qlh.stus.controller
 * @ClassName: StudentController
 * @Description: -控制器
 * @author: Rick.yang
 * @date: 2022-06-08 16:32:38
 */
@Api(tags = "学生信息")
@RestController
@RequestMapping("/stus/student")
public class StudentController extends BaseController {

    @Autowired
    private IStudentService iStudentService;

    @Autowired
    private IClazzService iClazzService;

    @Autowired
    private ISystemCommonApi iSystemCommonApi;

    @Autowired
    private ISystemCommonApi systemCommonApi;
    
    @Autowired
    private ResourceMappersProperties ymlUri;

    @ApiOperation(value = "列表")
    @GetMapping("/list")
    public ResponseBean<Page<Student>> list(Page page, String name, String state, HttpServletRequest request) {
        // LoginUser loginUser = systemCommonApi.getLoginUser(getToekn(request));
        QueryWrapper<Student> queryWrapper = new QueryWrapper<Student>();
        QueryWrapper<Clazz> queryWrapper2 = new QueryWrapper<Clazz>();
        LoginUser me = iSystemCommonApi.refreshTokenCacheLoginUser(this.getToekn(request));

        String roleStr = me.getRoleCode().iterator().next();
        Page<Student> student = null;
        // 教学总监 / 班主任
        if ("onlinestu".equals(roleStr) || "tom".equals(roleStr) || "colstu".equals(roleStr)) {
            queryWrapper.eq("name", me.getName());
        } else if ("tea".equals(roleStr)) {
            queryWrapper2.eq("tid", me.getId());
            List<Clazz> list = iClazzService.list(queryWrapper2);
            List arrayList = new ArrayList();
            for (Clazz clazz : list) {
                arrayList.add(clazz.getId());
            }
            queryWrapper.in("cid", arrayList.toArray());
        } else {
        }
        student = iStudentService.page(page, queryWrapper);
        queryWrapper.orderByDesc("comeTime", "schoolTime");
        if (StrUtil.isNotBlank(name)) {
            queryWrapper.like("name", "%" + name + "%");
        }
        if (StrUtil.isNotBlank(state)) {
            queryWrapper.like("state", "%" + state + "%");
        }
        return ResponseBean.Success(iStudentService.page(page, queryWrapper));
    }

    @ApiOperation(value = "列表")
    @GetMapping("/getStulist")
    public ResponseBean<List<Student>> list3(String name) {
        List<Student> name1 = iStudentService
                .list(new QueryWrapper<Student>().select("name").like("name", "%" + name + "%"));
        return ResponseBean.Success(name1);
    }

    @ApiOperation(value = "导出学生excel表格")
    @RequestMapping("/OutModelExcel")
    public ResponseBean modelExcel(@ApiParam(value = "fn") @RequestParam(name = "fn") String fn,
            HttpServletResponse response) {
        List<Student> studentList = iStudentService.list();
        // 导出操作
        ExportParams ex = new ExportParams(null, "学生数据列表");
        Workbook wb = ExcelExportUtil.exportExcel(ex, Student.class, studentList);
        response.setHeader("content-Type", "application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment;filename=" + fn);
        response.setCharacterEncoding("UTF-8");
        try {
            OutputStream out = response.getOutputStream();
            wb.write(out);
            out.flush();
            out.close();
            wb.close();
            return ResponseBean.Success();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseBean.Fail();

    }

    @ApiOperation(value = "导出全部快速查分")
    // 点击下载excel 表格
    @RequestMapping(value = "/exportScore", method = RequestMethod.GET)
    public void downAll(HttpServletResponse response) throws Exception {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("信息表");
        List<Student> classmateList = iStudentService.list();
        // 设置要导出的文件的名字
        String fileName = "学生信息表" + ".xls";
        // 新增数据行，并且设置单元格数据
        int rowNum = 1;
        String[] headers = { "姓名", "地址", "学校名称", "所学专业", "学生手机号", "登录名" };
        // headers表示excel表中第一行的表头
        HSSFRow row = sheet.createRow(0);
        // 在excel表中添加表头
        for (int i = 0; i < headers.length; i++) {
            HSSFCell cell = row.createCell(i);
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text);
        }
        // 在表中存放查询到的数据放入对应的列
        for (Student table : classmateList) {
            HSSFRow row1 = sheet.createRow(rowNum);
            rowNum = ClazzController.getRowNum(rowNum, table, row1);
        }
        response.setContentType("application/octet-stream");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        response.flushBuffer();
        workbook.write(response.getOutputStream());
    }

    @ApiOperation(value = "导出全部快速查分")
    // 点击下载excel 表格
    @RequestMapping(value = "/exportScore2", method = RequestMethod.GET)
    public void downAll2(@ApiParam(value = "ids") @RequestParam(name = "ids") String[] ids,
            HttpServletResponse response) throws Exception {
        System.out.println(ids);
        QueryWrapper<Student> studentQueryWrapper = new QueryWrapper<Student>().in("cid", ids);
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("信息表");
        List<Student> classmateList = iStudentService.list(studentQueryWrapper);
        // 设置要导出的文件的名字
        String fileName = "学生信息表" + ".xls";
        // 新增数据行，并且设置单元格数据
        int rowNum = 1;
        String[] headers = { "姓名", "地址", "学校名称", "所学专业", "学生手机号", "登录名" };
        // headers表示excel表中第一行的表头
        HSSFRow row = sheet.createRow(0);
        // 在excel表中添加表头
        for (int i = 0; i < headers.length; i++) {
            HSSFCell cell = row.createCell(i);
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text);
        }
        // 在表中存放查询到的数据放入对应的列
        for (Student table : classmateList) {
            HSSFRow row1 = sheet.createRow(rowNum);
            rowNum = ClazzController.getRowNum(rowNum, table, row1);
        }
        response.setContentType("application/octet-stream");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        response.flushBuffer();
        workbook.write(response.getOutputStream());
    }

    // try {
    // byte[] buff = new byte[1024];
    // OutputStream out = response.getOutputStream();
    // workbook.write(out);
    // workbook.close();
    // } catch (IOException e) {
    // e.printStackTrace();
    // }

    @ApiOperation(value = "感应查询学生")
    @GetMapping("/intnStu")
    public ResponseBean<List<StudentVO>> list2(
            @ApiParam(value = "name") @RequestParam(name = "name", required = false) String name) {
        return ResponseBean.Success(iStudentService.interactionStu(name));
    }

    @ApiOperation(value = "添加")
    @PostMapping("/save")
    public ResponseBean save(@RequestBody Student record) {
        if (iStudentService.save(record)) {
            return ResponseBean.Success();
        } else {
            return ResponseBean.Fail();
        }
    }

    @ApiOperation(value = "去编辑")
    @GetMapping("/toUpdate") // @RequestParam(name = "id")
    public ResponseBean<Map<String, Object>> toUpdate(@ApiParam(value = "id") String id) {
        Student student = new Student();
        if (StrUtil.isNotBlank(id))
            student = iStudentService.getById(id);
        Map<String, Object> map = new HashMap<>();
        QueryWrapper<Clazz> queryWrapper = new QueryWrapper<Clazz>().select("DISTINCT id,`name`");
        List<Clazz> clazzList = iClazzService.list(queryWrapper);
        map.put("clazzList", clazzList);
        map.put("student", student);
        return ResponseBean.Success(map);
    }

    @ApiOperation(value = "修改")
    @GetMapping("/revise")
    public ResponseBean revise(@ApiParam(value = "id") @RequestParam(name = "id") String id) {

        if (iStudentService.revise(id) == 1) {
            return ResponseBean.Success();
        } else {
            return ResponseBean.Fail();
        }
    }

    @ApiOperation(value = "编辑")
    @PutMapping("/update")
    public ResponseBean update(@RequestBody Student record) {
        Student id = iStudentService.getById(record.getId());

        if (!id.getCid().equals(record.getCid())) {
            QueryWrapper<Clazz> queryWrapper = new QueryWrapper<Clazz>().in("id", record.getCid(), id.getCid())
                    .select("name");
            // iClazzService.
        }
        if (iStudentService.updateById(record)) {
            return ResponseBean.Success();
        } else {
            return ResponseBean.Fail();
        }
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("/remove")
    public ResponseBean remove(@ApiParam(value = "ids") @RequestParam(name = "ids") String[] ids) {
        if (iStudentService.removeByIds(Arrays.asList(ids))) {
            return ResponseBean.Success();
        } else {
            return ResponseBean.Fail();
        }
    }

    @ApiOperation(value = "查询该班级学生name和ID")
    @GetMapping("/toStus")
    public ResponseBean<List<Student>> toStus(@ApiParam(value = "班级id" ,required = true) @RequestParam(name = "id") String id) {
        return ResponseBean.Success(iStudentService.lookupClazzStu(id));
    }

    @ApiOperation(value = "查询该班级学生name")
    @GetMapping("/stuInClass")
    public ResponseBean<List<String>>findClazzStuName(String cid){
        return ResponseBean.Success(iStudentService.findClazzStuName(cid));

    }

    @ApiOperation(value = "修改全班学生默认密码")
    @RequiresPermissions("003000150025")
//	@RequestMapping("/updStuPwd")
    @GetMapping("/updStuPwd")
    public ResponseBean updPwd(@ApiParam(value = "班级默认密码") @RequestParam(name = "deftpwd") String deftpwd,
                               @ApiParam(value = "班级id") @RequestParam(name = "id") String id) {
        if (iStudentService.updStuPwd(deftpwd,id) > 0) {
            return ResponseBean.Success();
        } else {
            return ResponseBean.Fail();
        }
    }


    @ApiOperation(value = "去确认导入")
    // @RequiresPermissions("")
    @GetMapping("/toImport")
    public ResponseBean<List<Student>> toImport(@ApiParam(value = "fnm") @RequestParam(name = "fnm") String fnm) {
        // 读取yuml的配置数据
        List<ResourceMappersProperties.ResourceMapper> resourceMapperList = ymlUri.getMappers();
        Map<String, String> uriMap = resourceMapperList.stream().collect(Collectors.toMap(
                ResourceMappersProperties.ResourceMapper::getUri, ResourceMappersProperties.ResourceMapper::getFile));

        List<Student> students = iStudentService.parseStudent(new File(uriMap.get("/statics/excels"), fnm));
        if (ObjectUtil.isNotNull(students) && students.size() > 0) {
            return ResponseBean.Success(students);
        } else {
            return ResponseBean.Fail();
        }

    }

    @ApiOperation(value = "完成导入")
    // @RequiresPermissions("")
    @PutMapping("/imports")
    public ResponseBean importDatas(@RequestBody List<Student> records) {
        // name -> cid
        QueryWrapper<Clazz> queryWrapper = new QueryWrapper<Clazz>();
        Clazz clz = null;
        queryWrapper.eq("name", records.get(0).getCid());
        clz = iClazzService.getOne(queryWrapper);
        if (ObjectUtil.isNull(clz)) {
            return ResponseBean.Fail("添加学生班级未创建222");
        }
        for (Student stu : records) {
            if (StrUtil.isBlank(stu.getCid())) {
                continue;
            }
            stu.setCid(clz.getId());
        }

        Boolean flag = iStudentService.saveBatch(records);
        if (flag) {
            return ResponseBean.Success();
        } else {
            return ResponseBean.Fail();
        }

    }

    @ApiOperation(value = "查询班级学生")
    @GetMapping("/toStu")
    public ResponseBean<List<Student>> list4(String cid) {
        QueryWrapper<Student> queryWrapper = new QueryWrapper<Student>();
        return ResponseBean.Success(iStudentService.lookupClazzStu(cid));
    }

    public List<Student> lookupClazzStu(String cid){
        QueryWrapper<Student> queryWrapper = new QueryWrapper<Student>();
        return iStudentService.lookupClazzStu(cid);
    }

}
