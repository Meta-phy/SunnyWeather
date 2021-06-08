package com.example.sunnyweather.logic.model

import com.google.gson.annotations.SerializedName

data class DailyResponse(val code: String, val daily: List<Daily>) {

    data class Daily(
        val tempMax: String,//最高温度
        val tempMin: String,//最低温度
        val textDay: String,//天气状况
        val winDirDay: String,//风向
        val windScaleDay: String,//风力
        val windSpeedDay: String,//风速
        val humidity: String,//相对湿度
        val iconDay: String,
        val fxDate:String//日期
    )
}