package com.rustan.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rustan.domain.GetWeatherUseCase
import com.rustan.domain.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WeatherViewModel(val getWeatherUseCase: GetWeatherUseCase) : ViewModel() {
    private val _weatherScreenState: MutableStateFlow<WeatherScreenState> = MutableStateFlow(
        WeatherScreenState.Loading
    )
    val weatherScreenState: StateFlow<WeatherScreenState> = _weatherScreenState.asStateFlow()
    fun fetchData(
        latitude: Double,
        longitude: Double
    ) {
        viewModelScope.launch {
            getWeatherUseCase(latitude, longitude).collectLatest { result ->
                when (result) {
                    is NetworkResult.Success -> {
                        _weatherScreenState.update {
                            WeatherScreenState.Success(
                                WeatherState(
                                    weather = result.data,
                                    showStories = false
                                )
                            )
                        }
                    }

                    is NetworkResult.Error.ApiError -> {
                        _weatherScreenState.update {
                            WeatherScreenState.Error(
                                WeatherError.Api
                            )
                        }
                    }

                    is NetworkResult.Error.Exception -> {
                        _weatherScreenState.update {
                            WeatherScreenState.Error(
                                WeatherError.Unknown
                            )
                        }
                    }
                }
            }
        }
    }
}