package com.cs.cswidgetandutilslibrary.utils

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author chenshuai12619
 * @date 2018-02-23
 */
object TimeUtils {
	val FORMAT_YMDHMS = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINESE)
	val FORMAT_YMDHM = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINESE)
	val FORMAT_YMD = SimpleDateFormat("yyyy-MM-dd", Locale.CHINESE)
	val FORMAT_HMS = SimpleDateFormat("HH:mm:ss", Locale.CHINESE)
	val FORMAT_HM = SimpleDateFormat("HH:mm", Locale.CHINESE)

	/**
	 * string 转 millis
	 */
	fun string2Millis(date: String, format: DateFormat = FORMAT_YMDHMS): Long {
		return string2Date(date, format).time
	}

	/**
	 * string 转 date
	 */
	fun string2Date(date: String, format: DateFormat = FORMAT_YMDHMS): Date {
		return format.parse(date)
	}

	/**
	 * string 转 新的时间格式
	 */
	fun string2NewFormat(source: String, toFormat: String, sourceFormat: DateFormat = FORMAT_YMDHMS): String {
		return SimpleDateFormat(toFormat, Locale.CHINESE).format(string2Date(source, sourceFormat))
	}

	/**
	 * millis 转 string
	 */
	fun millis2String(millis: Long, format: DateFormat = FORMAT_YMDHMS): String {
		return format.format(millis)
	}
}