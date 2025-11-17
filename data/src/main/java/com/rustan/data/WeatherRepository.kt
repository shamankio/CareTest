package com.rustan.data

import com.rustan.data.model.WeatherResponseDTO

const val OpenWeatherAPIKey = "caad2e5e29d9a5690eab278d25af3811"

interface WeatherRepository {
    suspend fun getWeather(latitude: Double, longitude: Double): WeatherResponseDTO
}

class WeatherRepositoryImplement(private val apiWeatherService: ApiWeatherServiceImplement) :
    WeatherRepository {
    override suspend fun getWeather(
        latitude: Double,
        longitude: Double
    ): WeatherResponseDTO {
        return apiWeatherService.getWeather(
            key = OpenWeatherAPIKey,
            latitude = latitude,
            longitude = longitude
        )
    }
}