package com.cs.bbyz.application

import android.app.Application
import com.cs.bbyz.constant.Constant
import com.cs.bbyz.http.HttpRequestJsonHandler
import com.cs.csnetworklibrary.http.HttpMethods
import com.cs.cswidgetandutilslibrary.CsUtils
import com.cs.cswidgetandutilslibrary.database.PropertiesUtils

/**
 * @author chenshuai12619
 * @date 2018-02-07
 */
class MainApplication : Application() {
	override fun onCreate() {
		super.onCreate()
		PropertiesUtils.loadPropertiesFile(this, "net_config.properties")
		CsUtils.init(this)
		HttpMethods.init(Constant.BUSSINESS_TYPE_COMMON, requestJsonHandle = HttpRequestJsonHandler())
	}
}