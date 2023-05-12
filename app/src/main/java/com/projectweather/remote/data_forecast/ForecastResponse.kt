package com.projectweather.remote.data_forecast

import com.projectweather.remote.data_forecast.Forecast

data class ForecastResponse(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<Forecast>,
    val message: Double
)