package com.example.piggybank.repository

import com.example.piggybank.dao.ExpenseEntity
import com.example.piggybank.dao.ExpensesDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ExpensesRepository(
    private val expensesDao: ExpensesDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun getSumExpenses(): Double = withContext(ioDispatcher) {
        expensesDao.sumExpenses()
    }

    suspend fun getExpenses(): List<ExpenseEntity> {
        return withContext(ioDispatcher) {
            expensesDao.getExpenses()
        }
    }

    suspend fun saveExpenseValue(expenseValue: ExpenseEntity){
        withContext(ioDispatcher){
            expensesDao.saveExpenses(expenseValue)
        }
    }
}