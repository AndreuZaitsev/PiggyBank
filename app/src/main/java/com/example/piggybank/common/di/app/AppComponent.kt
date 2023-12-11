package com.example.piggybank.common.di.app

import com.example.piggybank.common.di.activity.ActivityComponent
import com.example.piggybank.common.di.dispatcher.DispatcherModule
import dagger.Component

@AppScope
@Component(
    modules = [
        AppModule::class,
        DispatcherModule::class,
        DataSourceModule::class
    ]
)
interface AppComponent {

    fun newActivityComponentBuilder(): ActivityComponent.Builder
}
