package com.flying.dutch.myapplication.view

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SpaceItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        if (view.visibility == View.VISIBLE) {
            outRect.top = space
            outRect.left = space
            outRect.right = space
            outRect.bottom = space
        } else {
            outRect.top = 0
            outRect.left = 0
            outRect.right = 0
            outRect.bottom = 0
        }
    }
}

