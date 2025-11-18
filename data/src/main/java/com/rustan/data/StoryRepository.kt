package com.rustan.data

import com.rustan.data.model.PhotoDTO
import java.net.URL

interface StoryRepository {
    suspend fun getPhotos(city: String, count: Int): List<PhotoDTO>
}

class StoryRepositoryImpl(private val storyServiceImplement: StoryServiceImplement) :
    StoryRepository {
    override suspend fun getPhotos(city: String, count: Int): List<PhotoDTO> {
        return storyServiceImplement.getPhotos(city, count)
    }
}