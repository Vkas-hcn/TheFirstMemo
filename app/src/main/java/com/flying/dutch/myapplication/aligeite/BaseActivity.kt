package com.flying.dutch.myapplication.aligeite

import com.flying.dutch.myapplication.utils.StatusBarUtil

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

abstract class BaseActivity<DB : ViewDataBinding, VM : ViewModel> : AppCompatActivity() {

    protected lateinit var binding: DB
    protected lateinit var viewModel: VM

    @LayoutRes
    abstract fun getLayoutResId(): Int

    abstract fun getViewModelClass(): Class<VM>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, getLayoutResId())
        viewModel = ViewModelProvider(this).get(getViewModelClass())
        binding.lifecycleOwner = this

        setupStatusBar()
    }

    private fun setupStatusBar() {
        StatusBarUtil.setTransparent(this)
    }

    inline fun <reified T : AppCompatActivity> navigateToActivity(params: Bundle? = null) {
        val intent = Intent(this, T::class.java)
        params?.let { intent.putExtras(it) }
        startActivity(intent)
    }
}
