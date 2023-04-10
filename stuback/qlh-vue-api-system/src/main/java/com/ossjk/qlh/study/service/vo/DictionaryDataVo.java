package com.ossjk.qlh.study.service.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "自我反馈映射Vo")
@Data
public class DictionaryDataVo {
    /**
     * 反馈1
     */
    @ApiModelProperty(value = "反馈1")
    private String a1;

    /**
     * 反馈2
     */
    @ApiModelProperty(value = "反馈2")
    private String a2;

    /**
     * 反馈3
     */
    @ApiModelProperty(value = "反馈3")
    private String a3;

    /**
     * 反馈4
     */
    @ApiModelProperty(value = "反馈4")
    private String a4;

    /**
     * 反馈5
     */
    @ApiModelProperty(value = "反馈5")
    private String a5;

    /**
     * 反馈6
     */
    @ApiModelProperty(value = "反馈6")
    private String a6;
}
