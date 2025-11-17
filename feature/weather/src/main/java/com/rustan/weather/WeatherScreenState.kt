package com.rustan.weather

import androidx.compose.runtime.Stable
import com.rustan.domain.entities.Weather

@Stable
data class WeatherState(
    val weather: Weather?,
    val showStories: Boolean,
)

sealed interface WeatherScreenState {
    data class Success(val weatherState: WeatherState) : WeatherScreenState
    data class Error (val weatherError:WeatherError) : WeatherScreenState
    data object Loading : WeatherScreenState
    data object Empty : WeatherScreenState
}
sealed interface WeatherError {
    data object Api: WeatherError
    data object NetworkFailure: WeatherError
//    data object PermissionDenied: WeatherError
    data object Unknown: WeatherError
}