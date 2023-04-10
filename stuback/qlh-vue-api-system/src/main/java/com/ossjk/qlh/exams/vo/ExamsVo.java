package com.ossjk.qlh.exams.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.ossjk.qlh.exams.entity.Exams;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("/试卷vo")
@Data
public class ExamsVo extends Exams {

    @ApiModelProperty(value = "考试id")
    @TableId("id")
    private String id;

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

    @ApiModelProperty(value = "试卷类型")
    @TableField("extype")
    private String extype;

    @ApiModelProperty(value = "试卷名称")
    @TableField("exname")
    private String exname;


    @ApiModelProperty(value = "考试时长")
    @TableField("exduration")
    private int exduration;


    @ApiModelProperty(value = "个人得分")
    @TableField("exscore")
    private String exscores;


    @ApiModelProperty(value = "试卷总分")
    @TableField("anscore")
    private String anscore;


    private int state;
    private int lookState;

}
