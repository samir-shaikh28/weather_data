package com.example.weather.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class HorizontalSpaceItemDecoration(private val width: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {

        val position = parent.getChildLayoutPosition(view)
        outRect.right = width

        if (position == 0) {
            outRect.left = width
        } else {
            outRect.left = 0
        }
    }
}