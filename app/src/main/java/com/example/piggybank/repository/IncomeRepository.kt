package com.example.piggybank.repository

import com.example.piggybank.common.dependencyInjection.dispatcher.IoDispatcher
import com.example.piggybank.dao.IncomeDao
import com.example.piggybank.dao.IncomeEntity
import com.example.piggybank.remotedatasource.IRemoteIncomes
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class IncomeRepository @Inject constructor(
    private val incomeDao: IncomeDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val remoteIncomes: IRemoteIncomes,
) {

    suspend fun getSumIncomes(): Double = withContext(ioDispatcher) {
        incomeDao.overrideIncomesAndSum(remoteIncomes.getIncomes())
    }

    suspend fun saveIncomeValue(incomeValue: IncomeEntity) {
        withContext(ioDispatcher) {
            val newId = incomeDao.saveIncome(incomeValue)
                            .first()
                            .takeIf { it != -1L } ?: return@withContext

            remoteIncomes.saveIncomes(incomeValue.copy(id = newId.toInt()))
        }
    }

    suspend fun getIncomes(): List<IncomeEntity> = withContext(ioDispatcher) {
        incomeDao.overrideIncomesAndGet(remoteIncomes.getIncomes())
    }

    suspend fun deleteIncome(id: Int) {
        withContext(ioDispatcher) {
            remoteIncomes.deleteIncome(id)
            incomeDao.deleteIncome(id)
        }
    }
}