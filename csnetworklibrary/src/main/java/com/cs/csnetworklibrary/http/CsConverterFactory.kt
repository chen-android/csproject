package com.cs.csnetworklibrary.http

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

/**
 * @author chenshuai12619
 * @date 2018-02-06
 */
class CsConverterFactory private constructor(
		private val requestJsonHandle: CsJsonHandleInterface,
		private val responseJsonHandle: CsJsonHandleInterface
) : Converter.Factory() {

	companion object {
		fun create(
				requestJsonHandle: CsJsonHandleInterface,
				responseJsonHandle: CsJsonHandleInterface
		): CsConverterFactory = CsConverterFactory(requestJsonHandle, responseJsonHandle)
	}

	override fun responseBodyConverter(
			type: Type?,
			annotations: Array<out Annotation>?,
			retrofit: Retrofit?
	): Converter<ResponseBody, *>? {
		return CsResponseBodyConverter(type, responseJsonHandle)
	}

	override fun requestBodyConverter(
			type: Type?,
			parameterAnnotations: Array<out Annotation>?,
			methodAnnotations: Array<out Annotation>?,
			retrofit: Retrofit?
	): Converter<*, RequestBody>? {
		return CsRequestBodyConverter(requestJsonHandle)
	}
}