package com.cs.csnetworklibrary.http

import android.util.Log
import com.cs.cswidgetandutilslibrary.CsUtils
import com.cs.cswidgetandutilslibrary.database.PropertiesUtils
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

/**
 * Created by chenshuai12619 on 2017/8/31 13:37.
 */
object HttpMethods {
	private const val DB_BASE_URL: String = ""

	//key:业务类型
	private val retrofitMap: MutableMap<String, Retrofit>
	private val urlCaches: MutableMap<String, String>
	private var DEFAULT_TIMEOUT: Long? = 30 * 1000

	private val okHttpClientBuilder: OkHttpClient.Builder
		get() {
			val builder = OkHttpClient.Builder()
			builder.connectTimeout(DEFAULT_TIMEOUT!!, TimeUnit.SECONDS)

			if (CsUtils.isAppDebug) {
				val interceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message -> Log.i("BBKB-LOGGER", message) })
				interceptor.level = HttpLoggingInterceptor.Level.BASIC
				builder.addInterceptor(interceptor)
			}
			return builder
		}

	init {
		DEFAULT_TIMEOUT = PropertiesUtils.getProperty("HTTP_TIMEOUT_SECOND", PropertiesUtils.PropertyType.INT)

		retrofitMap = mutableMapOf()
		urlCaches = mutableMapOf()
	}

	fun init(vararg types: String) {//type ,是配置文件，地址key
		for (type in types) {
			retrofitMap[type] = Retrofit.Builder()
					.client(okHttpClientBuilder.build())
					.addConverterFactory(BbkbConverterFactory.create())
					.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
					.baseUrl(getBaseUrl(type))
					.build()
		}
	}

	fun init(requestJsonHandle: BbkbJsonHandleInterface, responseJsonHandle: BbkbJsonHandleInterface, vararg types: String) {//type ,是配置文件，地址key
		for (type in types) {

			retrofitMap[type] = Retrofit.Builder()
					.client(okHttpClientBuilder.build())
					.addConverterFactory(BbkbConverterFactory.create(requestJsonHandle, responseJsonHandle))
					.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
					.baseUrl(getBaseUrl(type))
					.build()
		}
	}

	fun <T> getClassInstance(businessType: String, clazz: Class<T>): T {
		return retrofitMap[businessType]!!.create(clazz)
	}

	private fun getBaseUrl(type: String): String {
		if (BbkbUtils.isAppDebug) {
			return if (urlCaches.containsKey(type)) {
				urlCaches[type]!!
			} else {
				var url = BbkbDbUtils.getInstance().getString(HttpConstant.DB_BASE_URL, type, "")
				if (EmptyUtils.isEmpty(url)) {
					url = PropertiesUtils.getInstance().getProperty("http_path_" + type + "_debug")
					BbkbDbUtils.getInstance().putString(HttpConstant.DB_BASE_URL, type, url)
				}
				urlCaches[type] = url
				url
			}
		} else {
			return if (urlCaches.containsKey(type)) {
				urlCaches[type]!!
			} else {
				val url = PropertiesUtils.getInstance().getProperty<String>("http_path_" + type)
				urlCaches[type] = url
				url
			}
		}
	}

	fun clearUrlCache() {
		BbkbDbUtils.getInstance().clear(HttpConstant.DB_BASE_URL)
	}


}
