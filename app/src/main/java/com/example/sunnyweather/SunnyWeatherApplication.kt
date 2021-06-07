package com.example.sunnyweather

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

//在项目的任何位置通过调用 SunnyWeatherApplication.context 来获取Context对象

class SunnyWeatherApplication :Application() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        const val TOKEN = "d2fb7e6dc0924935afe6ae03883021f5"

    }
    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }

}