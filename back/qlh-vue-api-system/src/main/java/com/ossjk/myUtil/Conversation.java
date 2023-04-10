package com.ossjk.myUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ossjk.config.mvc.ResourceMappersProperties;
import com.ossjk.config.wxminiapp.WxMaProperties;
import com.ossjk.qlh.study.entity.Tuisongzuoye;
import com.ossjk.qlh.study.entity.WxMsgDto;
import com.ossjk.qlh.study.entity.WxTemplateDataDto;
import com.ossjk.qlh.study.vo.Tuisongzuoye2Vo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class Conversation {

    public Conversation(){}

    public Conversation(String appid,String secret){
        this.appid = appid;
        this.secret = secret;
    }

    /**
     * 缓存的access_token
     */
    private static String accessToken;
    /**
     * access_token的失效时间
     */
    private static long expiresTime;

    private static String appid;

    private static String secret;

    public String getAccessToken() {

        // 判断accessToken是否已经过期，如果过期需要重新获取
        if (accessToken == null || expiresTime < System.currentTimeMillis()) {
            RestTemplate restTemplate = new RestTemplate();
            Map<String, String> params = new HashMap<>(2);
            params.put("APPID", appid);
            params.put("APPSECRET", secret);
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(
                    "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+appid+"&secret="+secret+"", String.class, params);
            String body = responseEntity.getBody();
            JSONObject object = JSON.parseObject(body);
            Integer errcode = object.getInteger("errcode");
            if (errcode != null && errcode != 0) {
                String errmsg = object.getString("errmsg");
                System.out.println("请求accessToken失败，返回码：" + errcode + "，错误信息：" + errmsg);
                // 抛个异常
            }
            // 缓存accessToken
            accessToken = object.getString("access_token");
            // 设置accessToken的失效时间
            long expires_in = object.getLong("expires_in");
            // 失效时间 = 当前时间 + 有效期(提前一分钟，也可不提前，这里只是稳妥一下)
            expiresTime = System.currentTimeMillis() + (expires_in - 60) * 1000;
        }
        return accessToken;
    }

    /**
     * 发送完成模板消息
     * //@param openid 用户的openid
     * @return 结果。发送成功，返回值实例：{"errcode":0,"errmsg":"ok","msgid":11111}
     */
    public String sendVlogCompleteTemplateMsg(Tuisongzuoye2Vo ts) {
        String time = null;
        String substring = null;
        if(ts != null){
            int length = ts.getContent().length();
            if(length > 10){
                substring = ts.getContent().substring(0, 10)+".....";
            }else{
                substring = ts.getContent();
            }
            time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        }
        RestTemplate restTemplate = new RestTemplate();
        //发送订阅消息的url，官网地址：https://developers.weixin.qq.com/miniprogram/dev/api-backend/open-api/subscribe-message/subscribeMessage.send.html
        String url = "https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token=" + getAccessToken();
        //拼接推送的模版
        WxMsgDto wxMsgDto = new WxMsgDto();
        //用户的openid（要发送给的那个用户）
        wxMsgDto.setTouser(ts.getWxappopenid());
        //订阅消息模板id
        wxMsgDto.setTemplate_id("EPrqxRPus0WZw5Vtmdr-bkjaCeREH-xZaOK4LzJQX-E");
        //点击消息跳转的页面
        wxMsgDto.setPage("pages/index/index");

        Map<String, WxTemplateDataDto> map = new HashMap<>(5);
        //根据从小程序中的模板获取的参数，进行赋值
        map.put("thing6", new WxTemplateDataDto(ts.getThejobtype()));//课程名称
        map.put("thing7", new WxTemplateDataDto(ts.getTname()));//老师姓名
        map.put("thing9", new WxTemplateDataDto(ts.getTname()));//老师姓名
        map.put("time2", new WxTemplateDataDto(time));//开课时间
        map.put("thing3", new WxTemplateDataDto(substring));//主题介绍
        wxMsgDto.setData(map);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, wxMsgDto, String.class);
        return responseEntity.getBody();
    }


    /**
     * 发送完成模板消息
     * //@param openid 用户的openid
     * @return 结果。发送成功，返回值实例：{"errcode":0,"errmsg":"ok","msgid":11111}
     */
    public String sendVlogCompleteTemplateMsg2(Tuisongzuoye2Vo t2) {
        String time = null;
        String substring = null;
        if(t2 != null){
            int length = t2.getContent().length();
            if(length > 10){
                substring = t2.getContent().substring(0, 10)+".....";
            }else{
                substring = t2.getContent();
            }
            time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        }
        RestTemplate restTemplate = new RestTemplate();
        //发送订阅消息的url，官网地址：https://developers.weixin.qq.com/miniprogram/dev/api-backend/open-api/subscribe-message/subscribeMessage.send.html
        String url = "https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token=" + getAccessToken();
        //拼接推送的模版
        WxMsgDto wxMsgDto = new WxMsgDto();
        //用户的openid（要发送给的那个用户）
        wxMsgDto.setTouser(t2.getWxappopenid());
        //订阅消息模板id
        wxMsgDto.setTemplate_id("EPrqxRPus0WZw5Vtmdr-bkjaCeREH-xZaOK4LzJQX-E");
        //点击消息跳转的页面
        wxMsgDto.setPage("pages/index/index");

        Map<String, WxTemplateDataDto> map = new HashMap<>(5);
        //根据从小程序中的模板获取的参数，进行赋值
        map.put("thing6", new WxTemplateDataDto(t2.getThejobtype()));//课程名称
        map.put("thing7", new WxTemplateDataDto(t2.getTname()));//老师姓名
        map.put("thing9", new WxTemplateDataDto(t2.getTname()));//老师姓名
        map.put("time2", new WxTemplateDataDto(time));//开课时间
        map.put("thing3", new WxTemplateDataDto(substring));//主题介绍
        wxMsgDto.setData(map);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, wxMsgDto, String.class);
        return responseEntity.getBody();
    }

}
