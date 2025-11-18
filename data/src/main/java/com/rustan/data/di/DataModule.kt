package com.rustan.data.di

import com.rustan.data.ApiWeatherService
import com.rustan.data.ApiWeatherServiceImplement
import com.rustan.data.ApiStoryService
import com.rustan.data.StoryRepository
import com.rustan.data.StoryRepositoryImpl
import com.rustan.data.StoryServiceImplement
import com.rustan.data.WeatherRepository
import com.rustan.data.WeatherRepositoryImplement
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val networkModule = module {
    fun provideRetrofit(baseUrl: String, moshi: Moshi, client: OkHttpClient): Retrofit {
        return Retrofit.Builder().baseUrl(baseUrl).client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    fun provideHttpClient(): OkHttpClient {
        return OkHttpClient().newBuilder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }).build()
    }

    fun provideApi(retrofit: Retrofit): ApiWeatherService {
        return retrofit.create(ApiWeatherService::class.java)
    }
    fun provideStoryApi(retrofit: Retrofit): ApiStoryService {
        return retrofit.create(ApiStoryService::class.java)
    }
    single { provideHttpClient() }
    single {
        Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()
    }
    single {
        provideRetrofit(
            baseUrl = "https://api.openweathermap.org/", moshi = get(), client = get()
        )
    }
    single { provideApi(get()) }
    single { provideStoryApi(get()) }
    single { ApiWeatherServiceImplement(get()) }
    single { StoryServiceImplement(get()) }
    single<WeatherRepository> { WeatherRepositoryImplement(get()) }
    single<StoryRepository> { StoryRepositoryImpl(get()) }
}