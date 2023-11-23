package com.example.piggybank.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.piggybank.calculator.Calculator
import com.example.piggybank.dao.IncomeEntity
import com.example.piggybank.repository.ExpensesRepository
import com.example.piggybank.repository.IncomeRepository
import com.example.piggybank.uistates.AddFundsUIState
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddFundsViewModel @Inject constructor(
    private val incomeRepository: IncomeRepository,
    private val expensesRepository: ExpensesRepository,
    private val calculator: Calculator,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddFundsUIState())
    val uiState: StateFlow<AddFundsUIState> = _uiState.asStateFlow()

    private val _navigateToEditIncomesScreenEvent = MutableSharedFlow<Unit>()
    val navigateToEditIncomesScreenEvent = _navigateToEditIncomesScreenEvent.asSharedFlow()

    fun onKeyClicked(key: String) {
        _uiState.update {
            it.copy(keyBoardInput = it.keyBoardInput + key)
        }
    }

    fun onResetClicked() {
        _uiState.update {
            it.copy(keyBoardInput = "")
        }
    }

    fun onDeleteClicked() {
        _uiState.update {
            it.copy(keyBoardInput = it.keyBoardInput.dropLast(1))
        }
    }

    fun onSumClicked() {
        _uiState.update {
            it.copy(keyBoardInput = it.keyBoardInput.calculateInput().toString())
        }
    }

    fun onEnterClicked() {
        viewModelScope.launch {
            val income = IncomeEntity(
                System.currentTimeMillis(),
                _uiState.value.keyBoardInput.calculateInput()
            )
            saveIncome(income)
            loadBalance()
        }
    }

    fun loadBalance() {
        viewModelScope.launch {
            _uiState.update {
                val newBalance = incomeRepository.getSumIncomes() - expensesRepository.getSumExpenses()
                it.copy(balance = newBalance.toString(), keyBoardInput = "")
            }
        }
    }

    private fun String.calculateInput(): Double = calculator.calculate(this)

    private suspend fun saveIncome(income: IncomeEntity) {
        incomeRepository.saveIncomeValue(income)
    }

    fun onEditClicked() {
        viewModelScope.launch {
            _navigateToEditIncomesScreenEvent.emit(Unit)
        }
    }
}