package com.cs.bbyz.http

import com.cs.bbyz.constant.Constant
import com.cs.bbyz.http.encrypt.AES256Encryption
import com.cs.bbyz.module.HttpResponse
import com.cs.bbyz.utils.GsonUtil
import com.orhanobut.logger.Logger
import io.reactivex.functions.Function
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener

/**
 * @author chenshuai12619
 * @date 2018-02-06
 */
class DecryptFun<T>(private var command: String, private var clazz: Class<T>) : Function<HttpResponse<T>, HttpResponse<T>> {
	override fun apply(t: HttpResponse<T>): HttpResponse<T> {
		if (t.success) {
			decrypt(t, command, clazz)
		}
		return t
	}

	companion object {
		fun <T> decrypt(resp: HttpResponse<T>, command: String, clazz: Class<T>): HttpResponse<T> {
			if (resp.success) {
				val content = AES256Encryption.decrypt(AES256Encryption.hexStringToBytes(resp.content), Constant.rsaKeyCache[command]?.toByteArray())
				val json = JSONObject(GsonUtil.gson.toJson(resp))
				val tokener = JSONTokener(content).nextValue()
				json.put("obj", if (tokener is JSONObject) JSONObject(content) else JSONArray(content))
				Logger.json(json.toString())
				return if (tokener is JSONObject) fromJsonObject(json.toString(), clazz) else fromJsonArray(json.toString(), clazz)

			}
			return resp
		}

		private fun <T> fromJsonObject(jsonString: String, clazz: Class<T>): HttpResponse<T> {
			val type = ParameterizedTypeImpl(HttpResponse::class.java, arrayOf(clazz))
			return GsonUtil.gson.fromJson(jsonString, type)
		}

		private fun <T> fromJsonArray(jsonString: String, clazz: Class<T>): HttpResponse<T> {
			val listType = ParameterizedTypeImpl(List::class.java, arrayOf(clazz))
			val type = ParameterizedTypeImpl(HttpResponse::class.java, arrayOf(listType))
			return GsonUtil.gson.fromJson(jsonString, type)
		}
	}
}