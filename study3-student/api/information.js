import { $ajaxPost ,$ajaxGet} from "@/utils/methods.js"

export const information = (data) => {
    const url = "/lky/tuisongzuoye/list"
    return $ajaxGet(url, data)
}

export const check = (data) => {
    const url = "/lky/tuisongzuoye/qianshou"
    return $ajaxGet(url, data)
}