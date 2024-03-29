package com.example.piggybank.activity

import androidx.appcompat.app.AppCompatActivity
import com.example.piggybank.application.MyApplication
import com.example.piggybank.common.di.activity.ActivityComponent
import com.example.piggybank.common.di.app.AppComponent
import com.example.piggybank.common.di.presentation.PresentationModule

open class BaseActivity : AppCompatActivity() {

    private val appComponent: AppComponent
        get() = (application as MyApplication).appComponent

    val activityComponent: ActivityComponent by lazy {
        appComponent.newActivityComponentBuilder()
            .activity(this)
            .build()
    }

    private val presentationComponent by lazy {
        activityComponent.newPresentationComponent(PresentationModule(this))
    }

    protected val injector get() = presentationComponent
}