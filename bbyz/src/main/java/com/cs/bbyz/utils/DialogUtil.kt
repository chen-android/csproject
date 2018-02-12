package com.cs.bbyz.utils

import android.content.Context
import com.afollestad.materialdialogs.MaterialDialog
import com.cs.bbyz.R


/**
 * @author chenshuai12619
 * @date 2018-02-08
 */
object DialogUtil {
	fun showProgressDialog(context: Context): MaterialDialog {
		var dialog = MaterialDialog.Builder(context)
				.content(R.string.please_wait)
				.progress(true, 0)
				.build()
		return dialog.apply {
			dialog.show()
		}
	}
}