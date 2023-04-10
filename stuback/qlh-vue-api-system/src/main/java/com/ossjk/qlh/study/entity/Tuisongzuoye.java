package com.ossjk.qlh.study.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.ossjk.core.base.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * Copyright  2022-08-05 QLH. Tech Ltd. All rights reserved.
 * 
 * @Package: com.ossjk.qlh.xueli.entity
 * @ClassName: Tuisongzuoye
 * @Description: -实体类
 * @author: Rick.yang
 * @date:  2022-08-05 14:40:29 
 */
@ApiModel(value = "推送接收作业表")
@Data
public class Tuisongzuoye extends BaseEntity<Tuisongzuoye> {

	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@ApiModelProperty(value = "推送id", required = true)
	@TableId("id")
	private String id;

	/**
	 * 学生id
	 */
	@ApiModelProperty(value = "学生id")
	@TableField("sid")
	private String sid;

	/**
	 * 老师id
	 */
	@ApiModelProperty(value = "老师id")
	@TableField("tid")
	private String tid;

	/**
	 * 作业类型
	 */
	@ApiModelProperty(value = "作业类型")
	@TableField("thejobtype")
	private String thejobtype;

	/**
	 * 内容
	 */
	@ApiModelProperty(value = "内容")
	@TableField("content")
	private String content;

	/**
	 * 学生读取时间
	 */
	@ApiModelProperty(value = "学生读取时间")
	@TableField("subtime")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	@JSONField(format = "yyyy-MM-dd HH:mm")
	private Date subtime;

	/**
	 * 老师推送时间
	 */
	@ApiModelProperty(value = "老师推送时间")
	@TableField("chetime")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	@JSONField(format = "yyyy-MM-dd HH:mm")
	private Date chetime;

	/**
	 * 是否推送
	 */
	@ApiModelProperty(value = "是否推送")
	@TableField("whethertopush")
	private Integer whethertopush;

	/**
	 * 作业创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	@TableField("crtm")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	@JSONField(format = "yyyy-MM-dd HH:mm")
	private Date crtm;
	 
	@Override
	protected Serializable pkVal() {
			return id;
	}
 

	 
}
