package com.rustan.domain.maper

import com.rustan.data.model.WeatherResponseDTO
import com.rustan.domain.entities.Weather
import java.util.Date

class MapEntities {
    fun mapWeatherDTOtoWeather(weatherDTO: WeatherResponseDTO): Weather {
        return Weather(
            lat = weatherDTO.coord?.lat,
            lon = weatherDTO.coord?.lon,
            weatherConditions = weatherDTO.weather?.map { weatherConditionDTO ->
                Weather.WeatherCondition(
                    id = weatherConditionDTO.id,
                    main = weatherConditionDTO.main,
                    description = weatherConditionDTO.description,
                    icon = weatherConditionDTO.icon
                )
            },
            temp = weatherDTO.main?.temp,
            feelsLike = weatherDTO.main?.feelsLike,
            tempMin = weatherDTO.main?.tempMin,
            tempMax = weatherDTO.main?.tempMax,
            pressure = weatherDTO.main?.pressure,
            humidity = weatherDTO.main?.humidity,
            windSpeed = weatherDTO.wind?.speed,
            windDeg = weatherDTO.wind?.deg,
            windGust = weatherDTO.wind?.gust,
            cloudsAll = weatherDTO.clouds?.all,
            sunCountry = weatherDTO.sys?.country,
            sunSunrise = weatherDTO.sys?.sunrise?.let { Date(it * 1000L) },
            sunSet = weatherDTO.sys?.sunset?.let { Date(it * 1000L) },
            name = weatherDTO.name,
            visibility = weatherDTO.visibility,
            timestamp = weatherDTO.timestamp?.let { Date(it * 1000L) }
        )
    }
}
