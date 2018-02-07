package com.cs.bbyz.http

import com.cs.bbyz.module.User
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * @author chenshuai12619
 * @date 2018-02-07
 */
interface RequestInstance {

	@POST("api/Mobile")
	fun login(@Query("command") command: String, @Query("workNo") workNo: String, @Body map: MutableMap<String, Any>): Observable<HttpResponse<User>>
}