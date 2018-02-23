package com.cs.bbyz.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cs.bbyz.R
import com.cs.bbyz.module.SchemItem
import com.cs.cswidgetandutilslibrary.utils.TimeUtils
import kotlinx.android.synthetic.main.item_home_schem.view.*

/**
 * @author chenshuai12619
 * @date 2018-02-23
 */
class SchemListAdapter(schemList: List<SchemItem>) : RecyclerView.Adapter<SchemListAdapter.ViewHolder>() {
	lateinit var context: Context
	var schemList: List<SchemItem> = schemList

	fun clearData() {
		this.schemList = listOf()
	}

	override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
		context = parent!!.context
		return ViewHolder(LayoutInflater.from(parent.context).inflate(
				R.layout.item_home_schem,
				parent,
				false))
	}

	override fun getItemCount(): Int {
		return schemList.size
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		val item = this.schemList[position]
		holder.itemView.schem_time.text = TimeUtils.string2NewFormat(item.DriveTime, "HH:mm")
		holder.itemView.schem_bus_id.text = context.getString(R.string.schem_bus_id, item.SchemNo)
		holder.itemView.schem_name.text = context.getString(R.string.schem_station, item.StationName, item.EndStopName)
		holder.itemView.schem_sale_num.text = context.getString(R.string.schem_sale_num, item.SaledNum)
		holder.itemView.schem_least_seat_num.text = context.getString(R.string.schem_seat_remain, item.LeastSeatNum)
		holder.itemView.schem_reserve_num.text = context.getString(R.string.schem_reverse_num, item.ReserveNum)
		holder.itemView.schem_status.text =
				if (item.IsRun == 0) {
					if (item.SaledNum > 0) {
						"停班有售"
					} else {
						"停班"
					}
				} else {
					"开班"
				}
		holder.itemView.schem_type.visibility = if (item.SchemTypeCode == 2) View.VISIBLE else View.GONE
	}

	inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}