package com.example.piggybank.repository

import com.example.piggybank.R
import com.example.piggybank.dao.CategoryEntity
import com.example.piggybank.dao.CategoryDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CategoriesRepository(
    private val categoryDao: CategoryDao
) {

    private val initialCategories = listOf(
        CategoryEntity("car", R.drawable.ic_car, 1),
        CategoryEntity("pet", R.drawable.ic_pet, 2),
        CategoryEntity("alcohol", R.drawable.ic_alcohole, 3),
        CategoryEntity("clothes", R.drawable.ic_clothes, 4),
        CategoryEntity("food", R.drawable.ic_food, 5)
    )

    init {
        prepopulate()
    }

    private fun prepopulate() {
        CoroutineScope(Job() + Dispatchers.IO).launch {
            categoryDao.insertOrIgnore(*initialCategories.toTypedArray())
        }
    }

    suspend fun getCategories(): List<CategoryEntity> {
        return withContext(Dispatchers.IO) {
            categoryDao.getAll()
        }
    }

    suspend fun saveCategory(category: CategoryEntity) {
        withContext(Dispatchers.IO) {
            categoryDao.insertCategory(category)
        }
    }
}