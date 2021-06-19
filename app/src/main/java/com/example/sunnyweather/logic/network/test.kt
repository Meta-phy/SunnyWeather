package com.example.sunnyweather.logic.network

suspend fun main(){
    var res = SunnyWeatherNetwork.searchPlaces("佛山")
    println(res)
    var test = SunnyWeatherNetwork.getRealtimeWeather("101280803")
    println(test)
    var t = SunnyWeatherNetwork.getDailyWeather("101280803")
    println(t)

}