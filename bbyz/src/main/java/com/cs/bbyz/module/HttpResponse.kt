package com.cs.bbyz.module;

/**
 * @author chenshuai12619
 * @date 2018-02-13
 */

data class HttpResponse<T>(var returnInfo: String?, var returnNo: Int?, var secure: String?,
                           var content: String?, var obj: T?, var listObj: List<T>?) {
	var success: Boolean = false
		get() = returnNo == 0
}
