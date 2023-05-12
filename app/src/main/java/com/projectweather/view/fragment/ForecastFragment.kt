package com.projectweather.view.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.projectweather.databinding.FragmentForecastBinding
import com.projectweather.remote.Constant
import com.projectweather.remote.VolleyHandler
import com.projectweather.remote.data_forecast.ForecastResponse
import com.projectweather.presenter.mvp_forecast.ForecastPresenter
import com.projectweather.presenter.mvp_forecast.MVPForecast
import com.projectweather.view.adapter.ForecastAdapter

class ForecastFragment : Fragment(), MVPForecast, MVPForecast.ForecastView {
    private lateinit var binding: FragmentForecastBinding
    private lateinit var forecastPresenter: ForecastPresenter
    private lateinit var sharedPreferences: SharedPreferences
    private var cityName: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentForecastBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initForecastPresenter()
    }

    private fun initForecastPresenter() {
        initSharedPreference()
        forecastPresenter = ForecastPresenter(VolleyHandler(context), this)
        forecastPresenter.getForecast(cityName ?: "London")
    }

    private fun initSharedPreference() {
        sharedPreferences =
            context?.getSharedPreferences(Constant.SHARED_PREF_FILE, Context.MODE_PRIVATE)!!
        cityName = sharedPreferences.getString(Constant.SHARED_PREF_CITY_KEY, "")
    }

    override fun onLoad(isLoading: Boolean) {
        if (isLoading) {
            binding.loader.visibility = View.VISIBLE
        } else {
            binding.loader.visibility = View.GONE
        }
    }

    override fun showError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun setResult(forecastResponse: ForecastResponse) {
        binding.recyclerViewForecast.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewForecast.adapter = ForecastAdapter(forecastResponse.list)

    }

    fun updateLocation(city: String) {
        initSharedPreference()
        forecastPresenter.getForecast(city)
    }
}