package com.example.sunnyweather.logic.network

import com.example.sunnyweather.SunnyWeatherApplication
import com.example.sunnyweather.logic.model.PlaceResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PlaceService{
    @GET("v2/city/lookup?key=${SunnyWeatherApplication.TOKEN}")
    fun searchPlaces(@Query("location") query: String): Call<PlaceResponse>
}