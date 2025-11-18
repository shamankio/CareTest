package com.rustan.weather.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.rustan.weather.WeatherScreen
import kotlinx.serialization.Serializable

@Serializable
data class WeatherRoute(
    val lat: Double,
    val lon: Double
)

fun NavController.navigateToWeather(lat: Double, lon: Double, navOptions: NavOptions? = null) {
    navigate(WeatherRoute(lat = lat, lon = lon), navOptions)
}

fun NavGraphBuilder.weatherScreen(
    onNavigateToStories: (String) -> Unit,
    onRetryFetch: () -> Unit,
    isPad: Boolean = false
) {
    composable<WeatherRoute> { backStackEntry ->
        val weatherRoute: WeatherRoute = backStackEntry.toRoute()
        WeatherScreen(
            onNavigateToStories = onNavigateToStories,
            onRetryFetch = onRetryFetch,
            isPad = isPad,
            latitude = weatherRoute.lat,
            longitude = weatherRoute.lon
        )
    }
}
