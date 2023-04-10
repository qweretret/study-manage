import {
	$ajaxPost,
	$ajaxGet,
	$ajaxDelete,
	$ajaxPut
} from "@/utils/methods.js"

// 请求班级对应学生
export const getClassInStu = (data) => {
	const url = '/lky/tuisongzuoye/listStuid'

	return $ajaxGet(url, data)
}

// 请求学生推送信息
export const getStudens = (data) => {
	const url = '/lky/tuisongzuoye/list2'

	return $ajaxGet(url, data)
}

// 请求老师所拥有全部班级的信息
export const getTeacherClassList = _ => {
	const url = '/lky/tuisongzuoye/listClass'

	return $ajaxGet(url)
}

// 请求学生详细信息
export const getStudensAll = (data) => {
	const url = '/lky/tuisongzuoye/list3'

	return $ajaxGet(url, data)
}

// 推送作业
export const pullHomeWork = (data) => {
	const url = '/lky/tuisongzuoye/tuisong'
	
	return $ajaxGet(url, data)
}

// 删除作业
export const deleteHomWork = (data) => {
	const url = '/lky/tuisongzuoye/remove'
	
	return $ajaxDelete(url, data)
}

// 确认修改信息
export const confirmChange = (data) => {
	const url = '/lky/tuisongzuoye/update'
	
	return $ajaxPut(url, data)
}

// 班级老师
export const getTeacherId = (data) => {
	const url = '/lky/tuisongzuoye/intnTcr'
	
	return $ajaxGet(url, data)
}

// /lky/tuisongzuoye/save

export const addInfo = (data) => {
	const url = '/lky/tuisongzuoye/save'
	
	return $ajaxPost(url, data)
}

// submittedToPush 添加并推送
export const addOrPullMsg = (data) => {
	const url = 'lky/tuisongzuoye/submittedToPush2'
	
	return $ajaxPost(url, data)
}