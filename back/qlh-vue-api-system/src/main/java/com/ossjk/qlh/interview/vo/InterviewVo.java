package com.ossjk.qlh.interview.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value = "面试VO")
@Data
public class InterviewVo implements Serializable {

    @ApiModelProperty(value = "学生id")
    private String sid;

    @ApiModelProperty(value = "班级id")
    private String cid;

    @ApiModelProperty(value = "学生名字")
    private String sname;

    /**
     * 学生头像
     */
    @ApiModelProperty(value = "学生头像")
    private String head;

    @ApiModelProperty(value = "面试通过")
    private String offer;

    @ApiModelProperty(value = "面试不通过")
    private String fail;

    @ApiModelProperty(value = "面试总数")
    private String let;

}
