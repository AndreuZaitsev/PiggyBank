package com.example.piggybank.application

import android.app.Application
import com.example.piggybank.BuildConfig
import com.example.piggybank.common.di.app.AppComponent
import com.example.piggybank.common.di.app.AppModule
import com.example.piggybank.common.di.app.DaggerAppComponent
import timber.log.Timber.DebugTree
import timber.log.Timber.Forest.plant

open class MyApplication : Application() {

    val appComponent: AppComponent by lazy {
        initComponent()
    }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            plant(DebugTree())
        }
    }

    open fun initComponent(): AppComponent = DaggerAppComponent.builder()
        .appModule(AppModule(this))
        .build()
}
