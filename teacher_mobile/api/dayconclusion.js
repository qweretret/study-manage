//每日总结
import {
	$ajaxPost,
	$ajaxGet
} from "@/utils/methods.js"

//请求学生提交的情况
export const getList = (data) => {
	const url = '/teacherApplet/feed/listPerson'

	return $ajaxGet(url, data)
}

//请求老师班级
export const getClass = _ => {
	const url = '/teacherApplet/feed/listDown'

	return $ajaxGet(url)
}

//请求学生列表
export const getStuInfo = (data) => {
	const url = '/teacherApplet/feed/lookStudent'

	return $ajaxGet(url, data)
}

//请求学生详细信息
export const stuDetailed = (data) => {
	const url = '/teacherApplet/feed/lookData'

	return $ajaxGet(url, data)
}

//上一天下一天的反馈
export const getLastDay = (data) => {
	const url = '/teacherApplet/feed/list'

	return $ajaxGet(url, data)
}

//提交评价与评分
export const pullScroe = (data) => {
	const url = '/teacherApplet/feed/evaluate'

	return $ajaxGet(url, data)
}


