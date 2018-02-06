package com.cs.bbyz.http

import com.blankj.utilcode.util.ToastUtils
import io.reactivex.functions.Function

/**
 * @author chenshuai12619
 * @date 2018-02-06
 */
class HttpResultFun<T> : Function<HttpResponse<T>, T> {
	override fun apply(t: HttpResponse<T>): T {
		if (t.success.not()) {
			ToastUtils.showShort(t.returnInfo)
		}
		return t.obj
	}
}