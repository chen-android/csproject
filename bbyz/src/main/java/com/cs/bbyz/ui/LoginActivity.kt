package com.cs.bbyz.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.cs.bbyz.R
import com.cs.bbyz.constant.Constant
import com.cs.bbyz.http.HttpResultFun
import com.cs.bbyz.http.HttpService
import com.cs.bbyz.storage.CacheUtils
import com.orhanobut.logger.Logger
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_login)

		init()
		login_bt.setOnClickListener {
			HttpService.requestInstance.login(Constant.COMMAND_LOGIN.first, login_account_et.text.toString(), HttpService.getEncryptRequestMap(mutableMapOf<String, Any>().apply {
				put("Password", login_pwd_et.text.toString())
				put(Constant.COMMAND_NO, Constant.COMMAND_LOGIN.first)
			}, Constant.COMMAND_LOGIN.first)).map(HttpResultFun(Constant.COMMAND_LOGIN.first))
					.subscribeOn(Schedulers.io())
					.observeOn(AndroidSchedulers.mainThread())
					.subscribe {
						Logger.d(it)
					}
		}
	}

	private fun init() {
		val account = CacheUtils.getUserAccount()
		login_account_et.setText(account)
		CacheUtils.getUserPwd().let {
			login_pwd_et.setText(it)
			login_save_pwd_cb.isChecked = it.isEmpty().not()
		}
	}

}
