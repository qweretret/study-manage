package com.ossjk.qlh.study.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel("导航vo")
@Data
public class PersonDataVo implements Serializable {

    @ApiModelProperty(value = "班级人数")
    private Integer allPerson;

    @ApiModelProperty(value = "已提交")
    private Integer yesSubmit;

    @ApiModelProperty(value = "及格")
    private Integer pass;

    @ApiModelProperty(value = "不及格")
    private Integer noPass;

}
