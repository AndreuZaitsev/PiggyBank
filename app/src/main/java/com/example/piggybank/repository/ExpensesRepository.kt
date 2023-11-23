package com.example.piggybank.repository

import com.example.piggybank.common.dependencyInjection.dispatcher.IoDispatcher
import com.example.piggybank.dao.ExpenseEntity
import com.example.piggybank.dao.ExpensesDao
import com.example.piggybank.remotedatasource.IRemoteExpenses
import java.util.Date
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class ExpensesRepository @Inject constructor(
    private val expensesDao: ExpensesDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val remoteExpenses: IRemoteExpenses,
) {

    suspend fun getSumExpenses(): Double = withContext(ioDispatcher) {
        expensesDao.overrideExpensesAndSum(remoteExpenses.getExpenses())
    }

    suspend fun getExpenses(): List<ExpenseEntity> =
        withContext(ioDispatcher) {
            expensesDao.overrideExpensesAndGet(remoteExpenses.getExpenses())
        }

    suspend fun saveExpenseValue(expenseValue: ExpenseEntity) {
        withContext(ioDispatcher) {
            val newId = expensesDao.saveExpenses(expenseValue)
                            .first().takeIf { it != -1L } ?: return@withContext
            remoteExpenses.saveExpenses(expenseValue.copy(id = newId.toInt()))
        }
    }

    suspend fun deleteExpense(id: Int) =
        withContext(ioDispatcher) {
            remoteExpenses.deleteExpense(id)
            expensesDao.deleteExpense(id)
        }

    suspend fun deleteCategoryExpense(categoryName: String) = withContext(ioDispatcher) {
        remoteExpenses.deleteCategoryExpense(categoryName)
        expensesDao.deleteCategoryExpense(categoryName)
    }

    suspend fun getExpensesByMonth(month: Int): List<ExpenseEntity> {
        return withContext(ioDispatcher) {
            getExpenses().filter {
                Date(it.dateInMls).month == month
            }
        }
    }

    suspend fun getExpensesByYear(year: Int): List<ExpenseEntity> {
        return withContext(ioDispatcher) {
            getExpenses().filter {
                Date(it.dateInMls).year == year
            }
        }
    }
}