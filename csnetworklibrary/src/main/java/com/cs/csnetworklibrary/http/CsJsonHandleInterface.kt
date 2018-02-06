package com.cs.csnetworklibrary.http

/**
 * 网络请求过程中，对json的特殊处理。
 * @author chenshuai12619
 * @date 2018-02-06
 */
interface CsJsonHandleInterface {
	fun handleJson(jsonString: String): String
}