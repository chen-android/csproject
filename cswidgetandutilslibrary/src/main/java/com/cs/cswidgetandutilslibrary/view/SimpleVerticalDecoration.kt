package com.cs.cswidgetandutilslibrary.view

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * @author chenshuai12619
 * @date 2018-02-23
 */
class SimpleVerticalDecoration : RecyclerView.ItemDecoration() {
	override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
		outRect?.set(0, 0, 0, 1)
	}
}