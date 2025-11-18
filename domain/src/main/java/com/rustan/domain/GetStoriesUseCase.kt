package com.rustan.domain

import com.rustan.data.StoryRepository
import com.rustan.domain.entities.Story
import com.rustan.domain.maper.MapEntities
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException

interface GetStoriesUseCase {
    operator fun invoke(city: String, count: Int): Flow<NetworkResult<List<Story>>>
}

class GetStoriesUseCaseImpl(
    private val repository: StoryRepository,
    private val coroutineDispatcherIO: CoroutineDispatcher,
    private val mapEntities: MapEntities
) : GetStoriesUseCase {
    override fun invoke(
        city: String,
        count: Int
    ): Flow<NetworkResult<List<Story>>> {
        return flow {
            runCatching {
                repository.getPhotos(city = city, count = count)
            }.onSuccess { urls ->
                val stories = urls.map { mapEntities.mapPhotoDTOToStory(it) }
                emit(NetworkResult.Success(stories))
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
