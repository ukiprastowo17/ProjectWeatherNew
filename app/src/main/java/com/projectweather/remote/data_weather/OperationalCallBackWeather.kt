package com.projectweather.remote.data_weather

interface OperationalCallBackWeather {
    fun onSuccess(weatherResponse: WeatherResponse)
    fun onFailure(message: String)
}