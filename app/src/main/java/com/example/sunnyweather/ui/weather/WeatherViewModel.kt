package com.example.sunnyweather.ui.weather

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.sunnyweather.logic.Repository
import com.example.sunnyweather.logic.model.Location

class WeatherViewModel {
    class WeatherViewModel : ViewModel() {
        private val locationLiveData = MutableLiveData<Location>()
        var locationId = ""
        var locationLng = ""
        var locationLat = ""
        var placeName = ""
        val weatherLiveData = Transformations.switchMap(locationLiveData) { location ->
            Repository.refreshWeather(location.id,location.lng, location.lat)
        }
        fun refreshWeather(id: String, lng: String, lat: String) {
            Log.d("test","刷新天气")
            Log.d("test-id",id)
            locationLiveData.value = Location(id,lng, lat)
        }
    }

}