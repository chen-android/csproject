package com.cs.bbyz.ui

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.text.format.DateFormat
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.cs.bbyz.R
import com.cs.bbyz.adapter.SchemListAdapter
import com.cs.bbyz.constant.Constant
import com.cs.bbyz.http.HttpService
import com.cs.bbyz.storage.CacheUtils
import com.cs.bbyz.utils.DialogUtil
import com.cs.cswidgetandutilslibrary.utils.ToastUtils
import com.cs.cswidgetandutilslibrary.view.SimpleVerticalDecoration
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
	var schemNo: String = ""
	var stopNo: String = ""
	lateinit var schemDate: String
	var showOverTime: Boolean = false
	var adapter: SchemListAdapter? = null

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
		schemDate = DateFormat.format(Constant.FORMAT_YMD, System.currentTimeMillis()).toString()
	}

	private fun initEvent() {
		nav_view.setNavigationItemSelectedListener(this)
		fab.setOnClickListener {

		}
		home_srl.refreshHeader = ClassicsHeader(this)
		home_srl.isEnableLoadmore = false
		home_srl.isEnableRefresh = true
		home_srl.setOnRefreshListener {
			requestSchemList()
		}
		home_rv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
		home_rv.addItemDecoration(SimpleVerticalDecoration())
		home_rv.itemAnimator = DefaultItemAnimator()
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

	/**
	 * 查询可用车站
	 */
	private fun requestStationList() {
		HttpService.requestStation(this, Constant.COMMAND_STATION.first, {
			if (it?.isNotEmpty() == true) {
				DialogUtil.showSimpleListDialog(
						this,
						getString(R.string.station_title),
						it,
						{ _, item ->
							CacheUtils.station = item
							home_srl.autoRefresh()
						}
				)
			} else {
				ToastUtils.showShort(getString(R.string.no_usable_station))
			}
		})
	}

	private fun requestSchemList() {
		val contentMap: MutableMap<String, Any> = mutableMapOf(
				Pair("StationID", CacheUtils.station!!.ID!!),
				Pair("DriveDate", this.schemDate),
				Pair("SchemNo", this.schemNo),
				Pair("StopNo", this.stopNo),
				Pair("ShowOverTimeSchem", if (this.showOverTime) "1" else "0")

		)
		HttpService.requestSchemList(
				this,
				Constant.COMMAND_SCHEMLIST.first,
				contentMap,
				{
					home_srl.finishRefresh()
					if (it != null && it.isNotEmpty()) {
						home_no_data_tv.visibility = View.GONE
						if (this.adapter == null) {
							this.adapter = SchemListAdapter(it)
						}
						home_rv.adapter = this.adapter
					} else {
						home_no_data_tv.visibility = View.VISIBLE
					}
				},
				{ _, message ->
					ToastUtils.showLong(message)
					home_srl.finishRefresh()
					this.adapter?.clearData()
					this.adapter?.notifyDataSetChanged()
					home_no_data_tv.visibility = View.VISIBLE
				}
		)
	}
}
