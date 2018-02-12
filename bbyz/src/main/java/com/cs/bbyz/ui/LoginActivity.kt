package com.cs.bbyz.ui

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.cs.bbyz.R
import com.cs.bbyz.constant.Constant
import com.cs.bbyz.http.HttpService
import com.cs.bbyz.storage.CacheUtils
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_login)
		init()
		initEvent()
	}

	private fun init() {
		val account = CacheUtils.getUserAccount()
		login_account_et.setText(account)
		CacheUtils.getUserPwd().let {
			login_pwd_et.setText(it)
			login_save_pwd_cb.isChecked = it.isEmpty().not()
		}
	}

	private fun initEvent() {
		login_bt.setOnClickListener {
			HttpService.requestLogin(
					this,
					Constant.COMMAND_LOGIN.first,
					login_account_et.text.toString(),
					login_pwd_et.text.toString(),
					{
						CacheUtils.setUserAccount(login_account_et.text.toString())
						CacheUtils.workerNo = login_account_et.text.toString()
						if (login_save_pwd_cb.isChecked) {
							CacheUtils.setUserPwd(login_pwd_et.text.toString())
						}
						startActivity(Intent(this, MainActivity::class.java))
					}
			)
		}
	}

}
