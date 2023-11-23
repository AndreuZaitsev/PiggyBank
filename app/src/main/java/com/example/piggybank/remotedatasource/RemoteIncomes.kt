package com.example.piggybank.remotedatasource

import com.example.piggybank.dao.IncomeEntity
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import kotlinx.coroutines.tasks.await

interface IRemoteIncomes{

    suspend fun saveIncomes(income: IncomeEntity)
    suspend fun getIncomes(): List<IncomeEntity>
    suspend fun deleteIncome(id:Int)

}

class RemoteIncomes @Inject constructor(
    private val fireStore: FirebaseFirestore
): IRemoteIncomes {

    private val collectionRef get() = fireStore.collection("incomes")

    override suspend fun saveIncomes(income: IncomeEntity) {
        collectionRef
            .document(income.toDocumentPath())
            .set(income)
            .await()
    }

    override suspend fun getIncomes(): List<IncomeEntity> =
        collectionRef
            .get()
            .await()
            .toObjects(IncomeEntity::class.java)

    override suspend fun deleteIncome(id:Int) {
        collectionRef
            .document("Income_$id")
            .delete()
            .await()
    }

    private fun IncomeEntity.toDocumentPath(): String = name + "_" + id
}