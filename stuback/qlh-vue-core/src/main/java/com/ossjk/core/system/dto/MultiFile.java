package com.ossjk.core.system.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel(value = "微信上传Dto")
@Data
public class MultiFile {

    /**
     * base64 字符串
     * 接收多个
     */
    private String[] model;

    /**
     * 保存路径
     */
    private String uri;

}
