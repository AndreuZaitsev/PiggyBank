package com.example.piggybank

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    private val repository = CategoriesRepository(DataBaseHolder.dataBase.categoryDao())

    init {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    categories = loadCategoryItems(),
                    balance = "xer"
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
                repository.addCategory()
            }
            _uiState.update {
                it.copy(
                    categories = loadCategoryItems()
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

        categoryItems.add(CategoryItem("add", R.drawable.ic_add, false))
        return categoryItems
    }
}
