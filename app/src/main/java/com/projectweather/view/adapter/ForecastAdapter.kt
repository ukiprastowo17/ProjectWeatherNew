package com.projectweather.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.projectweather.databinding.ForecastItemBinding
import com.projectweather.remote.Constant.ABSOLUTE_ZERO
import com.projectweather.remote.Constant.IMG_URL
import com.projectweather.remote.data_forecast.Forecast
import com.projectweather.view.util.TimeUtil

class ForecastAdapter(private var listOfForecast: List<Forecast>) :
    RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder>() {
    private lateinit var binding: ForecastItemBinding

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ForecastViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        binding = ForecastItemBinding.inflate(layoutInflater, parent, false)
        return ForecastViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        holder.bind(listOfForecast[position])
    }

    override fun getItemCount() = listOfForecast.size

    inner class ForecastViewHolder(private val view: ForecastItemBinding) :
        RecyclerView.ViewHolder(view.root) {
        private lateinit var textViewDay: TextView

        fun bind(forecast: Forecast) {
            textViewDay = binding.txtDay
            forecast.apply {
                view.txtDay.text = TimeUtil.dayStringFormat(listOfForecast[position].dt_txt)
                view.txtHour.text = TimeUtil.hourStringFormat(listOfForecast[position].dt_txt)
                Glide.with(this@ForecastViewHolder.itemView.context)
                    .load("${IMG_URL}/${forecast.weather[0].icon}.png")
                    .into(binding.imgForecast)
                binding.txtForecastDegree.text =
                    Math.round(forecast.main.temp - ABSOLUTE_ZERO).toString() + "°"
            }
        }
    }
}