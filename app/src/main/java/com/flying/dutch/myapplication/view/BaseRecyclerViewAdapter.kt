package com.flying.dutch.myapplication.view


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


/**
 * RecyclerView adapter基类
 *
 *
 * 封装了数据集合以及ItemView的点击事件回调,同时暴露 [.onBindData]
 * 用于数据与view绑定
 *
 * @param <T> A data bean class that will be used by the adapter.
 *
 *
 * Created by DavidChen on 2018/5/30.
</T> */
abstract class BaseRecyclerViewAdapter<T>(
    private val mContext: Context,
    private val mData: List<T>,
    private val mLayoutId: Int
) :
    RecyclerView.Adapter<RecyclerViewHolder>(), View.OnClickListener {
    private var mListener: OnItemClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val view = LayoutInflater.from(mContext).inflate(mLayoutId, parent, false)
        view.setOnClickListener(this)
        return RecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.itemView.tag = position
        val bean = mData[position]
        onBindData(holder, bean, position)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onClick(v: View) {
        if (mListener != null) {
            mListener!!.onItemClick(this, v, v.tag as Int)
        }
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener?) {
        mListener = onItemClickListener
    }

    /**
     * 数据绑定，由实现类实现
     *
     * @param holder   The reference of the all view within the item.
     * @param bean     The data bean related to the position.
     * @param position The position to bind data.
     */
    protected abstract fun onBindData(holder: RecyclerViewHolder?, bean: T, position: Int)

    /**
     * item点击监听器
     */
    interface OnItemClickListener {
        /**
         * item点击回调
         *
         * @param adapter  The Adapter where the click happened.
         * @param v        The view that was clicked.
         * @param position The position of the view in the adapter.
         */
        fun onItemClick(adapter: RecyclerView.Adapter<*>?, v: View?, position: Int)
    }
}