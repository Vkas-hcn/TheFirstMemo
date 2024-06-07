package com.flying.dutch.myapplication.view


import android.util.SparseArray
import android.view.View
import androidx.recyclerview.widget.RecyclerView


/**
 * RecyclerView ViewHolder基类
 *
 *
 * 使用 [.mViews] 对ItemView的子view进行存储，同时使用 [.getView] 方法进行ItemView
 * 中的子View的获取。获取方式是：如果mViews中存在则直接使用，不存在则从ItemView中find。
 *
 *
 * Created by DavidChen on 2018/5/30.
 */
class RecyclerViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
    private val mViews: SparseArray<View?>

    init {
        mViews = SparseArray()
    }

    /**
     * 获取需要的View，如果已经存在引用则直接获取，如果不存在则重新加载并保存
     *
     * @param viewId id
     * @return 需要的View
     */
    fun getView(viewId: Int): View? {
        var view = mViews[viewId]
        if (view == null) {
            view = itemView.findViewById(viewId)
            mViews.put(viewId, view)
        }
        return view
    }
}