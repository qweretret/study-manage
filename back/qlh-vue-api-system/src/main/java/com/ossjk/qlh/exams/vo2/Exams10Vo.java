package com.ossjk.qlh.exams.vo2;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.ossjk.qlh.exams.entity.Exams;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "考试试卷")
@Data
public class Exams10Vo extends Exams {
    /**
     * 学生id
     */
    @ApiModelProperty(value = "学生id")
    @TableId("stuid")
    private String stuid;

    /**
     * 学生姓名
     */
    @ApiModelProperty(value = "学生姓名")
    @TableField("stuname")
    private String stuname;
}
