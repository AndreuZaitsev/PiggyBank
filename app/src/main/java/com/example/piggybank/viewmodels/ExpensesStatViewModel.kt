package com.example.piggybank.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class ExpensesStatViewModel: ViewModel() {

    private val _navigateToEditExpensesScreenEvent = MutableSharedFlow<Unit>()
    val navigateToEditExpensesScreenEvent = _navigateToEditExpensesScreenEvent.asSharedFlow()

    fun onEditClicked(){
        viewModelScope.launch {
            _navigateToEditExpensesScreenEvent.emit(Unit)
        }
    }
}