package com.rustan.data

import com.rustan.data.model.WeatherResponseDTO
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiWeatherService {
    @GET("data/2.5/weather?units=metric")
    suspend fun getWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") key: String
    ): WeatherResponseDTO
}
class ApiWeatherServiceImplement(private val apiWeatherService: ApiWeatherService){
    suspend fun getWeather(latitude: Double, longitude: Double, key: String): WeatherResponseDTO {
        return apiWeatherService.getWeather(latitude, longitude, key)
    }

}