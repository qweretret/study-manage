//每日反馈
import {
	$ajaxPost,
	$ajaxGet
} from "@/utils/methods.js"

//请求学生列表 

export const getStuInfo = (data) => {
	const url = '/study/summarize/getclazzvaluetion'

	return $ajaxGet(url, data)
}

//请求提交情况
export const getCommitInfo = (data) => {
	const url = '/study/summarize/getcircumstance'

	return $ajaxGet(url, data)
}

//查看上下天 /study/summarize/dateDisplay
export const getLastdayOrnextDay = (data) => {
	const url = '/study/summarize/dateDisplay'

	return $ajaxGet(url, data)
}

//查看学生详情 /study/summarize/modifyAndView
export const getDetailedList = (data) => {
	const url = '/study/summarize/modifyAndView'

	return $ajaxGet(url, data)
}

//已读
export const pullRead = (data) => {
	const url = '/study/summarize/updatelist'

	return $ajaxGet(url, data)
}