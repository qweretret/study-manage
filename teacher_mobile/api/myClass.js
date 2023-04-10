//我的班级
import {
	$ajaxPost,
	$ajaxGet
} from "@/utils/methods.js"

// 获取老师班级
export const getClass = (data) => {
	const url = '/lky/tuisongzuoye/listClass'
	return $ajaxGet(url, data)
}

// 获取学生
export const getStuinfo = (data) => {
	const url = '/study/myclass/getStudentList2'
	return $ajaxGet(url, data)
}