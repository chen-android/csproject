package com.cs.csnetworklibrary.http

import com.google.gson.Gson
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Converter
import java.nio.charset.Charset

/**
 * @author chenshuai12619
 * @date 2018-02-06
 */
class CsRequestBodyConverter(private var handle: CsJsonHandleInterface) : Converter<Any, RequestBody> {
	private val mediaType: MediaType = MediaType.parse("application/json; charset=UTF-8")!!

	override fun convert(value: Any): RequestBody {
		val s = Gson().toJson(value)
		val result = handle.handleJson(s)
		return RequestBody.create(mediaType, result.toByteArray(Charset.forName("UTF-8")))
	}

}