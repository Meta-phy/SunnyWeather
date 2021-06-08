package com.example.sunnyweather.logic.model

data class Weather(val realtime: RealtimeResponse.Now, val daily: List<DailyResponse.Daily>)