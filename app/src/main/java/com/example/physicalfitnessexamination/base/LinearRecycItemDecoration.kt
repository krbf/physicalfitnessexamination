package com.example.physicalfitnessexamination.base

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * 垂直列表 间距控制器
 * Created by chenzhiyuan On 2018/12/27
 */
class LinearRecycItemDecoration(val left: Int, val top: Int, val right: Int, val bottom: Int) :
        RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.left = left
        outRect.top = top
        outRect.right = right
        outRect.bottom = bottom
    }
}