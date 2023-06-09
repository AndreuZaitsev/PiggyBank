package com.example.piggybank.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.piggybank.R
import com.example.piggybank.adapters.CategoryItem
import com.example.piggybank.application.DataBaseHolder
import com.example.piggybank.calculator.Calculator
import com.example.piggybank.repository.CategoriesRepository
import com.example.piggybank.repository.IncomeRepository
import com.example.piggybank.uistates.MainUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    private val _navigateToCategoryCreationEvent = MutableSharedFlow<Unit>()
    val navigateToCategoryCreationEvent = _navigateToCategoryCreationEvent.asSharedFlow()

    private val _navigateToAddFundsEvent = MutableSharedFlow<Unit>()
    val navigateToAddFundsEvent = _navigateToAddFundsEvent.asSharedFlow()

    private val repository = CategoriesRepository(DataBaseHolder.dataBase.categoryDao())
    private val incomeRepository = IncomeRepository(DataBaseHolder.dataBase.incomeDao())

    fun showCategories() {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    categories = loadCategoryItems(),
                    balance = loadBalance()
                )
            }
        }
    }

    fun onResetClicked() {
        _uiState.update {
            it.copy(keyboardInput = "")
        }
    }

    fun onDeleteClicked() {
        _uiState.update {
            it.copy(keyboardInput = it.keyboardInput.dropLast(1))
        }
    }

    fun onSumClicked() {
        _uiState.update {
            it.copy(keyboardInput = Calculator().calculate(it.keyboardInput).toString())
        }
    }

    fun onKeyClicked(key: String) {
        _uiState.update {
            it.copy(keyboardInput = it.keyboardInput + key)
        }
    }

    fun onCategoryClicked(categoryItem: CategoryItem) {
        viewModelScope.launch {
            if (categoryItem.iconRes == R.drawable.ic_add) {
                _navigateToCategoryCreationEvent.emit(Unit)
            }
        }
    }

    fun onAddBalanceClicked() {
        viewModelScope.launch {
            _navigateToAddFundsEvent.emit(Unit)
        }
    }

    private suspend fun loadCategoryItems(): List<CategoryItem> {
        val categoryItems: MutableList<CategoryItem> = repository
            .getCategories()
            .map {
                CategoryItem(it.name, it.iconResId, false)
            }
            .toMutableList()

        categoryItems.add(CategoryItem("add", R.drawable.ic_add, false))
        return categoryItems
    }

    private suspend fun loadBalance(): String {
        return incomeRepository.getSumIncomes().toString()
    }
}
