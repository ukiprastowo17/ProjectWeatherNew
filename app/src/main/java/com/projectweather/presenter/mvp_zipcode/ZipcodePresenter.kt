package com.projectweather.presenter.mvp_zipcode

import com.projectweather.remote.VolleyHandler
import com.projectweather.remote.data_zipcode.CityValidateResponse
import com.projectweather.remote.data_zipcode.OperationalCallbackCityValidate
import com.projectweather.remote.data_zipcode.OperationalCallbackZipCode
import com.projectweather.remote.data_zipcode.ZipcodeResponse

class ZipcodePresenter(
    private val volleyHandler: VolleyHandler,
    private val zipCodeView: MVPZipCode.ZipcodeView
): MVPZipCode.ZipcodePresenter {
    override fun getZipcodeData(zipCode: String, countryCode: String) {
        zipCodeView.onLoad(true)
        volleyHandler.getZipCodeData(zipCode, countryCode, object : OperationalCallbackZipCode {
            override fun onSuccess(zipcodeResponse: ZipcodeResponse) {
                zipCodeView.onLoad(false)
                zipCodeView.setResult(zipcodeResponse)
            }

            override fun onFailure(message: String) {
                zipCodeView.showError(message)
            }
        })
    }

    override fun getCityValidateData(city: String) {
        zipCodeView.onLoad(true)
        volleyHandler.getCityValidateData(city, object : OperationalCallbackCityValidate {
            override fun onSuccess(cityValidateResponse: CityValidateResponse) {
                zipCodeView.onLoad(false)
                zipCodeView.setCityValidateResult(cityValidateResponse)
            }

            override fun onFailure(message: String) {
                zipCodeView.showError(message)
            }
        })
    }
}