package com.rustan.domain

import com.rustan.data.WeatherRepository
import com.rustan.domain.entities.Weather
import com.rustan.domain.maper.MapEntities
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException

interface GetWeatherUseCase {
    operator fun invoke(
        latitude: Double,
        longitude: Double
    ): Flow<NetworkResult<Weather>>
}

class GetWeatherUseCaseImpl(
    private val repository: WeatherRepository,
    private val coroutineDispatcherIO: CoroutineDispatcher,
    private val mapEntities: MapEntities
) : GetWeatherUseCase {
    override fun invoke(
        latitude: Double,
        longitude: Double
    ): Flow<NetworkResult<Weather>> {
        return flow {
            runCatching {
                repository.getWeather(latitude = latitude, longitude = longitude)
            }.onSuccess { weatherDTO ->
                val weather = mapEntities.mapWeatherDTOtoWeather(weatherDTO)
                emit(NetworkResult.Success(weather))
            }.onFailure {
                if (it is HttpException) {
                    emit(NetworkResult.Error.ApiError(it.code(), it.message()))
                } else {
                    emit(NetworkResult.Error.Exception(it.message))
                }
            }
        }.flowOn(coroutineDispatcherIO)
    }
}