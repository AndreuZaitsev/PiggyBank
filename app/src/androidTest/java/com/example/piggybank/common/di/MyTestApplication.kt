package com.example.piggybank.common.di

import com.example.piggybank.application.MyApplication
import com.example.piggybank.common.di.app.AppComponent
import com.example.piggybank.common.di.app.AppModule

class MyTestApplication : MyApplication() {

    override fun initComponent(): AppComponent = DaggerTestAppComponent.builder()
        .appModule(AppModule(this))
        .build()
}