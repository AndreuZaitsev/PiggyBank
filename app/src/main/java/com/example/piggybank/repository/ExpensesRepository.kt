package com.example.piggybank.repository

import com.example.piggybank.dao.ExpenseEntity
import com.example.piggybank.dao.ExpensesDao
import java.util.Date
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ExpensesRepository(
    private val expensesDao: ExpensesDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
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