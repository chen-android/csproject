package com.cs.cswidgetandutilslibrary.utils

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author chenshuai12619
 * @date 2018-02-23
 */
object TimeUtils {
	val DEFAULT_FORMAT = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINESE)

	/**
	 * string 转 毫秒
	 */
	fun string2Millis(date: String, format: DateFormat = DEFAULT_FORMAT): Long {
		return string2Date(date, format).time
	}

	/**
	 * string 转 date
	 */
	fun string2Date(date: String, format: DateFormat = DEFAULT_FORMAT): Date {
		return format.parse(date)
	}

	/**
	 * string 转 新的时间格式
	 */
	fun string2NewFormat(source: String, toFormat: String, sourceFormat: DateFormat = DEFAULT_FORMAT): String {
		return SimpleDateFormat(toFormat, Locale.CHINESE).format(string2Date(source, sourceFormat))
	}
}