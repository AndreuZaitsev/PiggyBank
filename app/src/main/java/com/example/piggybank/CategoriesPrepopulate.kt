package com.example.piggybank

import android.content.SharedPreferences
import com.example.piggybank.dao.CategoryDao
import com.example.piggybank.dao.CategoryEntity

class CategoriesPrepopulate(
    private val categoryDao: CategoryDao,
    private val sharedPreferences: SharedPreferences
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
        if (!isPopulated) {
            categoryDao.insertOrIgnore(*initialCategories.toTypedArray())

            with(sharedPreferences.edit()) {
                putBoolean(PREF_KEY, true)
                apply()
            }
        }
    }

    companion object{
        private const val PREF_KEY = "Is prepopulate"
    }
}