package com.cs.csnetworklibrary.http

import com.alibaba.fastjson.JSON
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Converter
import java.nio.charset.Charset

/**
 * @author chenshuai12619
 * @date 2018-02-06
 */
class CsRequestBodyConverter(private var handle: CsJsonHandleInterface? = DefaultCsJsonHandle()) : Converter<Any, RequestBody> {
	private val mediaType: MediaType = MediaType.parse("application/json; charset=UTF-8")!!

	override fun convert(value: Any): RequestBody {
		val s = JSON.toJSONString(value)
		val result = handle!!.handleJson(s)
		return RequestBody.create(mediaType, result.toByteArray(Charset.forName("UTF-8")))
	}

}