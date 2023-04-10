package com.ossjk.qlh.study.service.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.ossjk.qlh.study.entity.Summarize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("课程Vo")
@Data
public class CourseVo extends Summarize {

    @ApiModelProperty(value = "课程ID")
    private String id;

    @ApiModelProperty(value = "小-课程名称")
    private String name;
}
