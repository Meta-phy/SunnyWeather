package com.example.sunnyweather.ui.place

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.sunnyweather.R
import com.example.sunnyweather.WeatherActivity
import com.example.sunnyweather.logic.model.Location
import com.example.sunnyweather.logic.model.Place

class PlaceAdapter(private val fragment: Fragment, private val placeList: List<Place>) :
    RecyclerView.Adapter<PlaceAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) { val placeName: TextView = view.findViewById(
        R.id.placeName)
        val placeAddress: TextView = view.findViewById(R.id.placeAddress)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place = placeList[position]
        //对location进行处理
        Log.d("test","创建卡片")
        Log.d("test", place.toString())
        place.location = Location(place.id,place.lon,place.lat)
        //
        holder.placeName.text = place.name
        holder.placeAddress.text = place.country+" "+place.adm1+" "+place.adm2
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.place_item,
            parent, false)
        val holder = ViewHolder(view)
        holder.itemView.setOnClickListener {
            val position = holder.adapterPosition
            val place = placeList[position]
            val intent = Intent(parent.context, WeatherActivity::class.java).apply {
                putExtra("location_lng", place.lon)
                putExtra("location_lat", place.lat)
                putExtra("place_name", place.name)
                putExtra("location_id", place.id)
            }
            fragment.startActivity(intent)
            fragment.activity?.finish()
        }
        return holder
    }
    override fun getItemCount() = placeList.size
}