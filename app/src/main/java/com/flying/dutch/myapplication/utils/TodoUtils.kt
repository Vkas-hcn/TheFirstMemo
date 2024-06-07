package com.flying.dutch.myapplication.utils

import android.annotation.SuppressLint
import com.flying.dutch.myapplication.R
import com.flying.dutch.myapplication.data.ToDoBean
import com.flying.dutch.myapplication.dul.App

object TodoUtils {
    fun getStateBgColor(state: Int): Int {
        return when (state) {
            1 -> {
                R.color.bg_color_1
            }

            2 -> {
                R.color.bg_color_2
            }

            3 -> {
                R.color.bg_color_3
            }

            4 -> {
                R.color.bg_color_4
            }

            5 -> {
                R.color.bg_color_5
            }

            6 -> {
                R.color.bg_color_6
            }

            else -> R.color.bg_color_1
        }
    }

    fun getStateColor(state: Int): Int {
        return when (state) {
            1 -> {
                R.color.color_1
            }

            2 -> {
                R.color.color_2
            }

            3 -> {
                R.color.color_3
            }

            4 -> {
                R.color.color_4
            }

            5 -> {
                R.color.color_5
            }

            6 -> {
                R.color.color_6
            }

            else -> R.color.color_1
        }
    }

    fun getStateDisCheckImg(state: Int): Int {
        return when (state) {
            1 -> {
                R.drawable.icon_status_1
            }

            2 -> {
                R.drawable.icon_status_2
            }

            3 -> {
                R.drawable.icon_status_3
            }

            4 -> {
                R.drawable.icon_status_4
            }

            5 -> {
                R.drawable.icon_status_5
            }

            6 -> {
                R.drawable.icon_status_6
            }

            else -> R.drawable.icon_status_1
        }
    }

    fun getStateCheckImg(state: Int): Int {
        return when (state) {
            1 -> {
                R.drawable.icon_status_1_1
            }

            2 -> {
                R.drawable.icon_status_2_1
            }

            3 -> {
                R.drawable.icon_status_3_1
            }

            4 -> {
                R.drawable.icon_status_4_1
            }

            5 -> {
                R.drawable.icon_status_5_1
            }

            6 -> {
                R.drawable.icon_status_6_1
            }

            else -> R.drawable.icon_status_1_1
        }
    }

    fun getStateDateImg(state: Int): Int {
        return when (state) {
            1 -> {
                R.drawable.icon_date_1
            }

            2 -> {
                R.drawable.icon_date_2
            }

            3 -> {
                R.drawable.icon_date_3
            }

            4 -> {
                R.drawable.icon_date_4
            }

            5 -> {
                R.drawable.icon_date_5
            }

            6 -> {
                R.drawable.icon_date_6
            }

            else -> R.drawable.icon_date_1
        }
    }

    fun getInitToDoBeanList(): MutableList<ToDoBean> {
        return mutableListOf(
            ToDoBean(
                11111,"Welcome to ${App.instance?.getString(R.string.app_name)}",
                "",
                timestampToDate(),
                false,
                1
            ),
            ToDoBean(22222,"This is an easy ToDo List.", "", timestampToDate(), false, 2),
            ToDoBean(33333,"Add tasks quickly and mark them done.", "", timestampToDate(), false, 3),
            ToDoBean(44444,"Let's start organizing efficiently!", "", timestampToDate(), false, 4),
        )
    }

    //时间戳转日期
    @SuppressLint("SimpleDateFormat")
    fun timestampToDate(timestamp: String? = null): String {
        val date = java.util.Date(timestamp?.toLong() ?: System.currentTimeMillis())
        val format = java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
        return format.format(date)
    }
}