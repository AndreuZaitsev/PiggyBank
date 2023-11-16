package com.example.piggybank.fragments.common

import androidx.fragment.app.Fragment
import com.example.piggybank.activity.BaseActivity
import com.example.piggybank.common.dependencyInjection.presentation.PresentationComponent
import com.example.piggybank.common.dependencyInjection.presentation.PresentationModule

open class BaseFragment(layoutResId: Int) : Fragment(layoutResId) {

    private val presentationComponent: PresentationComponent by lazy {
        (requireActivity() as BaseActivity).activityComponent.newPresentationComponent(PresentationModule(this))
    }

    protected val injector get() = presentationComponent
}