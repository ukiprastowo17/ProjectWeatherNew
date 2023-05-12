package com.projectweather.view.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import java.util.*
import androidx.fragment.app.Fragment
import com.projectweather.R
import com.projectweather.databinding.FragmentWeatherBinding
import com.projectweather.remote.Constant.ABSOLUTE_ZERO
import com.projectweather.remote.Constant.SHARED_PREF_CITY_KEY
import com.projectweather.remote.Constant.SHARED_PREF_FILE
import com.projectweather.remote.VolleyHandler
import com.projectweather.remote.data_weather.WeatherResponse
import com.projectweather.presenter.mvp_weather.MVPWeather
import com.projectweather.presenter.mvp_weather.WeatherPresenter
import com.projectweather.view.util.TimeUtil
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

class WeatherFragment : Fragment(), MVPWeather.WeatherView {
    private lateinit var binding: FragmentWeatherBinding
    private lateinit var presenter: MVPWeather.WeatherPresenter
    private lateinit var sharedPreferences: SharedPreferences
    private var cityName: String? = null
    private var sunsetValue: Long? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentWeatherBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSharedPreference()
        presenter = WeatherPresenter(VolleyHandler(this.context), this)
        presenter.getWeatherData(cityName ?: getString(R.string.default_city))
    }

    private fun initSharedPreference() {
        sharedPreferences =
            context?.getSharedPreferences(SHARED_PREF_FILE, Context.MODE_PRIVATE)!!
        cityName = sharedPreferences.getString(SHARED_PREF_CITY_KEY, "")
    }

    override fun setResult(weatherResponse: WeatherResponse) {
        val currentDate = {
            LocalDateTime
                .now()
                .format(DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy"))
        }
        val tempConverter = { a: Double -> (a - ABSOLUTE_ZERO).roundToInt() }

        binding.apply {
            weatherResponse.apply {
                city.text = cityName
                date.text = currentDate().toString()
                temperature.text = tempConverter(main.temp).toString()
                weatherDescription.text = weather[0].main
                minTemperature.text = "Min Temp : " + tempConverter(main.temp_min).toString() + " C"
                maxTemperature.text = "Max Temp : " + tempConverter(main.temp_max).toString() + " C"
                txtHuimdPercentage.text = main.humidity.toString() + "%"
                txtWind.text = wind.speed.toString() + "km/h"
                pressure.text = main.pressure.toString() + "hPa"
                sunrise.text = TimeUtil.getDateTime(sys.sunrise.toString())
                sunset.text = TimeUtil.getDateTime(sys.sunset.toString())
                info.text = clouds.all.toString()

                Log.d("datasun", sys.sunrise.toString() + "-" + sys.sunset.toString())


            }
        }
    }

    fun updateLocation(city: String) {
        initSharedPreference()
        presenter.getWeatherData(city)
    }

    override fun onLoad(isLoading: Boolean) {
        if (isLoading) {
            Toast.makeText(this.context, getString(R.string.loading), Toast.LENGTH_SHORT).show()
        }
    }

    override fun showError(message: String) {
        Toast.makeText(this.context, message, Toast.LENGTH_SHORT).show()
    }
}