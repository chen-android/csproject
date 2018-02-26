package com.cs.bbyz.ui.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cs.bbyz.R
import com.cs.bbyz.constant.Constant
import com.cs.bbyz.http.HttpService
import com.cs.bbyz.storage.CacheUtils
import com.cs.bbyz.utils.DialogUtil
import com.cs.cswidgetandutilslibrary.utils.TimeUtils
import kotlinx.android.synthetic.main.fragment_schem_filter.*
import kotlinx.android.synthetic.main.fragment_schem_filter.view.*
import java.util.*

/**
 *
 */
class SchemFilterFragment : Fragment() {

	lateinit var rootView: View

	private var filterDate: Calendar = Calendar.getInstance()
	private var filterBusType: Int = 0
	private var filterShowOverTime: Boolean = false
	private var filterSchemNo: String = ""
	private var filterStopNo: String = ""
	private var minDate: Long = 0L
	private var maxDate: Long = 0L

	private lateinit var mListener: OnFilterConfirmListener

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
	                          savedInstanceState: Bundle?): View? {

		rootView = inflater.inflate(R.layout.fragment_schem_filter, container, false)
		initView()
		return rootView
	}

	private fun initView() {
		rootView.schem_filter_date_tv.let {
			it.text = TimeUtils.millis2String(this.filterDate.timeInMillis, TimeUtils.FORMAT_YMD)
			it.setOnClickListener {
				DialogUtil.buildDatePickerDialog(activity!!, filterDate, minDate, maxDate, { milliseconds ->
					this.filterDate.timeInMillis = milliseconds
					rootView.schem_filter_date_tv.text = TimeUtils.millis2String(milliseconds, TimeUtils.FORMAT_YMD)
				}).show(activity!!.supportFragmentManager, "date_dialog")
			}
		}
		rootView.schem_filter_bus_id_et.addTextChangedListener(object : TextWatcher {
			override fun afterTextChanged(s: Editable?) {
				rootView.schem_filter_station_tv.isEnabled = s!!.isEmpty()

			}

			override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
			}

			override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

			}
		})
		rootView.schem_filter_confirm_bt.setOnClickListener {
			filterBusType = when (rootView.schem_filter_type_rg.checkedRadioButtonId) {
				R.id.schem_filter_type_0_rb -> 0
				R.id.schem_filter_type_1_rb -> 1
				R.id.schem_filter_type_2_rb -> 2
				else -> 0
			}
			filterSchemNo = rootView.schem_filter_bus_id_et.text.toString()
			filterShowOverTime = schem_filter_show_overtime_cb.isChecked
			mListener.onConfirm(
					filterDate.timeInMillis,
					filterBusType,
					filterShowOverTime,
					filterSchemNo,
					filterStopNo
			)
		}
		rootView.schem_filter_cancel_bt.setOnClickListener {
			mListener.onCancel()
		}
	}

	override fun onAttach(context: Context) {
		super.onAttach(context)
		if (context is OnFilterConfirmListener) {
			mListener = context
		} else {
			throw RuntimeException(context.toString() + " must implement OnFilterConfirmListener")
		}
	}

	fun loadDate() {
		if (minDate == 0L) {
			requestFilterDateRange()
		}
	}

	private fun requestFilterDateRange() {
		HttpService.requestFilterDate(
				activity!!,
				Constant.COMMAND_FILTER_DATE.first,
				mutableMapOf(
						Pair("StationID", CacheUtils.station!!.ID!!)
				),
				{
					if (it != null && it.isNotEmpty()) {
						val times = it[0].Column1.split("~")
						this.minDate = TimeUtils.string2Millis(times[0], TimeUtils.FORMAT_YMD)
						this.maxDate = TimeUtils.string2Millis(times[1], TimeUtils.FORMAT_YMD)
					}

				}
		)

	}

	/**
	 *
	 */
	interface OnFilterConfirmListener {
		fun onConfirm(dateMillis: Long, busType: Int, showOverTime: Boolean, schemNo: String, stopNo: String)
		fun onCancel()
	}

	companion object {

		fun newInstance(): SchemFilterFragment {
			return SchemFilterFragment()
		}
	}
}
