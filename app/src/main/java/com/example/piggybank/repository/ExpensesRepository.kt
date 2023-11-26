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
    private val remoteExpensesSource: IRemoteExpenses,
) {

    suspend fun getSumExpenses(): Double = withContext(ioDispatcher) {
        expensesDao.overrideExpensesAndSum(remoteExpensesSource.getExpenses())
    }

    suspend fun getExpenses(): List<ExpenseEntity> =
        withContext(ioDispatcher) {
            expensesDao.overrideExpensesAndGet(remoteExpensesSource.getExpenses())
        }

    suspend fun saveExpenseValue(expenseValue: ExpenseEntity) {
        withContext(ioDispatcher) {
            val newId = expensesDao.insert(expenseValue)
                            .first().takeIf { it != -1L } ?: return@withContext
            remoteExpensesSource.saveExpenses(expenseValue.copy(id = newId.toInt()))
        }
    }

    suspend fun deleteExpense(id: Int) =
        withContext(ioDispatcher) {
            remoteExpensesSource.deleteExpense(id)
            expensesDao.deleteById(id)
        }

    suspend fun deleteExpensesByCategory(categoryName: String) = withContext(ioDispatcher) {
        remoteExpensesSource.deleteExpensesByCategory(categoryName)
        expensesDao.deleteExpensesByCategory(categoryName)
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