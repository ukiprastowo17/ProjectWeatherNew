package com.projectweather.remote.data_forecast

import com.projectweather.remote.data_forecast.Coord

data class City(
    val coord: Coord,
    val country: String,
    val id: Int,
    val name: String,
    val population: Int,
    val timezone: Int
)