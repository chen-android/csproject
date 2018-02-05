package com.cs.cswidgetandutilslibrary

import android.app.Application
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.Utils
import com.cs.cswidgetandutilslibrary.database.PropertiesUtils
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy

/**
 * 工具类初始化汇总，减少主工程的各种工具初始化代码
 * Created by chenshuai12619 on 2018-02-05.
 */
class CsUtils {
	companion object {
		var isAppDebug: Boolean = false

		fun init(context: Application) {
			Utils.init(context)
			val packageName: String? = PropertiesUtils.getProperty("net_plugin_name", PropertiesUtils.PropertyType.STRING)
			isAppDebug = AppUtils.isInstallApp(packageName)

			/*日之类初始化，必须置顶，因为下面几个工具类初始化，可能会用到logger。*/
			initLogger()
		}

		private fun initLogger() {
			val formatStrategy = PrettyFormatStrategy.newBuilder()
					.showThreadInfo(false)
					.tag("BBKB-LOGGER")
					.build()

			Logger.addLogAdapter(object : AndroidLogAdapter(formatStrategy) {
				override fun isLoggable(priority: Int, tag: String?): Boolean {
					return isAppDebug
				}
			})
		}
	}
}