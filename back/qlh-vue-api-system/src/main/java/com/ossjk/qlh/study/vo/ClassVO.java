package com.ossjk.qlh.study.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value = "班级Vo")
@Data
public class ClassVO implements Serializable {


    /**
     * 班级id
     */
    @ApiModelProperty(value = "", required = true)
    @TableId("id")
    private String id;

    /**
     * 班级名
     */
    @ApiModelProperty(value = "name")
    @TableField("name")
    private String value;

}
