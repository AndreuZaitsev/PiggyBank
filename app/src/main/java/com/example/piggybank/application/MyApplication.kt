package com.example.piggybank.application

import android.app.Application
import com.example.piggybank.BuildConfig
import com.example.piggybank.common.dependencyInjection.app.AppComponent
import com.example.piggybank.common.dependencyInjection.app.AppModule
import com.example.piggybank.common.dependencyInjection.app.DaggerAppComponent
import timber.log.Timber.*
import timber.log.Timber.Forest.plant

class MyApplication : Application() {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            plant(DebugTree())
        }
    }
}
