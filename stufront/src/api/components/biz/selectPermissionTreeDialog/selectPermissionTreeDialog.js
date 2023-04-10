import {
	ApiUtils,
	_axios
} from '@/api/apiUtil.js';
import constant from '@/util/constant.js';

const api = {
	list: (params, successCallback, failCallback, exceptionCallback) => {
		ApiUtils.getLoadingAjax(constant.serverUrl + "/components/permissionComponent/list", params, successCallback,
			failCallback,
			exceptionCallback);
	},
	getByIds: (params, successCallback, failCallback, exceptionCallback) => {
		ApiUtils.getLoadingAjax(constant.serverUrl + "/components/permissionComponent/getByIds", params, successCallback,
			failCallback,
			exceptionCallback);
	}
}
export default api;
