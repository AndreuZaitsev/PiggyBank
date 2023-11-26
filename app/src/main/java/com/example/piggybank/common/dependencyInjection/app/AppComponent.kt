package com.example.piggybank.common.dependencyInjection.app

import com.example.piggybank.common.dependencyInjection.activity.ActivityComponent
import com.example.piggybank.common.dependencyInjection.dispatcher.DispatcherModule
import dagger.Component

@AppScope
@Component(
    modules = [AppModule::class, DispatcherModule::class, DataSourceModule::class]
)
interface AppComponent {

    fun newActivityComponentBuilder(): ActivityComponent.Builder
}