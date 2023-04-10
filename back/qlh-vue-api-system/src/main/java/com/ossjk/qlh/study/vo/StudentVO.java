package com.ossjk.qlh.study.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value = "学生Vo")
@Data
public class StudentVO implements Serializable {


    /**
     * id
     */
    @ApiModelProperty(value = "学生id", required = true)
    @TableId("id")
    private String id;

    /**
     * 姓名
     */
    @ApiModelProperty(value = "学生名字")
    //@TableField("name")
    private String value;

    /**
     * 数据条数
     */
    @ApiModelProperty(value = "总结条数")
    private String num;

    /**
     * 提交时间
     */
    @ApiModelProperty(value = "提交日期")
    private String time;

}
