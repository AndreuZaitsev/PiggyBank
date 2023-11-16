package com.example.piggybank.repository

import com.example.piggybank.common.dependencyInjection.dispatcher.IoDispatcher
import com.example.piggybank.dao.ExpenseEntity
import com.example.piggybank.dao.ExpensesDao
import java.util.Date
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class ExpensesRepository @Inject constructor(
    private val expensesDao: ExpensesDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {
    suspend fun getSumExpenses(): Double = withContext(ioDispatcher) {
        expensesDao.sumExpenses()
    }

    suspend fun getExpenses(): List<ExpenseEntity> {
        return withContext(ioDispatcher) {
            expensesDao.getExpenses()
        }
    }

    suspend fun saveExpenseValue(expenseValue: ExpenseEntity) {
        withContext(ioDispatcher) {
            expensesDao.saveExpenses(expenseValue)
        }
    }

    suspend fun deleteExpense(id: Int) {
        withContext(ioDispatcher) {
            expensesDao.deleteExpense(id)
        }
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