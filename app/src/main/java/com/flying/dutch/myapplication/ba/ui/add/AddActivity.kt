package com.flying.dutch.myapplication.ba.ui.add

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.animation.LinearInterpolator
import android.widget.Toast
import com.flying.dutch.myapplication.R
import com.flying.dutch.myapplication.aligeite.BaseActivity
import com.flying.dutch.myapplication.ba.ui.guide.GuideViewModel
import com.flying.dutch.myapplication.ba.ui.main.MainActivity
import com.flying.dutch.myapplication.data.SaveTodoData
import com.flying.dutch.myapplication.data.ToDoBean
import com.flying.dutch.myapplication.databinding.ActivityAddBinding
import com.flying.dutch.myapplication.databinding.ActivityFolsBinding
import com.flying.dutch.myapplication.utils.TodoUtils

class AddActivity : BaseActivity<ActivityAddBinding, AddViewModel>() {

    override fun getLayoutResId(): Int = R.layout.activity_add

    override fun getViewModelClass(): Class<AddViewModel> = AddViewModel::class.java
    private var todoPos = -1
    private var bean: ToDoBean? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
        clickFun()
    }

    private fun initData() {
        todoPos = intent.extras?.getInt("addKey") ?: -1
        binding.colorState = 1
        showActivityData()
    }

    private fun clickFun() {
        binding.imageView.setOnClickListener {
            finish()
        }
        binding.view1.setOnClickListener {
            binding.colorState = 1
        }
        binding.view2.setOnClickListener {
            binding.colorState = 2
        }
        binding.view3.setOnClickListener {
            binding.colorState = 3
        }
        binding.view4.setOnClickListener {
            binding.colorState = 4
        }
        binding.view5.setOnClickListener {
            binding.colorState = 5
        }
        binding.view6.setOnClickListener {
            binding.colorState = 6
        }
        binding.tvSave.setOnClickListener {
            val title = binding.edName.text.toString().trim()
            val description = binding.edNotes.text.toString().trim()
            if (title.isBlank()) {
                Toast.makeText(this, "Please enter title", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (todoPos != -1) {
                bean?.let { it1 -> SaveTodoData.deleteToDoBean(it1) }
            }
            val beanNow = ToDoBean(
                id = System.currentTimeMillis(),
                title = title,
                description = description,
                date = TodoUtils.timestampToDate(System.currentTimeMillis().toString()),
                isFinish = bean?.isFinish?:false,
                level = binding?.colorState!!
            )
            SaveTodoData.saveToDoBean(beanNow)
            finish()
        }
    }

    fun showActivityData() {
        if (todoPos != -1) {
            bean = SaveTodoData.getToDoJsonBean()[todoPos]
            binding.edName.setText(bean?.title)
            binding.edNotes.setText(bean?.description)
            binding.colorState = bean?.level
        }
    }

}