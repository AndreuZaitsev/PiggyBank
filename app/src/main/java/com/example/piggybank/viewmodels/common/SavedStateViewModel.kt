package com.example.piggybank.viewmodels.common

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

abstract class SavedStateViewModel : ViewModel() {

    protected lateinit var savedStateHandle: SavedStateHandle

    open fun init(savedStateHandle: SavedStateHandle) {
        this.savedStateHandle = savedStateHandle
    }
}