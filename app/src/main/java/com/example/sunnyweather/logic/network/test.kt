package com.example.sunnyweather.logic.network

suspend fun main(){
    var res = SunnyWeatherNetwork.searchPlaces("佛山")
    print(res)
}