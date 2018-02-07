package com.cs.bbyz.http

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.TypeReference
import com.blankj.utilcode.util.ToastUtils
import com.cs.bbyz.constant.Constant
import com.cs.bbyz.http.encrypt.AES256Encryption
import io.reactivex.functions.Function

/**
 * @author chenshuai12619
 * @date 2018-02-06
 */
class HttpResultFun<T>(private var command: String) : Function<HttpResponse<T>, T?> {
	override fun apply(t: HttpResponse<T>): T? {
		if (t.success) {
			decrypt(t, command)
		} else {
			ToastUtils.showShort(t.returnInfo)

		}
		return t.obj
	}

	companion object {
		fun <T> decrypt(resp: HttpResponse<T>, command: String): HttpResponse<T> {
			if (resp.success) {
				val content = AES256Encryption.decrypt(resp.content?.toByteArray(), Constant.rsaKeyCache[command]?.toByteArray())
				resp.obj = JSON.parseObject(content, object : TypeReference<T>() {})
			}
			return resp
		}
	}
}