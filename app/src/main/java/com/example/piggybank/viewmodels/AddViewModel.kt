package com.example.piggybank.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.piggybank.R
import com.example.piggybank.adapters.AddCategoriesAdapter.AddCategoryItem
import com.example.piggybank.application.DataBaseHolder
import com.example.piggybank.repository.CategoriesRepository
import com.example.piggybank.uistates.AddUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddViewModel : ViewModel() {

    private val _addUiState = MutableStateFlow(AddUiState())
    val addUiState: StateFlow<AddUiState> = _addUiState.asStateFlow()

    private val repository = CategoriesRepository(DataBaseHolder.dataBase.categoryDao())

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
}