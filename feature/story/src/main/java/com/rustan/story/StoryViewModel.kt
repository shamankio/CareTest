package com.rustan.story

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rustan.domain.GetStoriesUseCase
import com.rustan.domain.NetworkResult
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StoryViewModel(val getStoriesUseCase: GetStoriesUseCase) : ViewModel() {
    val fetchRandomPhotoNb = 5
    private var progressJob: Job? = null

    private val _storyScreenState: MutableStateFlow<StoryScreenState> = MutableStateFlow(
        StoryScreenState.Loading
    )
    val storyScreenState: StateFlow<StoryScreenState> = _storyScreenState.asStateFlow()

    fun loadStories(city: String) {
        viewModelScope.launch {
            getStoriesUseCase(city = city, count = fetchRandomPhotoNb).collectLatest { result ->
                when (result) {
                    is NetworkResult.Success -> {
                        _storyScreenState.update {
                            StoryScreenState.Success(
                                StoryViewState(
                                    city = city,
                                    stories = result.data
                                )
                            )
                        }
                        startProgressTimer() // Start timer for the first story
                    }

                    is NetworkResult.Error.ApiError -> {
                        _storyScreenState.update {
                            StoryScreenState.Error(
                                StoryError.Api
                            )
                        }
                    }

                    is NetworkResult.Error.Exception -> {
                        _storyScreenState.update {
                            StoryScreenState.Error(
                                StoryError.Unknown
                            )
                        }
                    }
                }
            }
        }
    }

    fun onEvent(event: StoryEvent) {
        when (event) {
            StoryEvent.Next -> {
                _storyScreenState.update { currentState ->
                    if (currentState !is StoryScreenState.Success) {
                        return@update currentState
                    }
                    val currentViewState = currentState.storyViewState
                    var mCurrentIndex = 0
                    if (currentViewState.currentIndex < currentViewState.stories.size - 1) {
                        mCurrentIndex = currentViewState.currentIndex + 1
                    }
                    currentState.copy(
                        storyViewState = currentViewState.copy(
                            currentIndex = mCurrentIndex,
                            progress = 0.0
                        )
                    )
                }
            }

            StoryEvent.Previous -> {
                _storyScreenState.update { currentState ->
                    if (currentState !is StoryScreenState.Success) {
                        return@update currentState
                    }
                    val currentViewState = currentState.storyViewState
                    var mCurrentIndex = 0
                    if (currentViewState.currentIndex > 0) {
                        mCurrentIndex = currentViewState.currentIndex - 1
                    }
                    currentState.copy(
                        storyViewState = currentViewState.copy(
                            currentIndex = mCurrentIndex,
                            progress = 0.0
                        )
                    )
                }
            }
        }
    }

    private fun startProgressTimer() {
        progressJob?.cancel()
        progressJob = viewModelScope.launch {
            val duration = 3000L
            val interval = 50L
            var progress = 0.0

            while (progress < 1.0) {
                delay(interval)
                progress += interval.toDouble() / duration.toDouble()
                _storyScreenState.update { currentState ->
                    if (currentState !is StoryScreenState.Success) {
                        return@update currentState
                    }
                    if (progress >= 1.0) {
                        progress = 0.0
                        onEvent(StoryEvent.Next)
                    }
                    currentState.copy(
                        storyViewState = currentState.storyViewState.copy(progress = progress)
                    )
                }
            }
        }
    }
}
