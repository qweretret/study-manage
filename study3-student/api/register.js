import { $ajaxPost ,$ajaxGet,$ajaxPut} from "@/utils/methods.js"

export const register = (data) => {
    const url = "/interview/mianshi/list"
    return $ajaxGet(url, data)
}

export const registers = (data) => {
    const url = "/interview/company/list"
    return $ajaxGet(url, data)
}

export const registera = (data) => {
    const url = "/interview/posts/list"
    return $ajaxGet(url, data)
}

export const  repost = (data) => {
    const url = "/interview/mianshi/wxSave"
    return $ajaxPost(url, data)
}

export const  qiyepost = (data) => {
    const url = "/interview/company/save"
    return $ajaxPost(url, data)
}
export const  gwpost = (data) => {
    const url = "/interview/posts/save"
    return $ajaxPost(url, data)
}
export const  Replenish = (data) => {
    const url = "/interview/mianshi/zhjie"
    return $ajaxPut(url, data)
}
export const  filepost = (data) => {
    const url = "/components/uploadComponent/wxFileUpload"
    return $ajaxPost(url, data)
}
export const  getzjie = (data) => {
    const url = "/interview/mianshi/toUpdate"
    return $ajaxGet(url, data)
}
