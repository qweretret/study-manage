package com.ossjk.qlh.study.service.vo;

import com.ossjk.qlh.study.entity.Tuisongzuoye;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value = "全体表Vo")
@Data
public class Tuisongzuoye2Vo extends Tuisongzuoye implements Serializable {


    //班级ID
    private String classid;

    //班级名
    private String classname;

    /**
     * 学生姓名
     */
    @ApiModelProperty(value = "学生姓名")
    private String sname;

    /**
     * 老师姓名
     */
    @ApiModelProperty(value = "老师姓名")
    private String tname;

}
