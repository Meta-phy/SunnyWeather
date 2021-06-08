package com.example.sunnyweather.logic

import android.util.Log
import androidx.lifecycle.liveData
import com.example.sunnyweather.logic.model.Weather
import com.example.sunnyweather.logic.network.SunnyWeatherNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlin.coroutines.CoroutineContext

object Repository {
    fun searchPlaces(query: String) = fire(Dispatchers.IO) {
        val placeResponse = SunnyWeatherNetwork.searchPlaces(query)
        if (placeResponse.code == "200") {
            val places = placeResponse.location
            Result.success(places)
        } else {
            Result.failure(RuntimeException("response status is ${placeResponse.code}"))
        }
    }

    fun refreshWeather(locationId: String, lng: String, lat: String) = fire(Dispatchers.IO) {
        coroutineScope {
            val deferredRealtime = async {
                SunnyWeatherNetwork.getRealtimeWeather(locationId)
            }
            val deferredDaily = async {
                SunnyWeatherNetwork.getDailyWeather(locationId)
            }
            val realtimeResponse = deferredRealtime.await()
            val dailyResponse = deferredDaily.await()
            //测试输出
            Log.d("test-ID",locationId)
            Log.d("test-realtime",realtimeResponse.toString())
            Log.d("test-daily",dailyResponse.toString())


            if (realtimeResponse.code == "200" && dailyResponse.code == "200") {
                val weather = Weather(
                    realtimeResponse.now,
                    dailyResponse.daily
                )
                Result.success(weather)
            } else {
                Result.failure(
                    RuntimeException(
                        "realtime response status is ${realtimeResponse.code}" +
                                "daily response status is ${dailyResponse.code}"
                    )
                )
            }
        }
    }
    private fun <T> fire(context: CoroutineContext, block: suspend () -> Result<T>) =
        liveData<Result<T>>(context) {
            val result = try {
                block()
            } catch (e: Exception) {
                Result.failure<T>(e)
            }
            emit(result)
        }
}
