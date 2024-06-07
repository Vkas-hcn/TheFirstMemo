package com.flying.dutch.myapplication.ba.ui.main

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.flying.dutch.myapplication.R
import com.flying.dutch.myapplication.aligeite.BaseActivity
import com.flying.dutch.myapplication.ba.ui.add.AddActivity
import com.flying.dutch.myapplication.data.SaveTodoData
import com.flying.dutch.myapplication.data.ToDoBean
import com.flying.dutch.myapplication.databinding.ActivityMainBinding
import com.flying.dutch.myapplication.utils.TodoUtils
import com.flying.dutch.myapplication.view.SpaceItemDecoration
import com.flying.dutch.myapplication.view.ToDoBeanAdapter


class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {

    override fun getLayoutResId(): Int = R.layout.activity_main

    override fun getViewModelClass(): Class<MainViewModel> = MainViewModel::class.java
    private lateinit var toDoBeanAdapter: ToDoBeanAdapter
    private lateinit var toDoBeanAdapterList: MutableList<ToDoBean>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        clickFUn()
        setAdapter()
        setToDoAndCompletedCount()
    }

    private fun updateCanShow() {
        if (binding?.canTodo!!) {
            binding.canShow = toDoBeanAdapterList.any { !it.isFinish }
        } else {
            binding.canShow = toDoBeanAdapterList.any { it.isFinish }
        }
    }
    @SuppressLint("SetTextI18n")
    private fun setAdapter() {
        binding.textView2.text = "Welcome to ${this.getString(R.string.app_name)}"
        binding?.canTodo = true
        toDoBeanAdapterList = SaveTodoData.getToDoJsonBean()
        updateCanShow()
        toDoBeanAdapter = ToDoBeanAdapter(this, toDoBeanAdapterList, binding?.canTodo!!)
        binding.rvTodo.adapter = toDoBeanAdapter
        binding.rvTodo.layoutManager = LinearLayoutManager(this)
        val spacingInPixels = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, 6f, resources.displayMetrics
        ).toInt()
        binding.rvTodo.addItemDecoration(SpaceItemDecoration(spacingInPixels))
        toDoBeanAdapter.setOnDeleteClickListener(object : ToDoBeanAdapter.OnDeleteClickLister {
            override fun onDeleteClick(view: View?, position: Int) {
                showDeleteDialog(position)
                binding.rvTodo.closeMenu()
            }
        })
        toDoBeanAdapter.setOnCheckClickListener(object : ToDoBeanAdapter.OnCheckClickListener {
            override fun onCheckClick(view: View?, position: Int) {
                clickCheck(position)
            }
        })

        toDoBeanAdapter.setOnItemClickListener(object : ToDoBeanAdapter.OnItemClickListener {
            override fun onItemClick(view: View?, position: Int) {
                toJumpAddActivity(position)
            }
        })
    }

    fun clickCheck(position: Int) {
        val bean = toDoBeanAdapterList[position]
        bean.isFinish = !bean.isFinish
        bean.date = TodoUtils.timestampToDate(System.currentTimeMillis().toString())
        SaveTodoData.modifyToDoBean(bean)
        toDoBeanAdapter.refreshData(SaveTodoData.getToDoJsonBean())
        updateCanShow()
        setToDoAndCompletedCount()
    }

    private fun toJumpAddActivity(position: Int) {
        val bundle = Bundle().apply {
            putInt("addKey", position)
        }
        navigateToActivity<AddActivity>(bundle)
    }

    private fun setToDoAndCompletedCount() {
        val completedCount = toDoBeanAdapterList.count { it.isFinish }
        binding.tvProgressValue.text = "${completedCount}/${toDoBeanAdapterList.size}"
        binding.pbFff.progress =
            ((completedCount / toDoBeanAdapterList.size.toFloat()) * 100).toInt()
    }

    private fun clickFUn() {
        binding.constraintLayout.setOnClickListener {
            toJumpAddActivity(-1)
        }
        binding.tvTodo.setOnClickListener {
            binding?.canTodo = true
            updateCanShow()
            toDoBeanAdapter.updateIsToDo(binding?.canTodo!!)
        }
        binding.tvCompleted.setOnClickListener {
            binding?.canTodo = false
            updateCanShow()
            toDoBeanAdapter.updateIsToDo(binding?.canTodo!!)
        }
        binding.imgPrivacy.setOnClickListener {
            val url = "https://www.baidu.com"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }
    }

    fun showDeleteDialog(position: Int) {
        AlertDialog.Builder(this)
            .setTitle("Tip")
            .setMessage("Are you sure to delete it")
            .setPositiveButton("OK") { _, _ ->
                SaveTodoData.deleteToDoBean(toDoBeanAdapterList[position])
                toDoBeanAdapter.refreshData(SaveTodoData.getToDoJsonBean())
                updateCanShow()
                setToDoAndCompletedCount()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onResume() {
        super.onResume()
        toDoBeanAdapter.refreshData(SaveTodoData.getToDoJsonBean())
        setToDoAndCompletedCount()
        updateCanShow()
    }
}
