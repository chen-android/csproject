package com.cs.bbyz.ui

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.text.format.DateFormat
import android.view.Menu
import android.view.MenuItem
import com.cs.bbyz.R
import com.cs.bbyz.constant.Constant
import com.cs.bbyz.http.HttpService
import com.cs.bbyz.module.HttpResponse
import com.cs.bbyz.storage.CacheUtils
import com.cs.bbyz.utils.DialogUtil
import com.cs.cswidgetandutilslibrary.utils.ToastUtils
import com.google.gson.Gson
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		setSupportActionBar(toolbar)

		initView()
		initEvent()

	}

	private fun initView() {
		val toggle = ActionBarDrawerToggle(
				this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
		drawer_layout.addDrawerListener(toggle)
		toggle.syncState()

		toolbar.title = getString(R.string.station_title)
		toolbar.subtitle = DateFormat.format(Constant.FORMAT_YMDHM, System.currentTimeMillis())

	}

	private fun initEvent() {
		nav_view.setNavigationItemSelectedListener(this)
		fab.setOnClickListener {
			var json = "{'content':'adfasd','returnInfo':'info','obj':[{'ID':'1','StationName':'丽水'},{'ID':'2','StationName':'宁波'}]}"
			val resp = Gson().fromJson(
					json,
					HttpResponse::class.java
			)
			Logger.d(resp)
		}
	}

	override fun onBackPressed() {
		if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
			drawer_layout.closeDrawer(GravityCompat.START)
		} else {
			super.onBackPressed()
		}
	}

	override fun onCreateOptionsMenu(menu: Menu): Boolean {
		menuInflater.inflate(R.menu.main, menu)
		return true
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		return when (item.itemId) {
			R.id.action_station -> {
				requestStationList()
				true
			}
			R.id.action_settings -> true
			else -> super.onOptionsItemSelected(item)
		}
	}

	override fun onNavigationItemSelected(item: MenuItem): Boolean {
		// Handle navigation view item clicks here.
		when (item.itemId) {
			R.id.nav_camera -> {
				// Handle the camera action
			}
			R.id.nav_gallery -> {

			}
			R.id.nav_slideshow -> {

			}
			R.id.nav_manage -> {

			}
			R.id.nav_share -> {

			}
			R.id.nav_send -> {

			}
		}

		drawer_layout.closeDrawer(GravityCompat.START)
		return true
	}

	private fun requestStationList() {
		HttpService.requestStation(this, Constant.COMMAND_STATION.first, {
			if (it?.isNotEmpty() == true) {
				DialogUtil.showSimpleListDialog(
						this,
						getString(R.string.station_title),
						it,
						{ _, item ->
							CacheUtils.station = item

						}
				)
			} else {
				ToastUtils.showShort(getString(R.string.no_usable_station))
			}
		})
	}
}
