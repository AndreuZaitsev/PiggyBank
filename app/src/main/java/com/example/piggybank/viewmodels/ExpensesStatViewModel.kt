package com.example.piggybank.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class ExpensesStatViewModel @Inject constructor(): ViewModel() {

    private val _navigateToEditExpensesScreenEvent = MutableSharedFlow<Unit>()
    val navigateToEditExpensesScreenEvent = _navigateToEditExpensesScreenEvent.asSharedFlow()

    fun onEditClicked(){
        viewModelScope.launch {
            _navigateToEditExpensesScreenEvent.emit(Unit)
        }
    }
}