package com.cs.bbyz.http

import com.alibaba.fastjson.JSON
import com.blankj.utilcode.util.EncryptUtils
import com.cs.bbyz.constant.Constant
import com.cs.bbyz.http.encrypt.AES256Encryption
import com.cs.bbyz.http.encrypt.RSA
import com.cs.csnetworklibrary.http.CsJsonHandleInterface

/**
 * @author chenshuai12619
 * @date 2018-02-07
 */
class HttpRequestJsonHandler : CsJsonHandleInterface {
	override fun handleJson(jsonString: String): String {
		val json = JSON.parseObject(jsonString)
		val command: String = json.getString(Constant.COMMAND_NO)
				?: throw NullPointerException("请求参数没有携带commandNo")
		val rsaKey = EncryptUtils.encryptMD5ToString(System.currentTimeMillis().toString())
		Constant.rsaKeyCache[command] = rsaKey
		json["key"] = RSA.encrypt(rsaKey, Constant.PUBLIC_KEY)
		json["content"] = AES256Encryption.encrypt(json.getString("content").toByteArray(), json.getString("key").toByteArray())
		return json.toString()
	}
}