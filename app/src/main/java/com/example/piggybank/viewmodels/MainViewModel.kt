package com.example.piggybank.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.piggybank.CategoriesPrepopulate
import com.example.piggybank.R
import com.example.piggybank.adapters.CategoryItem
import com.example.piggybank.calculator.Calculator
import com.example.piggybank.dao.ExpenseEntity
import com.example.piggybank.repository.CategoriesRepository
import com.example.piggybank.repository.ExpensesRepository
import com.example.piggybank.repository.IncomeRepository
import com.example.piggybank.uistates.MainUiState
import com.example.piggybank.viewmodels.common.SavedStateViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel @Inject constructor(
    private val repository: CategoriesRepository,
    private val incomeRepository: IncomeRepository,
    private val expenseRepository: ExpensesRepository,
    private val categoriesPrepopulate: CategoriesPrepopulate,
    private val calculator: Calculator,
) : SavedStateViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState = _uiState.asStateFlow()
        .onEach { state -> savedStateHandle[SAVED_STATE_INPUT] = state.keyboardInput }

    private val _navigateToCategoryCreationEvent = MutableSharedFlow<Unit>()
    val navigateToCategoryCreationEvent = _navigateToCategoryCreationEvent.asSharedFlow()

    private val _navigateToAddFundsEvent = MutableSharedFlow<Unit>()
    val navigateToAddFundsEvent = _navigateToAddFundsEvent.asSharedFlow()

    private val _navigateToExpensesStatEvent = MutableSharedFlow<Unit>()
    val navigateToExpensesStatEvent = _navigateToExpensesStatEvent.asSharedFlow()

    private val _showErrorEvent = MutableSharedFlow<String>()
    val showErrorEvent = _showErrorEvent.asSharedFlow()

    init {
        viewModelScope.launch {
            categoriesPrepopulate.prepopulate()
            reloadState()
        }
    }

    override fun init(savedStateHandle: SavedStateHandle) {
        super.init(savedStateHandle)
        if (savedStateHandle.contains(SAVED_STATE_INPUT)) {
            _uiState.update {
                it.copy(keyboardInput = savedStateHandle[SAVED_STATE_INPUT]!!)
            }
        }
    }

    fun reloadState() {
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
            } else {
                _uiState.update {
                    it.copy(categories = it.categories.map { item ->
                        val isOn = if (item == categoryItem) !item.isSelected else false
                        //                        val isOn2 =
                        //                            if (item == categoryItem) {
                        //                                if (item.isSelected) {
                        //                                    false
                        //                                } else {
                        //                                    true
                        //                                }
                        //                            } else {
                        //                                false
                        //                            }
                        item.copy(isSelected = isOn)
                    })
                }
            }
        }
    }

    fun deleteCategory(categoryItem: CategoryItem) {
        viewModelScope.launch {
            val category = repository.getCategories().find {
                it.iconResId == categoryItem.iconRes && it.name == categoryItem.name
            }
            if (category == null) {
                _showErrorEvent.emit("Category wasn't found")
            } else {
                repository.deleteCategory(category)
                reloadState()
            }
        }
    }

    fun isAddCategoryItem(categoryItem: CategoryItem) = categoryItem == addCategoryItem

    fun onAddBalanceClicked() {
        viewModelScope.launch {
            _navigateToAddFundsEvent.emit(Unit)
        }
    }

    fun onStatisticClicked() {
        viewModelScope.launch {
            _navigateToExpensesStatEvent.emit(Unit)
        }
    }

    fun onEnteredClicked(inputValue: String) {
        viewModelScope.launch {
            if (inputValue.isEmpty()) {
                _showErrorEvent.emit("Wrong value")
            } else {
                val selectedCategory = _uiState.value.categories.find {
                    it.isSelected
                }
                if (selectedCategory == null) {
                    _showErrorEvent.emit("Choose item category")
                } else {
                    val expensesValue = _uiState.value.keyboardInput.calculateInput().toString()
                    val expense = ExpenseEntity(System.currentTimeMillis(), selectedCategory.name, expensesValue)
                    expenseRepository.saveExpenseValue(expense)
                    updateState()
                }
            }
        }
    }

    private fun String.calculateInput(): Double = calculator.calculate(this)

    private fun updateState() {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    categories = currentState.categories.map {
                        it.copy(isSelected = false)
                    },
                    balance = loadBalance(),
                    keyboardInput = ""
                )
            }
        }
    }

    private suspend fun loadCategoryItems(): List<CategoryItem> {
        val categoryItems: MutableList<CategoryItem> = repository
            .getCategories()
            .map {
                CategoryItem(it.name, it.iconResId, false)
            }
            .toMutableList()

        categoryItems.add(addCategoryItem)
        return categoryItems
    }

    private suspend fun loadBalance(): String {
        val expenses = expenseRepository.getSumExpenses()
        val incomes = incomeRepository.getSumIncomes()
        val balance = incomes.minus(expenses)
        return balance.toString()
    }

    companion object {

        private const val SAVED_STATE_INPUT = "SAVED_STATE_INPUT"

        private val addCategoryItem = CategoryItem("add", R.drawable.ic_add, false)
    }
}
