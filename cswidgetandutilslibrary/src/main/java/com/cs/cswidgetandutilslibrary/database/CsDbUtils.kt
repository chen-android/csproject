package com.cs.cswidgetandutilslibrary.database

import android.text.TextUtils
import android.util.Base64
import com.blankj.utilcode.util.Utils
import net.grandcentrix.tray.AppPreferences
import java.io.*

/**
 * Created by chenshuai12619 on 2018-02-05.
 */
object CsDbUtils {

	var db: AppPreferences? = null

	fun init() {
		db = AppPreferences(Utils.getApp())
	}

	fun putString(databaseName: String, key: String, value: String) = db!!.put(key, value)
	fun getString(databaseName: String, key: String): String? = db!!.getString(key)

	fun putInt(databaseName: String, key: String, value: Int) = db!!.put(key, value)
	fun getInt(databaseName: String, key: String): Int? = db!!.getInt(key, 0)

	fun putObject(databaseName: String, key: String, value: Any) {
		val baos = ByteArrayOutputStream()
		var oos: ObjectOutputStream? = null
		try {
			with(baos) {
				oos = ObjectOutputStream(this)
				oos!!.writeObject(value)
				db!!.put(key, String(Base64.encode(this.toByteArray(), Base64.DEFAULT)))
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