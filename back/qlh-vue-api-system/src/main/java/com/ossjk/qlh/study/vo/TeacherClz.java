package com.ossjk.qlh.study.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel("导航vo")
@Data
public class TeacherClz implements Serializable {

    @ApiModelProperty(value = "老师id")
    private String teacherId;

    @ApiModelProperty(value = "班级id")
    private String cid;

    @ApiModelProperty(value = "老师管理班级")
    private String teacherClz;
}
