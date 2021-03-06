package com.cs.csnetworklibrary.http

import android.util.Log
import com.cs.cswidgetandutilslibrary.CsUtils
import com.cs.cswidgetandutilslibrary.database.CsDbUtils
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
	private const val DB_BASE_URL: String = "db_base_url"

	//key:业务类型
	private val retrofitMap: MutableMap<String, Retrofit>
	private val urlCaches: MutableMap<String, String>
	private var DEFAULT_TIMEOUT: Int = 30

	private val okHttpClientBuilder: OkHttpClient.Builder
		get() {
			val builder = OkHttpClient.Builder()
			builder.connectTimeout(DEFAULT_TIMEOUT.toLong(), TimeUnit.SECONDS)

			if (CsUtils.isAppDebug) {
				val interceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message -> Log.i("BBYZ-NETWORK", message) })
				interceptor.level = HttpLoggingInterceptor.Level.BASIC
				builder.addInterceptor(interceptor)
			}
			return builder
		}

	init {
		DEFAULT_TIMEOUT = PropertiesUtils.getProperty("HTTP_TIMEOUT_SECOND", PropertiesUtils.PropertyType.INT) ?: 30

		retrofitMap = mutableMapOf()
		urlCaches = mutableMapOf()
	}

	/**
	 * types 是业务类型，如果应用需要多个请求地址时，会用到。
	 */
	fun init(vararg types: String, requestJsonHandle: CsJsonHandleInterface = DefaultCsJsonHandle(), responseJsonHandle: CsJsonHandleInterface = DefaultCsJsonHandle()) {
		for (type in types) {

			retrofitMap[type] = Retrofit.Builder()
					.client(okHttpClientBuilder.build())
					.addConverterFactory(CsConverterFactory.create(requestJsonHandle, responseJsonHandle))
					.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
					.baseUrl(getBaseUrl(type))
					.build()
		}
	}

	fun <T> getClassInstance(businessType: String, clazz: Class<T>): T {
		return retrofitMap[businessType]!!.create(clazz)
	}

	private fun getBaseUrl(type: String): String {
		if (CsUtils.isAppDebug) {
			return if (urlCaches.containsKey(type)) {
				urlCaches[type]!!
			} else {
				var url = CsDbUtils.getString(DB_BASE_URL + type)
				if (url.isEmpty()) {
					url = PropertiesUtils.getProperty("http_path_${type}_debug") ?: throw NullPointerException("接口地址为空，请检查")
					CsDbUtils.putString(DB_BASE_URL + type, url)
				}
				urlCaches[type] = url
				url
			}
		} else {
			return if (urlCaches.containsKey(type)) {
				urlCaches[type]!!
			} else {
				val url: String = PropertiesUtils.getProperty("http_path_$type")
						?: throw NullPointerException("接口地址为空，请检查")
				urlCaches[type] = url
				url
			}
		}
	}

	fun clearUrlCache() {
		CsDbUtils.remove(DB_BASE_URL)
	}


}
