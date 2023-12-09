package com.example.piggybank

import android.content.SharedPreferences
import com.example.piggybank.dao.CategoryEntity
import com.example.piggybank.repository.CategoriesRepository
import javax.inject.Inject

class CategoriesPrepopulate @Inject constructor(
    private val repository: CategoriesRepository,
    private val sharedPreferences: SharedPreferences,
) {

    private val initialCategories = listOf(
        CategoryEntity("car", R.drawable.ic_car, 1),
        CategoryEntity("pet", R.drawable.ic_pet, 2),
        CategoryEntity("alcohol", R.drawable.ic_alcohole, 3),
        CategoryEntity("clothes", R.drawable.ic_clothes, 4),
        CategoryEntity("food", R.drawable.ic_food, 5)
    )

    suspend fun prepopulate() {
        val defValue = false
        val isPopulated = sharedPreferences.getBoolean(PREF_KEY, defValue)
        val noCategories = repository.getCategories().isEmpty()
        if (!isPopulated && noCategories) {
            repository.savePrepopulate(initialCategories)
            with(sharedPreferences.edit()) {
                putBoolean(PREF_KEY, true)
                apply()
            }
        }
    }

    companion object {

        private const val PREF_KEY = "Is prepopulate"
    }
}