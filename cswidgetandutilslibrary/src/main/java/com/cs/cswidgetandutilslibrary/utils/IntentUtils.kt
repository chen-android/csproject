package com.cs.cswidgetandutilslibrary.utils

import android.content.Intent
import com.cs.cswidgetandutilslibrary.CsUtils


/**
 * @author chenshuai12619
 * @date 2018-02-22
 */
object IntentUtils {
	/**
	 * 获取打开 App 的意图
	 *
	 * @param packageName 包名
	 * @return 打开 App 的意图
	 */
	fun getLaunchAppIntent(packageName: String): Intent? {
		return getLaunchAppIntent(packageName, false)
	}

	/**
	 * 获取打开 App 的意图
	 *
	 * @param packageName 包名
	 * @param isNewTask   是否开启新的任务栈
	 * @return 打开 App 的意图
	 */
	fun getLaunchAppIntent(packageName: String, isNewTask: Boolean): Intent? {
		val intent = CsUtils.app.packageManager.getLaunchIntentForPackage(packageName)
				?: return null
		return getIntent(intent, isNewTask)
	}

	private fun getIntent(intent: Intent, isNewTask: Boolean): Intent {
		return if (isNewTask) intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) else intent
	}
}