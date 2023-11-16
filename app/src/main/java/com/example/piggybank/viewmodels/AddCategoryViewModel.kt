package com.example.piggybank.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.piggybank.R
import com.example.piggybank.adapters.AddCategoriesAdapter.AddCategoryItem
import com.example.piggybank.dao.CategoryEntity
import com.example.piggybank.repository.CategoriesRepository
import com.example.piggybank.uistates.AddUiState
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddCategoryViewModel @Inject constructor(
    private val repository: CategoriesRepository
): ViewModel() {

    private val _addUiState = MutableStateFlow(AddUiState())
    val addUiState: StateFlow<AddUiState> = _addUiState.asStateFlow()

    private val _navigateToCategoryCreationEvent = MutableSharedFlow<Unit>()
    val navigateToCategoryCreationEvent = _navigateToCategoryCreationEvent.asSharedFlow()

    private val _showErrorEvent = MutableSharedFlow<String>()
    val showErrorEvent = _showErrorEvent.asSharedFlow()

    private val categoryTemplates = listOf(
        AddCategoryItem("cooking", R.drawable.ic_cooking),
        AddCategoryItem("gift", R.drawable.ic_gift),
        AddCategoryItem("internet", R.drawable.ic_internet),
        AddCategoryItem("media", R.drawable.ic_media),
        AddCategoryItem("mobile", R.drawable.ic_mobile),
        AddCategoryItem("repair", R.drawable.ic_repair),
        AddCategoryItem("rest", R.drawable.ic_sea),
        AddCategoryItem("furniture", R.drawable.ic_sofa),
        AddCategoryItem("sport", R.drawable.ic_sport),
        AddCategoryItem("food", R.drawable.ic_food),
        AddCategoryItem("car", R.drawable.ic_car),
        AddCategoryItem("party", R.drawable.ic_alcohole),
        AddCategoryItem("pet", R.drawable.ic_pet),
        AddCategoryItem("clothes", R.drawable.ic_clothes)
    )

    init {
        viewModelScope.launch {
            _addUiState.update { currentState ->
                currentState.copy(
                    categories = categoryTemplates
                )
            }
        }
    }

    fun onCategoryClicked(item: AddCategoryItem) {
        viewModelScope.launch {
            updateCategorySelection(item)
        }
    }

    fun onAddCategoryClicked(categoryName: String) {
        viewModelScope.launch {
            // validate name and show error via event aka navigation
            if (categoryName.isEmpty()) {
                _showErrorEvent.emit("Enter category name!")
            } else {
                val selectedCategory = _addUiState.value.categories.find {
                    it.isSelected
                }
                if (selectedCategory == null) {
                    _showErrorEvent.emit("Choose item category")
                } else {
                    val newCategory = selectedCategory.copy(name = categoryName)
                    repository.saveCategory(CategoryEntity(newCategory.name, newCategory.iconRes))
                    _navigateToCategoryCreationEvent.emit(Unit)
                }
            }
        }
    }

    private fun updateCategorySelection(item: AddCategoryItem) {
        val categoriesToDisplay = if (item.isSelected) {
            categoryTemplates
        } else {
            categoryTemplates.map {
                if (item.iconRes == it.iconRes) {
                    it.copy(isSelected = true)
                } else {
                    it
                }
            }
        }

        _addUiState.update { currentState ->
            currentState.copy(categories = categoriesToDisplay)
        }
    }
}