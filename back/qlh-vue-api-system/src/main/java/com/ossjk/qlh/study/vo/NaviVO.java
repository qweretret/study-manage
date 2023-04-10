package com.ossjk.qlh.study.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ApiModel("导航vo")
@Data
public class NaviVO implements Serializable {

    @ApiModelProperty(value = "前一天")
    private String per;

    @ApiModelProperty(value = "当前")
    private String cur;

    @ApiModelProperty(value = "下一天")
    private String nxt;
    @ApiModelProperty(value = "备选天")
    private   String[]  mthDays  ;

}
