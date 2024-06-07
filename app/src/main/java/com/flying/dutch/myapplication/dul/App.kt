package com.flying.dutch.myapplication.dul

import android.app.Application
import com.flying.dutch.myapplication.data.SaveTodoData
import com.flying.dutch.myapplication.utils.TodoUtils
import com.google.gson.Gson
import java.util.UUID

class App : Application() {
    companion object {
        var instance: App? = null
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        instance = this
        if (SaveTodoData.todo_id.isEmpty()) {
            SaveTodoData.todo_id = UUID.randomUUID().toString()
        }
        SaveTodoData.getBlackList(this)
        if(SaveTodoData.todo_json_data.isEmpty()){
            SaveTodoData.todo_json_data = Gson().toJson(TodoUtils.getInitToDoBeanList())
        }
    }
}