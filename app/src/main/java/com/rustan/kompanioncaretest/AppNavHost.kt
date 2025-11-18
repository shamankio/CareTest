package com.rustan.kompanioncaretest

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.rustan.story.navigation.StoryRoute
import com.rustan.story.navigation.navigateToStory
import com.rustan.story.navigation.storyScreen
import com.rustan.weather.navigation.WeatherRoute
import com.rustan.weather.navigation.weatherScreen

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    isPad: Boolean
) {
    NavHost(
        navController = navController,
        startDestination = WeatherRoute(0.0, 0.0),
        modifier = modifier,
    ) {
        weatherScreen(
            onNavigateToStories = { city -> navController.navigateToStory(city = city)},
            onRetryFetch = {  },
            isPad = isPad
        )
        storyScreen(
            onNavigateBack = {navController.navigateUp()},
        )
    }
}
