package com.flying.dutch.myapplication.data

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.util.Log
import com.flying.dutch.myapplication.dul.App
import com.flying.dutch.myapplication.utils.TodoUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

object SaveTodoData {

    const val todoBlackNetString = "https://pate.doitrightnow.link/aldermen/runic/canteen"
    private val sorP by lazy {
        App.instance?.getSharedPreferences(
            "TFM",
            Context.MODE_PRIVATE
        )
    }
    var todo_json_data = ""
        set(value) {
            sorP!!.edit().run {
                putString("todo_json_data", value)
                commit()
            }
            field = value
        }
        get() = sorP?.getString("todo_json_data", "").toString()

    var todo_clock = ""
        set(value) {
            sorP!!.edit().run {
                putString("todo_clock", value)
                commit()
            }
            field = value
        }
        get() = sorP?.getString("todo_clock", "").toString()
    var todo_id = ""
        set(value) {
            sorP!!.edit().run {
                putString("todo_id", value)
                commit()
            }
            field = value
        }
        get() = sorP?.getString("todo_id", "").toString()


    fun getToDoJsonBean(): MutableList<ToDoBean> {
        val beanType = object : TypeToken<MutableList<ToDoBean>>() {}.type
        val pressureList = runCatching {
            Gson().fromJson<MutableList<ToDoBean>>(todo_json_data, beanType)
        }.getOrNull() ?: TodoUtils.getInitToDoBeanList()
        pressureList.sortByDescending { it.date }
        return pressureList
    }

    fun saveToDoBean(bean: ToDoBean) {
        val list = getToDoJsonBean()
        list.add(0, bean)
        todo_json_data = Gson().toJson(list)
    }

    fun deleteToDoBean(bean: ToDoBean) {
        val list = getToDoJsonBean()
        list.remove(bean)
        todo_json_data = Gson().toJson(list)
    }

    fun modifyToDoBean(bean: ToDoBean) {
        val list = getToDoJsonBean()
        val index = list.indexOfFirst { it.id == bean.id }
        if (index >= 0) {
            list[index] = bean
            todo_json_data = Gson().toJson(list)
        }
    }

    private fun cloakMapData(context: Context): Map<String, Any> {
        return mapOf<String, Any>(
            //bundle_id
            "prune" to ("com.doitrightnow.taskreminder"),
            //os
            "roast" to "w",
            //app_version
            "tilt" to context.getAppVersion(),
            //distinct_id
            "slob" to (todo_id),
            //client_ts
            "puppet" to (System.currentTimeMillis()),
            //device_model
            "settle" to Build.MODEL,
            //os_version
            "aspect" to Build.VERSION.RELEASE,
            //gaid
            "shipman" to "",
            //android_id
            "texas" to context.getAppId(),
        )
    }

    private fun Context.getAppVersion(): String {
        try {
            val packageInfo = this.packageManager.getPackageInfo(this.packageName, 0)

            return packageInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return "Version information not available"
    }

    @SuppressLint("HardwareIds")
    private fun Context.getAppId(): String {
        return Settings.Secure.getString(
            this.contentResolver,
            Settings.Secure.ANDROID_ID
        )
    }

    fun getBlackList(context: Context) {
        if (todo_clock.isNotEmpty()) {
            return
        }
        GlobalScope.launch(Dispatchers.IO) {
            Log.e("TAG", "The blacklist ：${cloakMapData(context)}")

            cloakMapData(context).postMapData(todoBlackNetString , onNext = {
                Log.e("TAG", "The blacklist request is successful：$it")
                todo_clock = it
            }, onError = {
                GlobalScope.launch(Dispatchers.IO) {
                    delay(10006)
                    Log.e("TAG", "The blacklist request failed：$it")
                    getBlackList(context)
                }
            })
        }
    }

    private fun Map<String, Any>.getMapData(
        url: String,
        onNext: (response: String) -> Unit,
        onError: (error: String) -> Unit
    ) {
        val queryParameters = StringBuilder()
        for ((key, value) in this) {
            if (queryParameters.isNotEmpty()) {
                queryParameters.append("&")
            }
            queryParameters.append(URLEncoder.encode(key, "UTF-8"))
            queryParameters.append("=")
            queryParameters.append(URLEncoder.encode(value.toString(), "UTF-8"))
        }

        val urlString = if (url.contains("?")) {
            "$url&$queryParameters"
        } else {
            "$url?$queryParameters"
        }

        val url = URL(urlString)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.connectTimeout = 14000

        try {
            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val inputStream = connection.inputStream
                val reader = BufferedReader(InputStreamReader(inputStream))
                val response = StringBuilder()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    response.append(line)
                }
                reader.close()
                inputStream.close()
                onNext(response.toString())
            } else {
                onError("HTTP error: $responseCode")
            }
        } catch (e: Exception) {
            onError("Network error: ${e.message}")
        } finally {
            connection.disconnect()
        }
    }

    private fun Map<String, Any>.postMapData(
        url: String,
        onNext: (response: String) -> Unit,
        onError: (error: String) -> Unit
    ) {
        val jsonParams = JSONObject(this).toString()

        val url = URL(url)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8")
        connection.doOutput = true

        try {
            val outputStream = connection.outputStream
            outputStream.write(jsonParams.toByteArray())
            outputStream.flush()

            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val inputStream = connection.inputStream
                val reader = BufferedReader(InputStreamReader(inputStream))
                val response = StringBuilder()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    response.append(line)
                }
                reader.close()
                inputStream.close()
                onNext(response.toString())
            } else {
                onError("HTTP error: $responseCode")
            }
        } catch (e: Exception) {
            onError("Network error: ${e.message}")
        } finally {
            connection.disconnect()
        }
    }

}