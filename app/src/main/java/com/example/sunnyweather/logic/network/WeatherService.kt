package com.example.sunnyweather.logic.network


import com.example.sunnyweather.SunnyWeatherApplication
import com.example.sunnyweather.logic.model.DailyResponse
import com.example.sunnyweather.logic.model.RealtimeResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WeatherService {

    @GET("v7/weather/now?key=${SunnyWeatherApplication.TOKEN}")
    fun getRealtimeWeather(@Query("location") id: String):
            Call<RealtimeResponse>

    @GET("v7/weather/3d?key=${SunnyWeatherApplication.TOKEN}")
    fun getDailyWeather(@Query("location") id: String):
            Call<DailyResponse>

}