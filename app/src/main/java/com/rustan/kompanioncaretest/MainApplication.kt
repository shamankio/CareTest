package com.rustan.kompanioncaretest

import android.app.Application
import com.rustan.data.di.networkModule
import com.rustan.domain.di.domainModule
import com.rustan.kompanioncaretest.di.myAppModules
import com.rustan.story.di.storyFeatureModule
import com.rustan.weather.di.weatherFeatureModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        GlobalContext.startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(
                myAppModules,
                networkModule,
                domainModule,
                weatherFeatureModule,
                storyFeatureModule
            )
        }
    }
}