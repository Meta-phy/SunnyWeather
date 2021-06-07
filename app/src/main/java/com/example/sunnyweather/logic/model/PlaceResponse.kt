package com.example.sunnyweather.logic.model

import com.google.gson.annotations.SerializedName

data class PlaceResponse(val code: String, val location: List<Place>)
data class Place(
    val name: String, val location: Location,val lat: String ,val lon: String,val country: String,val adm1: String,val adm2: String,
    @SerializedName("formatted_address") val address: String
)

data class Location(val lng: String, val lat: String)