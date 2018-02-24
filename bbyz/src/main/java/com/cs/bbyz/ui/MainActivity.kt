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
import com.cs.cswidgetandutilslibrary.utils.TimeUtils
import com.cs.cswidgetandutilslibrary.utils.ToastUtils
import com.cs.cswidgetandutilslibrary.view.SimpleVerticalDecoration
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.navigation_schem_filter.*
import java.util.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
	var schemNo: String = ""
	var stopNo: String = ""
	val schemDate: Calendar = Calendar.getInstance()
	var showOverTime: Boolean = false
	var adapter: SchemListAdapter? = null
	var minMilliseconds: Long = 0L
	var maxMilliseconds: Long = 0L

	var filterDate: Calendar = Calendar.getInstance()
	var filterShowOverTime: Boolean = false
	var filterSchemNo: String = ""
	var filterStopNo: String = ""
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
		toolbar.navigationIcon = resources.getDrawable(R.drawable.icon_main_person_center)

		toolbar.title = getString(R.string.station_title)
		toolbar.subtitle = DateFormat.format(Constant.FORMAT_YMDHM, schemDate.timeInMillis)
	}

	private fun initEvent() {
		nav_person_center.setNavigationItemSelectedListener(this)
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
		initFilter()
	}

	/**初始化过滤界面事件*/
	private fun initFilter() {
		schem_filter_date_tv.setOnClickListener {
			DialogUtil.buildDatePickerDialog(
					this,
					schemDate,
					this.minMilliseconds,
					this.maxMilliseconds,
					{ milliseconds ->
						filterDate.timeInMillis = milliseconds

					}
			)
		}
	}

	override fun onBackPressed() {
		if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
			drawer_layout.closeDrawer(GravityCompat.START)
		} else if (drawer_layout.isDrawerOpen(GravityCompat.END)) {
			drawer_layout.closeDrawer(GravityCompat.END)
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
			R.id.action_filter -> {
				if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
					drawer_layout.closeDrawer(GravityCompat.START)
				}
				if (this.minMilliseconds != 0L && this.maxMilliseconds != 0L) {
					drawer_layout.openDrawer(GravityCompat.END)
				} else {
					requestFilterDateRange()
				}

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

	private fun requestFilterDateRange() {
		HttpService.requestFilterDate(
				this,
				Constant.COMMAND_FILTER_DATE.first,
				mutableMapOf(
						Pair("StationID", CacheUtils.station!!.ID!!)
				),
				{
					if (it != null && it.isNotEmpty()) {
						var times = it[0].Column1.split("~")
						this.minMilliseconds = TimeUtils.string2Millis(times[0])
						this.maxMilliseconds = TimeUtils.string2Millis(times[1])
					}
					drawer_layout.openDrawer(GravityCompat.END)
				}
		)

	}

	/**
	 * 查询车次
	 */
	private fun requestSchemList() {
		val contentMap: MutableMap<String, Any> = mutableMapOf(
				Pair("StationID", CacheUtils.station!!.ID!!),
				Pair("DriveDate", DateFormat.format(Constant.FORMAT_YMD, schemDate.timeInMillis).toString()),
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
