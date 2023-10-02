package com.example.piggybank.repository

import android.content.SharedPreferences
import com.example.piggybank.R
import com.example.piggybank.dao.CategoryDao
import com.example.piggybank.dao.CategoryEntity
import com.example.piggybank.dao.ExpensesDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CategoriesRepository(
    private val categoryDao: CategoryDao,
    private val expensesDao: ExpensesDao
) {
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

    suspend fun deleteCategory(category: CategoryEntity) {
        withContext(Dispatchers.IO) {
            categoryDao.deleteCategory(category.id)
            expensesDao.deleteExpense(category.name)
        }
    }
}