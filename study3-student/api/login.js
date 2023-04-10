/**
  * @title: 考试接口
  * @description: 
  * @author JUN
  */

import { $ajaxPost ,$ajaxGet} from "@/utils/methods.js"

// 获取微信openid
export const login = (data) => {
    const url = "/wx/getSessionInfo"
    return $ajaxGet(url, data)
}

// 微信小程序登录
export const wxLogin = (data) => {
    const url = "/wx/wxLogin"
    return $ajaxPost(url, data)
}

// 获取微信用户信息
export const getWxUserInfo = (data) => {
    const url = "/wx/getWxUserInfo"
    return $ajaxGet(url, data)
}

//获取用户信息
export const getUserInfo = () => {
    const url = "/wx/getInfo"
    return $ajaxPost(url)
}