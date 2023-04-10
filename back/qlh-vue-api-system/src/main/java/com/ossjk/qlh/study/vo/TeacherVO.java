package com.ossjk.qlh.study.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value = "老师Vo")
@Data
public class TeacherVO implements Serializable {


    /**
     * id
     */
    @ApiModelProperty(value = "", required = true)
    @TableId("id")
    private String id;
    /**
     * 姓名
     */
    @ApiModelProperty(value = "name")
    @TableField("name")
    private String value;

}
