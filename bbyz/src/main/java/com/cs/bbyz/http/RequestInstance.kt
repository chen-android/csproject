package com.cs.bbyz.http

import com.cs.bbyz.module.HttpResponse
import com.cs.bbyz.module.SchemItem
import com.cs.bbyz.module.Station
import com.cs.bbyz.module.User
import com.cs.bbyz.storage.CacheUtils
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * @author chenshuai12619
 * @date 2018-02-07
 */
interface RequestInstance {

	/**
	 * 登录
	 */
	@POST("api/Mobile")
	fun login(
			@Query("command") command: String,
			@Query("workNo") workNo: String,
			@Body map: MutableMap<String, Any>
	): Observable<HttpResponse<User>>

	/**
	 * 请求可选车站
	 */
	@POST("api/Mobile")
	fun stationList(
			@Query("command") command: String,
			@Body map: MutableMap<String, Any>,
			@Query("workNo") workNo: String = CacheUtils.workerNo!!
	): Observable<HttpResponse<List<Station>>>

	/**
	 * 请求车次
	 */
	@POST("api/Mobile")
	fun schemList(
			@Query("command") command: String,
			@Body map: MutableMap<String, Any>,
			@Query("workNo") workNo: String = CacheUtils.workerNo!!
	): Observable<HttpResponse<List<SchemItem>>>
}