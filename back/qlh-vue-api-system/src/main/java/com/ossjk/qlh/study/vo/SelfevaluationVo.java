package com.ossjk.qlh.study.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.ossjk.qlh.study.entity.Selfevaluation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "自我评价VO")
@Data
public class SelfevaluationVo extends Selfevaluation {

    @ApiModelProperty(value = "学生名字")
    @TableField("sname")
    private String sname;
    /**
     * 班级名字
     */
    @ApiModelProperty(value = "班级名字")
    @TableField("cname")
    private String cname;
    /**
     * 老师
     */
    @ApiModelProperty(value = "老师")
    @TableField("tname")
    private String tname;

    /*
   当前选择时间
    */
    @ApiModelProperty(value = "当前选择时间")
    @TableField("theDayBefore")
    private String theCurrentTime;

    /*
    小程序每日反馈的时间选择-前一天时间
     */
    @ApiModelProperty(value = "前一天时间")
    @TableField("theDayBefore")
    private String theDayBefore;

    /*
    小程序每日反馈的时间选择-后一天时间
     */
    @ApiModelProperty(value = "后一天时间")
    @TableField("theDayAfterThat")
    private String theDayAfterThat;

    /**
     * 班级学生总数
     */
    @ApiModelProperty(value = "班级学生总数")
    private String totalnumberofpeople;

    /**
     * 学生已提交总数
     */
    @ApiModelProperty(value = "学生已提交总数")
    private String submitted;

}
