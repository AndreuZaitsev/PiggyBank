package com.example.piggybank

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        _uiState.update { currentState->
            currentState.copy(
                categories = CategoriesRepository().categories,
                balance = "xer"
            )
        }
    }

    fun onResetClicked(){
        _uiState.update {
            it.copy(keyboardInput = "")
        }
    }

    fun onDeleteClicked(){
        _uiState.update {
            it.copy(keyboardInput = it.keyboardInput.dropLast(1))
        }
    }

    fun onSumClicked(){
        _uiState.update {
            it.copy(keyboardInput = Calculator().calculate(it.keyboardInput).toString())
        }
    }

    fun onKeyClicked(key: String){
        _uiState.update {
            it.copy(keyboardInput = it.keyboardInput + key)
        }
    }
}
