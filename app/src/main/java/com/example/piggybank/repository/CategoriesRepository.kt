package com.example.piggybank.repository

import com.example.piggybank.common.dependencyInjection.dispatcher.IoDispatcher
import com.example.piggybank.dao.CategoryDao
import com.example.piggybank.dao.CategoryEntity
import com.example.piggybank.remotedatasource.IRemoteCategories
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class CategoriesRepository @Inject constructor(
    private val categoryDao: CategoryDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val expensesRepository: ExpensesRepository,
    private val remoteCategories: IRemoteCategories,
) {

    suspend fun getCategories(): List<CategoryEntity> {
        return withContext(ioDispatcher) {
            categoryDao.overrideCategoryAndGet(remoteCategories.getCategories())
        }
    }

    suspend fun saveCategory(category: CategoryEntity) {
        withContext(ioDispatcher) {
            val newId = categoryDao.insertCategory(category)
                            .first()
                            .takeIf { it != -1L } ?: return@withContext

            remoteCategories.saveCategory(category.copy(id = newId.toInt()))
        }
    }

    suspend fun deleteCategory(category: CategoryEntity) {
        withContext(ioDispatcher) {
            remoteCategories.deleteCategory(category)
            categoryDao.deleteCategory(category.id)
            expensesRepository.deleteCategoryExpense(category.name)
        }
    }

    suspend fun savePrepopulate(categories: List<CategoryEntity>) {
        withContext(ioDispatcher) {
            val insertedIds = categoryDao.insertCategory(*categories.toTypedArray())

            insertedIds.forEachIndexed { index, newId ->
                if (index == -1) return@forEachIndexed
                remoteCategories.saveCategory(categories[index].copy(id = newId.toInt()))
            }
        }
    }
}