package com.example.sunnyweather.logic.model

import com.google.gson.annotations.SerializedName

//按照JSON格式来定义相应的数据模型
data class RealtimeResponse(val code: String, val now: Now) {
    data class Now(
        val temp: String,//温度
        val feelsLike: String,//体感温度
        val text: String,//天气状况
        val winDir: String,//风向
        val windScale: String,//风力
        val windSpeed: String,//风速
        val humidity: String,//相对湿度
        val pressure: String,//大气压强
        val vis: String,//能见度
        val icon: String//图标

    )
}