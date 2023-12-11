package com.example.piggybank.common.di

import com.example.piggybank.common.di.app.AppComponent
import com.example.piggybank.common.di.app.AppModule
import com.example.piggybank.common.di.app.AppScope
import com.example.piggybank.common.di.dispatcher.DispatcherModule
import dagger.Component

@AppScope
@Component(
    modules = [
        AppModule::class,
        DispatcherModule::class,
        TestDataSourceModule::class
    ]
)
interface TestAppComponent : AppComponent
