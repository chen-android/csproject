package com.cs.bbyz.http

import com.cs.bbyz.constant.Constant
import com.cs.bbyz.http.encrypt.AES256Encryption
import com.cs.bbyz.module.HttpResponse
import com.cs.bbyz.utils.GsonUtil
import com.orhanobut.logger.Logger
import io.reactivex.functions.Function
import org.json.JSONObject

/**
 * @author chenshuai12619
 * @date 2018-02-06
 */
class DecryptFun<T>(private var command: String, private var clazz: Class<T>) : Function<HttpResponse<T>, HttpResponse<T>> {
	override fun apply(t: HttpResponse<T>): HttpResponse<T> {
		if (t.success) {
			return decrypt(t, command, clazz)
		}
		return t
	}

	companion object {
		fun <T> decrypt(resp: HttpResponse<T>, command: String, clazz: Class<T>): HttpResponse<T> {
			if (resp.success) {
				val content = AES256Encryption.decrypt(AES256Encryption.hexStringToBytes(resp.content), Constant.rsaKeyCache[command]?.toByteArray())
				val json = JSONObject(GsonUtil.gson.toJson(resp))
				json.put("obj", JSONObject(content))
				Logger.json(json.toString())
				return fromJsonObject(json.toString(), clazz)

			}
			return resp
		}

		private fun <T> fromJsonObject(jsonString: String, clazz: Class<T>): HttpResponse<T> {
			val type = ParameterizedTypeImpl(HttpResponse::class.java, arrayOf(clazz))
			return GsonUtil.gson.fromJson(jsonString, type)
		}
	}
}