package com.projectweather.remote.data_zipcode

data class ZipcodeResponse(
    val zip: String,
    val name: String,
    val lat: String,
    val lon: String,
    val country: String,
)