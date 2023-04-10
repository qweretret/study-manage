//面试点评
import {
	$ajaxPost,
	$ajaxGet,
	$ajaxPut
} from "@/utils/methods.js"

// 获取老师班级
export const getClass = (data) => {
	const url = '/interview/mianshi/dropDownBox'
	return $ajaxGet(url, data)
}

// 获取学生列表
export const getStuList = (data) => {
	const url = '/interview/mianshi/selectInterview'
	return $ajaxGet(url, data)
}

// 获取学生详细信息
export const getStuDetailed = (data) => {
	const url = '/interview/mianshi/list2'

	return $ajaxGet(url, data)
}

//点评

export const pullMsg = (data) => {
	const url = '/interview/mianshi/msdp'

	return $ajaxPut(url, data)
}
