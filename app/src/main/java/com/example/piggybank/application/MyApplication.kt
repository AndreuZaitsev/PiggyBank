package com.example.piggybank.application

import android.app.Application
import com.example.piggybank.common.dependencyInjection.app.AppComponent
import com.example.piggybank.common.dependencyInjection.app.AppModule
import com.example.piggybank.common.dependencyInjection.app.DaggerAppComponent

class MyApplication : Application() {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }

    override fun onCreate() {
        super.onCreate()
    }
}
