import { $ajaxPost ,$ajaxGet,$ajaxPut} from "@/utils/methods.js"

export const Class = (data) => {
    const url = "/study/myclass/getStudentList"
    return $ajaxGet(url, data)
}
export const student = (data) => {
    const url = "/study/myclass/getStudent"
    return $ajaxGet(url, data)
}
export const studentgetinfo = (data) => {
    const url = "/system/user/wxToUpdate"
    return $ajaxGet(url, data)
}
export const studentputinfo = (data) => {
    const url = "/system/user/wxUpdate"
    return $ajaxPut(url, data)
}