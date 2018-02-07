package com.cs.bbyz.http

/**
 * Created by chenshuai12619 on 2017-09-01.
 */

data class HttpResponse<T>(var returnInfo: String?, var returnNo: Int?, var secure: String?,
                           var content: String?, var obj: T?) {
	var success: Boolean = false
		get() = returnNo == 0
}