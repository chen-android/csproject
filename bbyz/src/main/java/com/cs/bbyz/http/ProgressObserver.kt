package com.cs.bbyz.http

import android.app.Dialog
import android.content.Context
import com.cs.bbyz.R
import com.cs.bbyz.module.HttpResponse
import com.cs.bbyz.utils.DialogUtil
import com.cs.cswidgetandutilslibrary.utils.ToastUtils
import com.orhanobut.logger.Logger
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 * @author chenshuai12619
 * @date 2018-02-12
 */
class ProgressObserver<T>(
		private var context: Context,
		private var success: ((result: T?) -> Unit),
		private var error: ((code: Int, message: String) -> Unit)?,
		private var showProgress: Boolean = true
) : Observer<HttpResponse<T>> {

	var dialog: Dialog? = null
	override fun onComplete() {
		dialog?.dismiss()
	}

	override fun onSubscribe(d: Disposable) {
		dialog = if (showProgress) DialogUtil.showProgressDialog(context) else null
	}

	override fun onNext(t: HttpResponse<T>) {
		if (t.success) {
			success(t.obj)
		} else {
			error?.let { error!!(t.returnNo!!, t.returnInfo!!) }
					?: ToastUtils.showLong(t.returnInfo)
		}
	}

	override fun onError(e: Throwable) {
		ToastUtils.showShort(R.string.network_error)
		e.printStackTrace()
		Logger.e(e.message)
		dialog?.dismiss()
	}
}