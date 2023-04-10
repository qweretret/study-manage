import {
	ApiUtils,
	_axios
} from '@/api/apiUtil.js';
import constant from '@/util/constant.js';
const api = {
	list: (params, successCallback, failCallback, exceptionCallback) => {
		ApiUtils.getLoadingAjax(constant.serverUrl + "/stus/student/list", params, successCallback, failCallback, exceptionCallback);
	},
	getStulist: (params, successCallback, failCallback, exceptionCallback) => {
		ApiUtils.getLoadingAjax(constant.serverUrl + "/stus/student/getStulist", params, successCallback, failCallback, exceptionCallback);
	},
	intnStu(params, successCallback, failCallback, exceptionCallback) {
		ApiUtils.getLoadingAjax(constant.serverUrl + "/stus/student/intnStu", params, successCallback, failCallback, exceptionCallback);
	},
	toImport(params, successCallback, failCallback, exceptionCallback) {
		ApiUtils.getLoadingAjax(constant.serverUrl + "/stus/student/toImport", params, successCallback, failCallback, exceptionCallback);
	},
	imports: (params, successCallback, failCallback, exceptionCallback) => {
		ApiUtils.putLoadingAjax(constant.serverUrl + "/stus/student/imports", params, successCallback, failCallback, exceptionCallback);
	},
	outports: (params, successCallback, failCallback, exceptionCallback) => {
		ApiUtils.downloadingAjax(constant.serverUrl + "/stus/student/exportScore", params, successCallback, failCallback, exceptionCallback);
	},
	outports2: (params, successCallback, failCallback, exceptionCallback) => {
		ApiUtils.downloadingAjax(constant.serverUrl + "/stus/student/exportScore2", params, successCallback, failCallback, exceptionCallback);
	},
	save: (params, successCallback, failCallback, exceptionCallback) => {
		ApiUtils.postLoadingAjax(constant.serverUrl + "/stus/student/save", params, successCallback, failCallback, exceptionCallback);
	},
	toUpdate: (params, successCallback, failCallback, exceptionCallback) => {
		ApiUtils.getLoadingAjax(constant.serverUrl + "/stus/student/toUpdate", params, successCallback, failCallback, exceptionCallback);
	},
	update: (params, successCallback, failCallback, exceptionCallback) => {
		ApiUtils.putLoadingAjax(constant.serverUrl + "/stus/student/update", params, successCallback, failCallback, exceptionCallback);
	},
	remove: (params, successCallback, failCallback, exceptionCallback) => {
		ApiUtils.deleteLoadingAjax(constant.serverUrl + "/stus/student/remove", params, successCallback, failCallback, exceptionCallback);
	},
	revise: (params, successCallback, failCallback, exceptionCallback) => {
		ApiUtils.getLoadingAjax(constant.serverUrl + "/stus/student/revise", params, successCallback, failCallback, exceptionCallback);
	},
}

export default api;
