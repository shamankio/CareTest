package com.rustan.kompanioncaretest.di

import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.named
import org.koin.dsl.module

val myAppModules = module {
    single(named("dispatcherIO")) { Dispatchers.IO }
    single(named("dispatcherDefault")) { Dispatchers.Default }
    single(named("dispatcherMain")) { Dispatchers.Main }
}