package com.rustan.story.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.rustan.story.StoryScreen
import com.rustan.story.StoryScreenView
import kotlinx.serialization.Serializable

@Serializable
data class StoryRoute(
    val city: String,
)

fun NavController.navigateToStory(city: String,  navOptions: NavOptions? = null) {
    navigate(StoryRoute(city = city), navOptions)
}

fun NavGraphBuilder.storyScreen(
    onNavigateBack: () -> Unit
) {
    composable<StoryRoute> { backStackEntry ->
        val storyRoute: StoryRoute = backStackEntry.toRoute()
        StoryScreenView(
            city = storyRoute.city,
            onNavigateBack = onNavigateBack,
        )
    }
}
