package com.cs.cswidgetandutilslibrary.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

/**
 * @author chenshuai12619
 * @date 2018-02-23
 */
class RecyclerViewDecoration(context: Context, orientation: Int, drawableRes: Int) : RecyclerView.ItemDecoration() {
	private var context: Context = context
	private var orientation: Int = LinearLayoutManager.VERTICAL
		set(value) {
			setLayoutOrientation(value)
		}

	var divider: Drawable? = null

	init {
		this.divider = this.context.resources.getDrawable(drawableRes)
		this.orientation = orientation
	}

	private fun setLayoutOrientation(orientation: Int) {
		if (orientation != LinearLayoutManager.HORIZONTAL && orientation != LinearLayoutManager.VERTICAL)
			throw IllegalArgumentException("invalid orientation")
		this.orientation = orientation
	}

	override fun onDraw(c: Canvas?, parent: RecyclerView?, state: RecyclerView.State?) {
		if (this.orientation == LinearLayoutManager.HORIZONTAL) {

		} else {

		}
	}

	private fun drawHorizontalLine(c: Canvas?, parent: RecyclerView?, state: RecyclerView.State?) {
		val left = parent!!.paddingLeft
		val right = parent.paddingRight
		val childCount = parent.childCount
		for (i in 0 until childCount) {
			val child = parent.getChildAt(i)
			val params = child.layoutParams as RecyclerView.LayoutParams
			val top = child.bottom + params.bottomMargin
			val bottom = top + this.divider!!.intrinsicHeight
			this.divider!!.setBounds(left, top, right, bottom)
			this.divider!!.draw(c)
		}
	}
}