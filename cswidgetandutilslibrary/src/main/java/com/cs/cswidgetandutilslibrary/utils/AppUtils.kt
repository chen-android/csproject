package com.cs.cswidgetandutilslibrary.utils

/**
 * @author chenshuai12619
 * @date 2018-02-22
 */
object AppUtils {
	fun isInstallApp(packageName: String?): Boolean {
		return packageName?.let {
			IntentUtils.getLaunchAppIntent(packageName) != null
		} ?: false
	}
}