import { $ajaxPost ,$ajaxGet} from "@/utils/methods.js"

export const Evaluation = (data) => {
    const url = "study/selfevaluation/list"
    return $ajaxGet(url, data)
}

export const evapost = (data) => {
    const url = "study/selfevaluation/save"
    return $ajaxPost(url, data)
}

export const inter = (data) => {
    const url = "study/summarize/wxList"
    return $ajaxGet(url, data)
}

export const submit = (data) => {
    const url = "study/summarize/wxSave"
    return $ajaxPost(url, data)
}