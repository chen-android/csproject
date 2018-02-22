package com.cs.bbyz.utils

import android.app.Dialog
import android.content.Context
import android.support.v7.app.AlertDialog
import android.view.View
import com.cs.bbyz.R


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
}