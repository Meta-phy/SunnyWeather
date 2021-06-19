package com.example.sunnyweather.logic

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.liveData
import com.example.sunnyweather.MyDatabaseHelper
import com.example.sunnyweather.SunnyWeatherApplication
import com.example.sunnyweather.logic.dao.PlaceDao
import com.example.sunnyweather.logic.model.Place
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
            //place加入数据库
            val dbHelper = MyDatabaseHelper(SunnyWeatherApplication.context, "sunnyweather.db", 2)
            dbHelper.writableDatabase//创建数据库
            val db = dbHelper.writableDatabase
            for (place in places){
                val values = ContentValues().apply { // 开始组装place数据
                    put("id", place.id)
                    put("name", place.name)
                    put("country", place.country)
                    put("adm1", place.adm1)
                    put("adm2", place.adm2)
                }
                db.insert("Place", null, values) // 插入数据
            }
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

                //weather插入数据库
                val dbHelper = MyDatabaseHelper(SunnyWeatherApplication.context, "sunnyweather.db", 2)
                dbHelper.writableDatabase//创建数据库
                val db = dbHelper.writableDatabase
                val values = ContentValues().apply { // 开始组装place数据
                    put("id",locationId)
                    put("temp", realtimeResponse.now.temp)
                    put("text", realtimeResponse.now.text)
                }
                db.insert("Weather", null, values) // 插入第一条数据

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

    fun savePlace(place: Place) = PlaceDao.savePlace(place)
    fun getSavedPlace() = PlaceDao.getSavedPlace()
    fun isPlaceSaved() = PlaceDao.isPlaceSaved()
}
