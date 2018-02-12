package com.cs.bbyz.http

import android.content.Context
import com.cs.bbyz.constant.Constant
import com.cs.bbyz.module.Station
import com.cs.bbyz.module.User
import com.cs.bbyz.storage.CacheUtils
import com.cs.csnetworklibrary.http.HttpMethods
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

/**
 * @author chenshuai12619
 * @date 2018-02-07
 */
object HttpService {
	val requestInstance: RequestInstance = HttpMethods.getClassInstance(Constant.BUSSINESS_TYPE_COMMON, RequestInstance::class.java)

	fun requestLogin(context: Context, command: String, account: String, pwd: String, next: ((user: User?) -> Unit)) {
		requestInstance.login(
				command,
				account,
				getEncryptRequestMap(mutableMapOf<String, Any>().apply {
					put("Password", pwd)
				}, command)
		).map(DecryptFun(command, User::class.java))
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(ProgressObserver<User>(
						context,
						{
							next(it)
						},
						null
				))
	}

	fun requestStation(context: Context, command: String, next: (stationList: List<Station>?) -> Unit) {
		requestInstance.stationList(command, getEncryptRequestMap(mutableMapOf<String, Any>(), command))
				.map(DecryptFun(command, null))
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(ProgressObserver<List<Station>>(
						context,
						{
							next(it)
						},
						null
				))
	}

	fun getEncryptRequestMap(contentMap: MutableMap<String, Any>, command: String): MutableMap<String, Any> {
		val map = mutableMapOf<String, Any>()
		map["content"] = contentMap
		map["common"] = getRequestCommonMap()
		map[Constant.HTTP_SECURE_TAG] = true
		map[Constant.COMMAND_NO] = command
		return map
	}

	fun getUnEncryptRequestMap(contentMap: HashMap<String, Any>): HashMap<String, Any> {
		val map = HashMap<String, Any>()
		map["content"] = contentMap
		map["common"] = getRequestCommonMap()
		map[Constant.HTTP_SECURE_TAG] = false
		return map
	}

	private fun getRequestCommonMap(): HashMap<String, Any?> {
		val map = HashMap<String, Any?>()
		map["appId"] = CacheUtils.common?.appId
		map["usId"] = CacheUtils.getUserAccount()
		map["loginStatus"] = CacheUtils.common?.loginStatus
		map["terminalType"] = "3"
		map["imei"] = CacheUtils.common?.imei
		map["appVer"] = CacheUtils.common?.appVer
		map["mobileVer"] = CacheUtils.common?.mobileVer
		map["channelVer"] = "bbyz"
		map["platformCode"] = CacheUtils.common?.platformCode
		return map
	}
}