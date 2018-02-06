package com.cs.csnetworklibrary.http

/**
 * @author chenshuai12619
 * @date 2018-02-06
 */
class DefaultCsJsonHandle : CsJsonHandleInterface {
	override fun handleJson(jsonString: String): String {
		return jsonString
	}
}