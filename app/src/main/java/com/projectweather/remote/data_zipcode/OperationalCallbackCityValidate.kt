package com.projectweather.remote.data_zipcode

import com.projectweather.remote.data_zipcode.CityValidateResponse

interface OperationalCallbackCityValidate {
    fun onSuccess(cityValidateResponse: CityValidateResponse)
    fun onFailure(message: String)
}