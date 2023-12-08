package com.example.piggybank.common.dependencyInjection.presentation

import androidx.savedstate.SavedStateRegistryOwner
import com.example.piggybank.ColorfulPalletGenerator
import com.example.piggybank.ColorfulPalletGenerator.*
import dagger.Module
import dagger.Provides

@Module
class PresentationModule(private val savedStateRegistryOwner: SavedStateRegistryOwner) {

    @Provides
    fun savedStateRegistryOwner() = savedStateRegistryOwner

    @Provides
    fun colorProvider(impl: ColorProvider): IColorProvider = impl
}