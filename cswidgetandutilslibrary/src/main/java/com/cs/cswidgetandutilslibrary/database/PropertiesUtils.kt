package com.cs.cswidgetandutilslibrary.database

import android.content.Context
import com.orhanobut.logger.Logger
import java.util.*

/**
 * Created by chenshuai12619 on 2018-02-05.
 */
object PropertiesUtils {
	private var configProperties: Properties? = null
	private var fileNameCache: MutableList<String>? = null

	init {
		configProperties = Properties()
		fileNameCache = mutableListOf()
	}

	enum class PropertyType {
		INT, STRING, DOUBLE, BOOLEAN
	}

	fun loadPropertiesFile(context: Context, vararg configName: String) {
		val pList: MutableList<Properties> = mutableListOf()
		configName.forEach {
			if (fileNameCache!!.contains(it)) {
				Logger.w("配置了同名Properties文件或相同文件添加第二次，请检查主工程，包括library")
			} else {
				fileNameCache!!.add(it)
				pList.add(loadConfigAssets(context, it))
			}
		}
		pList.forEach { pro ->
			pro.stringPropertyNames().forEach {
				if (configProperties!!.contains(pro)) {
					Logger.w("配置了同名Properties文件或相同文件添加第二次，请检查主工程，包括library")
				} else {
					configProperties!![it] = pro[it]
				}
			}
		}
	}

	private fun loadConfigAssets(context: Context, name: String): Properties {
		return Properties().apply {
			this.load(context.assets.open(name))
		}
	}

	fun <T> getProperty(key: String, type: PropertyType = PropertyType.STRING): T? {
		var value: String = configProperties!![key].toString()

		return when (type) {
			PropertyType.INT -> value.toIntOrNull() as T
			PropertyType.STRING -> value as T
			PropertyType.DOUBLE -> value.toDoubleOrNull() as T
			PropertyType.BOOLEAN -> value.toBoolean() as T
		}
	}
}