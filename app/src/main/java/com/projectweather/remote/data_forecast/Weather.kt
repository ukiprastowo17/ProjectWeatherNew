package com.projectweather.remote.data_forecast

data class Weather(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)