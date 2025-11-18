package com.rustan.domain.di

import com.rustan.domain.GetStoriesUseCase
import com.rustan.domain.GetStoriesUseCaseImpl
import com.rustan.domain.GetWeatherUseCase
import com.rustan.domain.GetWeatherUseCaseImpl
import com.rustan.domain.maper.MapEntities
import org.koin.core.qualifier.named
import org.koin.dsl.module

val domainModule = module {
    factory { MapEntities() }
    factory<GetWeatherUseCase> {
        GetWeatherUseCaseImpl(
            get(), get(named("dispatcherIO")), get()
        )
    }
    factory<GetStoriesUseCase> {
        GetStoriesUseCaseImpl(get(), get(named("dispatcherIO")), get())
    }
}