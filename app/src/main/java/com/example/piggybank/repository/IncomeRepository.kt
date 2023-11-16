package com.example.piggybank.repository

import com.example.piggybank.dao.IncomeDao
import com.example.piggybank.dao.IncomeEntity
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class IncomeRepository @Inject constructor(
    private val incomeDao: IncomeDao,
) {

    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO

    suspend fun getSumIncomes(): Double = withContext(ioDispatcher) {
        incomeDao.sumIncomes()
    }

    suspend fun saveIncomeValue(incomeValue: IncomeEntity) {
        withContext(ioDispatcher) {
            incomeDao.saveIncome(incomeValue)
        }
    }

    suspend fun getIncomes(): List<IncomeEntity> {
        return withContext(ioDispatcher) {
            incomeDao.getIncomes()
        }
    }

    suspend fun deleteIncome(id: Int) {
        withContext(ioDispatcher) {
            incomeDao.deleteIncome(id)
        }
    }
}