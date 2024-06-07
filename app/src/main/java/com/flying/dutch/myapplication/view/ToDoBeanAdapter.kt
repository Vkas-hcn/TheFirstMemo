package com.flying.dutch.myapplication.view


import android.content.Context
import android.graphics.Paint
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.flying.dutch.myapplication.R
import com.flying.dutch.myapplication.data.ToDoBean
import com.flying.dutch.myapplication.utils.TodoUtils



class ToDoBeanAdapter(
    context: Context?,
    private var data: MutableList<ToDoBean>,
    private var isToDo: Boolean
) : BaseRecyclerViewAdapter<ToDoBean?>(context!!, data, R.layout.item_to_do) {

    private var mDeleteClickListener: OnDeleteClickLister? = null
    private var mCheckClickListener: OnCheckClickListener? = null
    private var mItemClickListener: OnItemClickListener? = null
    private var context: Context = context!!

    override fun onBindData(holder: RecyclerViewHolder?, bean: ToDoBean?, position: Int) {
        val view = holder!!.getView(R.id.deleteButton)
        view!!.tag = position
        if (!view.hasOnClickListeners()) {
            view.setOnClickListener { v ->
                if (mDeleteClickListener != null) {
                    mDeleteClickListener!!.onDeleteClick(v, v.tag as Int)
                }
            }
        }

        setVisibility(isToDo, bean?.isFinish!!, holder.itemView)

        val tvTitle = holder.getView(R.id.tv_title) as TextView?
        val tvDate = holder.getView(R.id.tv_date) as TextView?
        val imgCheck = holder.getView(R.id.img_check) as ImageView?
        val checkImage = if (bean.isFinish) {
            tvTitle?.paint?.flags = Paint.STRIKE_THRU_TEXT_FLAG
            TodoUtils.getStateCheckImg(bean.level)
        } else {
            tvTitle?.paint?.flags = 0
            TodoUtils.getStateDisCheckImg(bean.level)
        }

        (holder.getView(R.id.con_item) as ConstraintLayout)?.setBackgroundColor(ContextCompat.getColor(context, TodoUtils.getStateBgColor(bean.level)))
        imgCheck?.setImageResource(checkImage)
        (holder.getView(R.id.icon_date) as ImageView)?.setImageResource(TodoUtils.getStateDateImg(bean.level))
        tvTitle?.text = bean.title
        tvDate?.text = bean.date
        tvTitle?.setTextColor(ContextCompat.getColor(context, TodoUtils.getStateColor(bean.level)))
        tvDate?.setTextColor(ContextCompat.getColor(context, TodoUtils.getStateColor(bean.level)))
        imgCheck?.setOnClickListener {
            mCheckClickListener?.onCheckClick(holder.itemView, position)
        }

        holder.itemView.setOnClickListener {
            mItemClickListener?.onItemClick(holder.itemView, position)
        }
    }

    private fun setVisibility(isToDo: Boolean, isVisible: Boolean, itemView: View) {
        val param = itemView.layoutParams as RecyclerView.LayoutParams
        if (isToDo && isVisible) {
            itemView.visibility = View.GONE
            param.height = 0
            param.width = 0
            itemView.setPadding(0, 0, 0, 0)
        }
        if (isToDo && !isVisible) {
            param.height = LinearLayout.LayoutParams.WRAP_CONTENT
            param.width = LinearLayout.LayoutParams.MATCH_PARENT
            itemView.visibility = View.VISIBLE
        }
        if (!isToDo && isVisible) {
            param.height = LinearLayout.LayoutParams.WRAP_CONTENT
            param.width = LinearLayout.LayoutParams.MATCH_PARENT
            itemView.visibility = View.VISIBLE
        }
        if (!isToDo && !isVisible) {
            itemView.visibility = View.GONE
            param.height = 0
            param.width = 0
            itemView.setPadding(0, 0, 0, 0)
        }
        itemView.layoutParams = param
    }

    fun setOnDeleteClickListener(listener: OnDeleteClickLister?) {
        mDeleteClickListener = listener
    }

    fun setOnCheckClickListener(listener: OnCheckClickListener?) {
        mCheckClickListener = listener
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        mItemClickListener = listener
    }

    interface OnDeleteClickLister {
        fun onDeleteClick(view: View?, position: Int)
    }

    interface OnCheckClickListener {
        fun onCheckClick(view: View?, position: Int)
    }

    interface OnItemClickListener {
        fun onItemClick(view: View?, position: Int)
    }

    fun refreshData(newData: MutableList<ToDoBean>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }

    fun updateIsToDo(newIsToDo: Boolean) {
        this.isToDo = newIsToDo
        notifyDataSetChanged()
    }
}
