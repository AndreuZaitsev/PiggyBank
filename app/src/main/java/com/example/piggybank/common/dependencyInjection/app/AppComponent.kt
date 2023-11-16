package com.example.piggybank.common.dependencyInjection.app

import com.example.piggybank.common.dependencyInjection.activity.ActivityComponent
import dagger.Component

@AppScope
@Component(modules = [AppModule::class])
interface AppComponent {

    fun newActivityComponentBuilder(): ActivityComponent.Builder
}