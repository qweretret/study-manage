import {
	ApiUtils,
	_axios
}from '@/api/apiUtil.js';
import constant from '@/util/constant.js';
const api = {
	list: (params, successCallback, failCallback, exceptionCallback) => {
		ApiUtils.getLoadingAjax(constant.serverUrl + "/lky/tuisongzuoye/list", params, successCallback, failCallback, exceptionCallback);
	},
	//查询班级与ID
	listClass: (params, successCallback, failCallback, exceptionCallback) => {
		ApiUtils.getLoadingAjax(constant.serverUrl + "/lky/tuisongzuoye/listClass", params, successCallback, failCallback, exceptionCallback);
	},
	//查询与班级对应的学生与ID
	listStuid: (params, successCallback, failCallback, exceptionCallback) => {
		ApiUtils.getLoadingAjax(constant.serverUrl + "/lky/tuisongzuoye/listStuid", params, successCallback, failCallback, exceptionCallback);
	},
	intnTcr(params, successCallback, failCallback, exceptionCallback) {
		ApiUtils.getLoadingAjax(constant.serverUrl + "/lky/tuisongzuoye/intnTcr", params, successCallback, failCallback, exceptionCallback);
	},
	qianshou: (params, successCallback, failCallback, exceptionCallback) => {
		ApiUtils.getLoadingAjax(constant.serverUrl + "/lky/tuisongzuoye/qianshou", params, successCallback, failCallback, exceptionCallback);
	},
	save: (params, successCallback, failCallback, exceptionCallback) => {
		ApiUtils.postLoadingAjax(constant.serverUrl + "/lky/tuisongzuoye/save", params, successCallback, failCallback, exceptionCallback);
	},
	toUpdate: (params, successCallback, failCallback, exceptionCallback) => {
		ApiUtils.getLoadingAjax(constant.serverUrl + "/lky/tuisongzuoye/toUpdate", params, successCallback, failCallback, exceptionCallback);
	},
	update: (params, successCallback, failCallback, exceptionCallback) => {
		ApiUtils.putLoadingAjax(constant.serverUrl + "/lky/tuisongzuoye/update", params, successCallback, failCallback, exceptionCallback);
	},
	remove: (params, successCallback, failCallback, exceptionCallback) => {
		ApiUtils.deleteLoadingAjax(constant.serverUrl + "/lky/tuisongzuoye/remove", params, successCallback, failCallback, exceptionCallback);
	},
}

export default api;
