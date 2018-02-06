package com.cs.csnetworklibrary.http

import com.alibaba.fastjson.JSON
import okhttp3.ResponseBody
import okio.BufferedSource
import okio.Okio
import retrofit2.Converter
import java.io.IOException
import java.lang.reflect.Type

/**
 * @author chenshuai12619
 * @date 2018-02-06
 */
class CsResponseBodyConverter(private var type: Type?, private var handle: CsJsonHandleInterface? = DefaultCsJsonHandle()) : Converter<ResponseBody, Any> {

	override fun convert(value: ResponseBody?): Any {
		val bufferedSource: BufferedSource = Okio.buffer(value!!.source())
		val tempStr = bufferedSource.readUtf8() ?: throw IOException("response is null")
		bufferedSource.close()
		return JSON.parseObject(handle?.handleJson(tempStr) ?: tempStr, type)
	}
}