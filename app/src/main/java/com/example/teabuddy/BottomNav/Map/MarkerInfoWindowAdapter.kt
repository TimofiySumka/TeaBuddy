package com.example.teabuddy.BottomNav.Map

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import com.example.teabuddy.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.google.android.libraries.places.api.model.Place

class MarkerInfoWindowAdapter(private val context: Context) : GoogleMap.InfoWindowAdapter {

    override fun getInfoWindow(marker: Marker): View {
        // Создаем кастомное представление для всего окна информации
        val view = LayoutInflater.from(context).inflate(R.layout.marker_info_contents, null)
        val title: TextView = view.findViewById(R.id.text_view_title)
        val address: TextView = view.findViewById(R.id.text_view_address)
        val ratingBar: RatingBar = view.findViewById(R.id.ratingBarMap)

        val data = marker.tag as? TeahouseModel
        Log.d("MarkerInfoWindowAdapter", "Rating: ${data?.rating}")

        if (data != null) {
            title.text = data.title
            address.text = data.address
            ratingBar.rating = data.rating
        }

        return view
    }

    override fun getInfoContents(marker: Marker): View? {
        return null
    }
}

