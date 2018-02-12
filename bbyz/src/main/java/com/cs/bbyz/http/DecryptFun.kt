package com.cs.bbyz.http

import com.alibaba.fastjson.JSON
import com.cs.bbyz.constant.Constant
import com.cs.bbyz.http.encrypt.AES256Encryption
import com.orhanobut.logger.Logger
import io.reactivex.functions.Function
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener

/**
 * 这里传clazz，属于kotlin的关系。
 * https://blog.simon-wirtz.de/kotlin-reified-types/
 * @author chenshuai12619
 * @date 2018-02-06
 */
class DecryptFun<T>(private var command: String, private var clazz: Class<T>?) : Function<HttpResponse<T>, HttpResponse<T>> {
	override fun apply(t: HttpResponse<T>): HttpResponse<T> {
		if (t.success) {
			decrypt(t, command)
		}
		return t
	}

	companion object {
		fun <T> decrypt(resp: HttpResponse<T>, command: String): HttpResponse<T> {
			if (resp.success) {
				val content = AES256Encryption.decrypt(AES256Encryption.hexStringToBytes(resp.content), Constant.rsaKeyCache[command]?.toByteArray())
				val json = JSONObject(JSON.toJSONString(resp))
				val tokener = JSONTokener(content).nextValue()
				json.put("obj", if (tokener is JSONObject) JSONObject(content) else JSONArray(content))
				Logger.json(json.toString())
				return JSON.parseObject(json.toString(), resp.javaClass)
			}
			return resp
		}
	}
}