package com.cs.cswidgetandutilslibrary.database

import android.text.TextUtils
import android.util.Base64
import com.blankj.utilcode.util.Utils
import java.io.*

/**
 * Created by chenshuai12619 on 2018-02-05.
 */
object CsDbUtils {
	private val trayMap: MutableMap<String, CsTrayPreFerence> = mutableMapOf()

	fun init(vararg databaseName: String) {
		databaseName.forEach {
			if (trayMap.containsKey(it).not()) {
				createNewDataBase(it, 1)
			}
		}
	}

	private fun createNewDataBase(dbName: String, version: Int) {
		trayMap[dbName] = CsTrayPreFerence(Utils.getApp(), dbName, version)
	}

	fun putString(databaseName: String, key: String, value: String) = trayMap[databaseName]?.put(key, value)
	fun getString(databaseName: String, key: String): String? = trayMap[databaseName]?.getString(key)

	fun putInt(databaseName: String, key: String, value: Int) = trayMap[databaseName]?.put(key, value)
	fun getInt(databaseName: String, key: String): Int? = trayMap[databaseName]?.getInt(key, 0)

	fun putObject(databaseName: String, key: String, value: Any) {
		val baos = ByteArrayOutputStream()
		var oos: ObjectOutputStream? = null
		try {
			with(baos) {
				oos = ObjectOutputStream(this)
				oos!!.writeObject(value)
				trayMap[databaseName]?.put(key, String(Base64.encode(this.toByteArray(), Base64.DEFAULT)))
			}
		} catch (e: IOException) {

		} finally {
			baos.close()
			if (oos != null) {
				oos?.close()
			}
		}
	}

	fun <T> getObject(databaseName: String, key: String): T? {
		val string = getString(databaseName, key)
		if (TextUtils.isEmpty(string)) {
			return null
		}
		return Base64.decode(string!!.toByteArray(), Base64.DEFAULT).let {
			val bais = ByteArrayInputStream(it)
			try {
				val bis = ObjectInputStream(bais)
				val o = bis.readObject()
				bis.readObject() as T
			} catch (e: Exception) {
				null
			} finally {
				bais.close()
			}
		}


	}
}