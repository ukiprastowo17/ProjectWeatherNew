package com.projectweather.remote.data_forecast

import com.projectweather.remote.data_forecast.ForecastResponse

interface OperationalCallbackForeCast {
    fun onSuccess(forecastResponse: ForecastResponse)
    fun onFailure(message: String)
}