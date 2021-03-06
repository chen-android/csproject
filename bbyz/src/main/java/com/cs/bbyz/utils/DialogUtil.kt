package com.cs.bbyz.utils

import android.app.Dialog
import android.content.Context
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.View
import com.cs.bbyz.R
import com.jzxiang.pickerview.TimePickerDialog
import com.jzxiang.pickerview.data.Type
import java.util.*


/**
 * @author chenshuai12619
 * @date 2018-02-08
 */
object DialogUtil {
	fun showProgressDialog(context: Context): Dialog {
		return AlertDialog.Builder(context)
				.setView(View.inflate(context, R.layout.dialog_progress, null))
				.create()
				.apply {
					show()
				}
	}

	fun <T> showSimpleListDialog(context: Context, title: String, list: List<T>, click: (position: Int, item: T) -> Unit): Dialog {
		var items: Array<String> = Array(list.size, { i: Int -> list[i].toString() })

		return AlertDialog.Builder(context)
				.setTitle(title)
				.setItems(
						items,
						{ _, which ->
							click(which, list[which])
						}
				).create().apply { show() }

	}

	fun buildDatePickerDialog(context: Context, calendar: Calendar, minMilliseconds: Long, maxMilliseconds: Long, callback: (milliseconds: Long) -> Unit): DialogFragment {
		return TimePickerDialog.Builder()
				.setCallBack({ _, milliseconds ->
					callback(milliseconds)
				})
				.setCancelStringId("取消")
				.setSureStringId("确定")
				.setTitleStringId("日期选择")
				.setYearText("年")
				.setMonthText("月")
				.setDayText("日")
				.setCyclic(false)
				.setMinMillseconds(minMilliseconds)
				.setMaxMillseconds(maxMilliseconds)
				.setCurrentMillseconds(calendar.timeInMillis)
				.setType(Type.YEAR_MONTH_DAY)
				.setThemeColor(context.resources.getColor(R.color.colorPrimary))
				.setWheelItemTextNormalColor(context.resources.getColor(R.color.color_333333))
				.setWheelItemTextSelectorColor(context.resources.getColor(R.color.colorPrimary))
				.setWheelItemTextSize(12)
				.build()
	}
}