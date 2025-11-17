package com.rustan.domain.entities

import java.util.Date

data class Weather(
    val lat: Double?,
    val lon: Double?,
    val weatherConditions: List<WeatherCondition>?,
    val temp: Double?,
    val feelsLike: Double?,
    val tempMin: Double?,
    val tempMax: Double?,
    val pressure: Int?,
    val humidity: Int?,
    val windSpeed: Double?,
    val windDeg: Int?,
    val windGust: Double?,
    val cloudsAll: Int?,
    val sunCountry: String?,
    val sunSunrise: Date?,
    val sunSet: Date?,
    val name: String?,
    val visibility: Int?,
    val timestamp: Date?
) {
    data class WeatherCondition(
        val id: Int?,
        val main: String?,
        val description: String?,
        val icon: String?
    )
}