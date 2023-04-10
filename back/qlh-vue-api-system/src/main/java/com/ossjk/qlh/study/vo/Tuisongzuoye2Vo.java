package com.ossjk.qlh.study.vo;
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

    //全部数量
    @ApiModelProperty(value = "全部数量")
    private String aq;

    //已推送数量
    @ApiModelProperty(value = "已推送数量")
    private String nop;

    //未推送数量
    @ApiModelProperty(value = "未推送数量")
    private String tnouq;

    //已签收数量
    @ApiModelProperty(value = "已签收数量")
    private String signed;

    //未签收数量
    @ApiModelProperty(value = "未签收数量")
    private String notsigned;

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

    /**
     * 要推送的学生openid
     */
    @ApiModelProperty(value = "要推送的学生openid")
    private String wxappopenid;

}
