package com.flying.dutch.myapplication.ba.ui.guide

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.animation.LinearInterpolator
import androidx.activity.addCallback
import com.flying.dutch.myapplication.R
import com.flying.dutch.myapplication.aligeite.BaseActivity
import com.flying.dutch.myapplication.ba.ui.main.MainActivity
import com.flying.dutch.myapplication.ba.ui.main.MainViewModel
import com.flying.dutch.myapplication.databinding.ActivityFolsBinding
import com.flying.dutch.myapplication.databinding.ActivityMainBinding

class GuideActivity : BaseActivity<ActivityFolsBinding, GuideViewModel>() {

    override fun getLayoutResId(): Int = R.layout.activity_fols

    override fun getViewModelClass(): Class<GuideViewModel> = GuideViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startCountdown()
        onBackPressedDispatcher.addCallback(this) {

        }
    }

    private fun startCountdown() {
        val animator = ValueAnimator.ofInt(0, 100)
        animator.duration = 2000
        animator.interpolator = LinearInterpolator()
        animator.addUpdateListener { animation ->
            val progress = animation.animatedValue as Int
            binding.pbFff.progress = progress
        }
        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                val bundle = Bundle().apply {
                    putString("key", "value")
                }
                navigateToActivity<MainActivity>(bundle)
                finish()
            }
        })
        animator.start()
    }
}