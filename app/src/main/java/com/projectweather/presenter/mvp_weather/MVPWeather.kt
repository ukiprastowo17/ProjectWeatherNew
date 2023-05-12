package com.projectweather.presenter.mvp_weather

import com.projectweather.remote.data_weather.WeatherResponse

interface MVPWeather {
    interface WeatherPresenter{
        fun getWeatherData(city: String)
    }
    interface WeatherView{
        fun setResult(weatherResponse: WeatherResponse)
        fun onLoad(isLoading: Boolean)
        fun showError(message: String)
    }
}