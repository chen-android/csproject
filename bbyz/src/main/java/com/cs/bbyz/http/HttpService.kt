package com.cs.bbyz.http

import android.content.Context
import com.cs.bbyz.constant.Constant
import com.cs.bbyz.module.FilterDate
import com.cs.bbyz.module.SchemItem
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
	private val requestInstance: RequestInstance =
			HttpMethods.getClassInstance(Constant.BUSSINESS_TYPE_COMMON, RequestInstance::class.java)

	/**
	 * 登录
	 */
	fun requestLogin(
			context: Context,
			command: String,
			account: String,
			pwd: String,
			next: ((user: User?) -> Unit)
	) {
		requestInstance.login(
				command,
				account,
				getEncryptRequestMap(mutableMapOf<String, Any>().apply {
					put("Password", pwd)
				}, command)
		).map(DecryptFun(command, User::class.java))
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(ProgressObserver(
						context,
						{
							next(it)
						},
						null
				))
	}

	/**
	 * 可操作车站
	 */
	fun requestStation(
			context: Context,
			command: String,
			next: (stationList: List<Station>?) -> Unit
	) {
		requestInstance.stationList(command, getEncryptRequestMap(mutableMapOf(), command))
				.map(DecryptArrayFun(command, Station::class.java))
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(ProgressObserver(
						context,
						{
							next(it)
						},
						null
				))
	}

	/**
	 * 车次列表
	 */
	fun requestSchemList(
			context: Context,
			command: String,
			contentMap: MutableMap<String, Any>,
			next: (schemList: List<SchemItem>?) -> Unit,
			error: (code: Int, message: String) -> Unit
	) {
		requestInstance.schemList(command, getEncryptRequestMap(contentMap, command))
				.map(DecryptArrayFun(command, SchemItem::class.java))
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(ProgressObserver(
						context,
						{
							next(it)
						}, error,
						false
				))
	}

	/**
	 * 车次过滤时间范围
	 */
	fun requestFilterDate(
			context: Context,
			command: String,
			contentMap: MutableMap<String, Any>,
			next: (filterDate: List<FilterDate>?) -> Unit
	) {
		requestInstance.schemFilterDate(command, getEncryptRequestMap(contentMap, command))
				.map(DecryptArrayFun(command, FilterDate::class.java))
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(ProgressObserver(
						context,
						{
							next(it)
						}, null
				))
	}

	fun getEncryptRequestMap(
			contentMap: MutableMap<String, Any>,
			command: String
	): MutableMap<String, Any> {
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