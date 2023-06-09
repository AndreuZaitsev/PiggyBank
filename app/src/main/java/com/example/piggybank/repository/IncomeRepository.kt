package com.example.piggybank.repository

import com.example.piggybank.dao.IncomeDao
import com.example.piggybank.dao.IncomeEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class IncomeRepository(
    private val incomeDao: IncomeDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun getSumIncomes(): Double = withContext(ioDispatcher) {
        incomeDao.sumIncomes()
    }

    suspend fun saveIncomeValue(incomeValue:IncomeEntity){
        withContext(ioDispatcher){
            incomeDao.saveIncome(incomeValue)
        }
    }
}