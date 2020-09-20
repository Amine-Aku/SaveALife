package com.impression.savealife.models

import com.mapbox.geojson.Point


class Place(private var title: String, private var lat: Double, private var lng: Double, private var city: String, var placeName: String) {

    private fun getPoint(): Point{
        return Point.fromLngLat(lng, lat)
    }

    override fun toString(): String {
        return "Place(title='$title', lat=$lat, lng=$lng, city='$city', placeName='$placeName')"
    }

}
