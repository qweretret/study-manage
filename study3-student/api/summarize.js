import { $ajaxPost ,$ajaxGet} from "@/utils/methods.js"

export const summarize = (data) => {
    const url = "/study/feedback/listStuxiao"
    return $ajaxGet(url, data)
}
