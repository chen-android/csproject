package com.cs.csnetworklibrary.http

import com.google.gson.Gson
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
class CsResponseBodyConverter(private var type: Type?, private var handle: CsJsonHandleInterface) : Converter<ResponseBody, Any> {

	override fun convert(value: ResponseBody?): Any {
		val bufferedSource: BufferedSource = Okio.buffer(value!!.source())
		val tempStr = bufferedSource.readUtf8() ?: throw IOException("response is null")
		bufferedSource.close()

		return Gson().fromJson(handle.handleJson(tempStr), type)
	}
}