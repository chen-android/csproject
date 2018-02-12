package com.cs.bbyz.storage

import com.cs.bbyz.module.Common
import com.cs.cswidgetandutilslibrary.database.CsDbUtils

/**
 * @author chenshuai12619
 * @date 2018-02-07
 */
object CacheUtils {
	var common: Common? = null
	var workerNo: String? = null
	fun getUserAccount(): String = CsDbUtils.getString("db_user_account")
	fun setUserAccount(account: String?) = CsDbUtils.putString("db_user_account", account)

	fun getUserPwd(): String = CsDbUtils.getString("db_user_pwd")
	fun setUserPwd(pwd: String?) = CsDbUtils.putString("db_user_pwd", pwd)
}