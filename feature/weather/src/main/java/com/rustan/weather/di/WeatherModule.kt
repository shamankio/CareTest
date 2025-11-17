package com.rustan.weather.di

import com.rustan.weather.WeatherViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val weatherFeatureModule = module {
    viewModel { WeatherViewModel(get()) }
}