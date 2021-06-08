package com.example.sunnyweather

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.sunnyweather.logic.model.Weather
import com.example.sunnyweather.logic.model.getSky
import com.example.sunnyweather.ui.weather.WeatherViewModel
import kotlinx.android.synthetic.main.activity_weather.*
import kotlinx.android.synthetic.main.forecast.*
import kotlinx.android.synthetic.main.life_index.*
import kotlinx.android.synthetic.main.now.*
import java.util.*

class WeatherActivity : AppCompatActivity() {
    val viewModel by lazy { ViewModelProvider(this).get(WeatherViewModel.WeatherViewModel::class.java) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)
        if (viewModel.locationLng.isEmpty()) {
            viewModel.locationLng = intent.getStringExtra("location_lng") ?: ""
        }
        if (viewModel.locationLat.isEmpty()) {
            viewModel.locationLat = intent.getStringExtra("location_lat") ?: ""
        }
        if (viewModel.placeName.isEmpty()) {
            viewModel.placeName = intent.getStringExtra("place_name") ?: ""
        }
        if (viewModel.locationId.isEmpty()) {
            viewModel.locationId = intent.getStringExtra("location_id") ?: ""
        }
        Log.d("test","获取数据")
        viewModel.weatherLiveData.observe(this, Observer { result ->
            val weather = result.getOrNull()
            Log.d("test", weather.toString())
            if (weather != null) {
                showWeatherInfo(weather)
            } else {
                Toast.makeText(this, "无法成功获取天气信息", Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
        })
        viewModel.refreshWeather(viewModel.locationId,viewModel.locationLng, viewModel.locationLat)
    }
    private fun showWeatherInfo(weather: Weather) {
        placeName.text = viewModel.placeName
        val realtime = weather.realtime
        val daily = weather.daily
        // 填充now.xml布局中的数据
        val currentTempText = "${realtime.temp.toInt()} ℃"
        currentTemp.text = currentTempText
        currentSky.text = realtime.text
//        val currentPM25Text = "空气指数 ${realtime.airQuality.aqi.chn.toInt()}"
        val currentPM25Text = "和风天气无空气指数"
        currentAQI.text = currentPM25Text
        nowLayout.setBackgroundResource(getSky(realtime.icon).bg)
        // 填充forecast.xml布局中的数据
        forecastLayout.removeAllViews()
        val days = daily.size
        for (i in 0 until days) {
            val skycon = daily[i].iconDay
            val tempMax = daily[i].tempMax
            val tempMin = daily[i].tempMin
            val date = daily[i].fxDate
            val view = LayoutInflater.from(this).inflate(R.layout.forecast_item,
                forecastLayout, false)
            val dateInfo = view.findViewById(R.id.dateInfo) as TextView
            val skyIcon = view.findViewById(R.id.skyIcon) as ImageView
            val skyInfo = view.findViewById(R.id.skyInfo) as TextView
            val temperatureInfo = view.findViewById(R.id.temperatureInfo) as TextView
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            dateInfo.text = date
            val sky = getSky(skycon)
            skyIcon.setImageResource(sky.icon)
            skyInfo.text = daily[i].textDay
            val tempText = "${tempMin.toInt()} ~ ${tempMax.toInt()} ℃"
            temperatureInfo.text = tempText
            forecastLayout.addView(view)
        }
        // 填充life_index.xml布局中的数据
//        val lifeIndex = daily.lifeIndex
        val lifeIndex = 99
        coldRiskText.text = realtime.humidity + "%"
        dressingText.text =  realtime.pressure + " hPa"
        ultravioletText.text =  realtime.vis + " km"
        carWashingText.text =  realtime.feelsLike + " ℃"
        weatherLayout.visibility = View.VISIBLE
    }
}