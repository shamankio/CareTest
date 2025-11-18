package com.rustan.data

import com.rustan.data.model.PhotoDTO
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import java.net.URL

interface ApiStoryService {
    @Headers("Authorization: Client-ID McCblZG0wBxB-8axocAtXw1XB0CVtxYBS_iLM4BrvYk")
    @GET("https://api.unsplash.com/photos/random")
    suspend fun getPhotos(
        @Query("query") city: String,
        @Query("orientation") orientation: String,
        @Query("count") count: Int
    ):List<PhotoDTO>
}

class StoryServiceImplement(private val apiStoryService:ApiStoryService){
    suspend fun getPhotos(city: String,  count: Int): List<PhotoDTO> {
      return  apiStoryService.getPhotos(city, "portrait", count)
    }
}