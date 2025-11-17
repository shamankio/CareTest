package com.rustan.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WeatherResponseDTO(
    @Json(name = "coord") val coord: CoordDTO?,
    @Json(name = "weather") val weather: List<WeatherConditionDTO>?,
    @Json(name = "main") val main: MainDTO?,
    @Json(name = "wind") val wind: WindDTO?,
    @Json(name = "clouds") val clouds: CloudsDTO?,
    @Json(name = "sys") val sys: SysDTO?,
    @Json(name = "name") val name: String?,
    @Json(name = "visibility") val visibility: Int?,
    @Json(name = "dt") val timestamp: Int?
)

@JsonClass(generateAdapter = true)
data class CoordDTO(
    @Json(name = "lat") val lat: Double?,
    @Json(name = "lon") val lon: Double?
)

@JsonClass(generateAdapter = true)
data class WeatherConditionDTO(
    @Json(name = "id") val id: Int?,
    @Json(name = "main") val main: String?,
    @Json(name = "description") val description: String?,
    @Json(name = "icon") val icon: String?
)

@JsonClass(generateAdapter = true)
data class MainDTO(
    @Json(name = "temp") val temp: Double?,
    @Json(name = "feels_like") val feelsLike: Double?,
    @Json(name = "temp_min") val tempMin: Double?,
    @Json(name = "temp_max") val tempMax: Double?,
    @Json(name = "pressure") val pressure: Int?,
    @Json(name = "humidity") val humidity: Int?
)

@JsonClass(generateAdapter = true)
data class WindDTO(
    @Json(name = "speed") val speed: Double?,
    @Json(name = "deg") val deg: Int?,
    @Json(name = "gust") val gust: Double?
)

@JsonClass(generateAdapter = true)
data class CloudsDTO(
    @Json(name = "all") val all: Int? // Cloudiness percentage
)

@JsonClass(generateAdapter = true)
data class SysDTO(
    @Json(name = "country") val country: String?,
    @Json(name = "sunrise") val sunrise: Int?,
    @Json(name = "sunset") val sunset: Int?
)