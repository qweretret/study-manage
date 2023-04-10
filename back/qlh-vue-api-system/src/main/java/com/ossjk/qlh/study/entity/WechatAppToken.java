package com.ossjk.qlh.study.entity;

import lombok.Data;

@Data
public class WechatAppToken {
    public Integer errcode;
    public String errmsg;
    public String openid;
    public String sessionKey;
    public String unionId;
}
