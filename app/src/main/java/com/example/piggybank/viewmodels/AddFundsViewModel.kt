package com.example.piggybank.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.piggybank.calculator.Calculator
import com.example.piggybank.dao.IncomeEntity
import com.example.piggybank.repository.ExpensesRepository
import com.example.piggybank.repository.IncomeRepository
import com.example.piggybank.uistates.AddFundsUIState
import com.example.piggybank.viewmodels.common.SavedStateViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddFundsViewModel @Inject constructor(
    private val incomeRepository: IncomeRepository,
    private val expensesRepository: ExpensesRepository,
    private val calculator: Calculator,
) : SavedStateViewModel() {

    private val _uiState = MutableStateFlow(AddFundsUIState())
    val uiState = _uiState.asStateFlow().onEach {
        state -> savedStateHandle[SAVED_STATE_INPUT] = state.keyBoardInput
    }

    private val _navigateToEditIncomesScreenEvent = MutableSharedFlow<Unit>()
    val navigateToEditIncomesScreenEvent = _navigateToEditIncomesScreenEvent.asSharedFlow()

    override fun init(savedStateHandle: SavedStateHandle) {
        super.init(savedStateHandle)
        if(savedStateHandle.contains(SAVED_STATE_INPUT)){
            _uiState.update {
                it.copy(keyBoardInput = savedStateHandle[SAVED_STATE_INPUT]!!)
            }
        }
    }

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
                it.copy(balance = newBalance.toString())
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

    companion object{
        private const val SAVED_STATE_INPUT = "SAVED_STATE_INPUT"
    }
}