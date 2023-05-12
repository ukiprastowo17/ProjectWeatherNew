package com.projectweather.presenter.mvp_forecast

import com.projectweather.remote.VolleyHandler
import com.projectweather.remote.data_forecast.ForecastResponse
import com.projectweather.remote.data_forecast.OperationalCallbackForeCast

class ForecastPresenter (
    private val volleyHandler: VolleyHandler,
    private val forecastView: MVPForecast.ForecastView)
    : MVPForecast.ForecastPresenter{

    override fun getForecast(city: String) {
        forecastView.onLoad(true)
        volleyHandler.getForecastData(city, object :OperationalCallbackForeCast{
            override fun onSuccess(forecastResponse: ForecastResponse) {
                forecastView.onLoad(false)
                forecastView.setResult(forecastResponse)
            }

            override fun onFailure(message: String) {
                forecastView.onLoad(false)
                forecastView.showError(message)
            }
        })
    }
}