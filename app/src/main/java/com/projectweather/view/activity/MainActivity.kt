package com.projectweather.view.activity

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.projectweather.R
import com.projectweather.database.DatabaseHelper
import com.projectweather.database.WeatherCityDao
import com.projectweather.databinding.ActivityMainBinding
import com.projectweather.remote.Constant
import com.projectweather.remote.VolleyHandler
import com.projectweather.remote.data_zipcode.CityValidateResponse
import com.projectweather.remote.data_zipcode.ZipcodeResponse
import com.projectweather.presenter.mvp_zipcode.MVPZipCode
import com.projectweather.presenter.mvp_zipcode.ZipcodePresenter
import com.projectweather.view.fragment.DashboardFragment

class MainActivity : AppCompatActivity(), MVPZipCode.ZipcodeView {

    private lateinit var binding: ActivityMainBinding
    private lateinit var zipcodePresenter: ZipcodePresenter
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var weatherCityDao: WeatherCityDao
    private lateinit var sharedPreferences: SharedPreferences

    private val dashboardFragment by lazy {
        binding.dashboardFragment.getFragment<DashboardFragment>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initSharedPreference()
        initTopAddCityButton()
        initZipcodePresenter()
        initDatabase()
    }

    private fun initTopAddCityButton() {
        binding.btnAddCity.setOnClickListener {
            closeKeyboard(binding.inputCity)
            binding.apply {
                binding.cityLabel.error = null
                zipcodePresenter.getCityValidateData(binding.inputCity.text.toString())
            }
        }
    }

    private fun initSharedPreference() {
        sharedPreferences =
            this.getSharedPreferences(Constant.SHARED_PREF_FILE, Context.MODE_PRIVATE)
        checkThemeAndSetTheme(
            sharedPreferences.getLong(Constant.SHARED_PREF_CITY_SUNSET, 990909),
            sharedPreferences.getLong(Constant.SHARED_PREF_CITY_TIME_NOW, 990909)
        )
    }

    private fun initDatabase() {
        dbHelper = DatabaseHelper(this.applicationContext)
        weatherCityDao = WeatherCityDao(dbHelper)
    }

    private fun initZipcodePresenter() {
        zipcodePresenter = ZipcodePresenter(VolleyHandler(this), this)
    }

    private fun closeKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(view.windowToken, 0)
    }

//    private fun initBottomCityWidgetDlg() {
//        bottomCityWidgetDlg = BottomSheetDialog(this)
//        bindingCityWidget = AddCityWidgetBinding.inflate(layoutInflater)
//        bottomCityWidgetDlg.setContentView(bindingCityWidget.root)
//        bindingCityWidget.btnGetCity.setOnClickListener {
//            closeKeyboard(bindingCityWidget.inputZipCode)
//            zipcodePresenter.getZipcodeData(
//                bindingCityWidget.inputZipCode.text.toString(),
//                bindingCityWidget.spinnerCountry.selectedItem.toString()
//            )
//
//        }
//        bindingCityWidget.btnAddCity.setOnClickListener {
//            closeKeyboard(bindingCityWidget.inputCity)
//            bindingCityWidget.apply {
//                bindingCityWidget.cityLabel.error = null
//                zipcodePresenter.getCityValidateData(bindingCityWidget.inputCity.text.toString())
//            }
//        }
//        val spinnerAdapter =
//            ArrayAdapter(this, android.R.layout.simple_list_item_1, Constant.COUNTRY_CODE)
//        bindingCityWidget.spinnerCountry.adapter = spinnerAdapter
//        val sharedCityValue = sharedPreferences.getString(Constant.SHARED_PREF_CITY_KEY, "")
//        bindingCityWidget.inputCity.setText(sharedCityValue)
//    }

    override fun onLoad(isLoading: Boolean) {
        binding.inputCity.setText(getString(R.string.loading))
    }

    override fun showError(message: String) {
        binding.inputCity.setText("")
        binding.cityLabel.error = getString(R.string.invalid_city)
    }

    override fun setResult(zipcodeResponse: ZipcodeResponse) {
        binding.inputCity.setText(zipcodeResponse.name)
        addCityInfo(zipcodeResponse)
    }

    override fun setCityValidateResult(cityValidateResponse: CityValidateResponse) =
        with(cityValidateResponse) {
            binding.inputCity.setText(cityValidateResponse.name)
            if (cod == "200") {
                val zipcodeResponse = ZipcodeResponse(
                    "",
                    name,
                    coord.lat,
                    coord.lon,
                    sys.country
                )
                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                editor.putLong(
                    Constant.SHARED_PREF_CITY_SUNSET,
                    cityValidateResponse.sys.sunset.toLong()
                )
                editor.putLong(
                    Constant.SHARED_PREF_CITY_TIME_NOW,
                    dt.toLong()
                )
                editor.apply()
                checkThemeAndSetTheme(
                    sys.sunset.toLong(),
                    dt.toLong()
                )
                addCityInfo(zipcodeResponse)
            } else {
                Toast.makeText(
                    this@MainActivity,
                    message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    private fun checkThemeAndSetTheme(timeSunset: Long, timeNow: Long) {
        if (timeSunset > timeNow) {
            // it means it is day
            binding.containerMain.setBackgroundResource(R.drawable.theme_day)
        } else {
            // it is night
            binding.containerMain.setBackgroundResource(R.drawable.theme_night)
        }
    }

    private fun addCityInfo(zipcodeResponse: ZipcodeResponse) = with(zipcodeResponse) {
        weatherCityDao.addCity(zipcodeResponse)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(Constant.SHARED_PREF_CITY_KEY, name)
        editor.putString(Constant.SHARED_PREF_CITY_LAT, lat)
        editor.putString(Constant.SHARED_PREF_CITY_LON, lon)
        editor.apply()
        Toast.makeText(this@MainActivity, "City added!", Toast.LENGTH_SHORT).show()


        dashboardFragment.weatherFragment.updateLocation(name)
        dashboardFragment.forecastFragment.updateLocation(name)
    }
}