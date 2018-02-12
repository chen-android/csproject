package com.cs.bbyz.http

import android.content.Context
import com.afollestad.materialdialogs.MaterialDialog
import com.blankj.utilcode.util.ToastUtils
import com.cs.bbyz.R
import com.cs.bbyz.utils.DialogUtil
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 * @author chenshuai12619
 * @date 2018-02-12
 */
class ProgressObserver<T>(var context: Context, var success: ((result: T?) -> Unit), var error: ((code: Int, message: String) -> Unit)?) : Observer<HttpResponse<T>> {

	var dialog: MaterialDialog? = null
	override fun onComplete() {
		dialog?.dismiss()
	}

	override fun onSubscribe(d: Disposable) {
		dialog = DialogUtil.showProgressDialog(context)
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
		dialog?.dismiss()
	}
}