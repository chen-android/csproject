package com.cs.bbyz.http

import com.cs.bbyz.constant.Constant
import com.cs.bbyz.http.encrypt.AES256Encryption
import com.cs.bbyz.module.HttpResponse
import com.cs.bbyz.utils.GsonUtil
import com.orhanobut.logger.Logger
import io.reactivex.functions.Function
import org.json.JSONArray
import org.json.JSONObject

/**
 * @author chenshuai12619
 * @date 2018-02-06
 */
class DecryptArrayFun<T>(private var command: String, private var clazz: Class<T>) : Function<HttpResponse<List<T>>, HttpResponse<List<T>>> {
	override fun apply(t: HttpResponse<List<T>>): HttpResponse<List<T>> {
		if (t.success) {
			return decrypt(t, command, clazz)
		}
		return t
	}

	companion object {
		fun <T> decrypt(resp: HttpResponse<List<T>>, command: String, clazz: Class<T>): HttpResponse<List<T>> {
			if (resp.success) {
				val content = AES256Encryption.decrypt(AES256Encryption.hexStringToBytes(resp.content), Constant.rsaKeyCache[command]?.toByteArray())
				val json = JSONObject(GsonUtil.gson.toJson(resp))
				json.put("obj", JSONArray(content))
				Logger.json(json.toString())
				return fromJsonArray(json.toString(), clazz)

			}
			return resp
		}

		private fun <T> fromJsonArray(jsonString: String, clazz: Class<T>): HttpResponse<List<T>> {
			val listType = ParameterizedTypeImpl(List::class.java, arrayOf(clazz))
			val type = ParameterizedTypeImpl(HttpResponse::class.java, arrayOf(listType))
			return GsonUtil.gson.fromJson(jsonString, type)
		}
	}
}