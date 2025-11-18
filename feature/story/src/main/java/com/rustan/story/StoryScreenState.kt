package com.rustan.story

import androidx.compose.runtime.Stable
import com.rustan.domain.entities.Story
import com.rustan.domain.entities.Weather


@Stable
data class StoryViewState(
    val city: String,
    val stories: List<Story> = emptyList(),
    val currentIndex: Int = 0,
    val progress: Double = 0.0
)
sealed interface StoryError {
    data object Api: StoryError
    data object NetworkFailure: StoryError
    data object Unknown: StoryError
}

sealed interface StoryScreenState {
    data class Success(val storyViewState: StoryViewState) : StoryScreenState
    data class Error (val error : StoryError) : StoryScreenState
    data object Loading : StoryScreenState
}
sealed interface StoryEvent {
    data object Previous: StoryEvent
    data object Next: StoryEvent
}
