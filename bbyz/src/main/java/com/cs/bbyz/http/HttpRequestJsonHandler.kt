package com.cs.bbyz.http

import com.cs.bbyz.constant.Constant
import com.cs.bbyz.http.encrypt.AES256Encryption
import com.cs.bbyz.http.encrypt.RSA
import com.cs.csnetworklibrary.http.CsJsonHandleInterface
import com.cs.cswidgetandutilslibrary.utils.EncryptUtils
import com.orhanobut.logger.Logger
import org.json.JSONObject

/**
 * @author chenshuai12619
 * @date 2018-02-07
 */
class HttpRequestJsonHandler : CsJsonHandleInterface {
	override fun handleJson(jsonString: String): String {
		val json = JSONObject(jsonString)

		Logger.json(jsonString)
		val command: String = json.getString(Constant.COMMAND_NO)
				?: throw NullPointerException("请求参数没有携带commandNo")
		val rsaKey = EncryptUtils.encryptMD5ToString(System.currentTimeMillis().toString())
		Constant.rsaKeyCache[command] = rsaKey
		json.put("key", RSA.encrypt(rsaKey, Constant.PUBLIC_KEY))
		json.put("content", AES256Encryption.encrypt(json.getString("content").toByteArray(), rsaKey.toByteArray()))
		return json.toString()
	}
}