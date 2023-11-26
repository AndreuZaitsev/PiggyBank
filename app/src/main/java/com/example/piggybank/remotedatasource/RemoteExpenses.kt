package com.example.piggybank.remotedatasource

import com.example.piggybank.dao.ExpenseEntity
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import kotlinx.coroutines.tasks.await

interface IRemoteExpenses {

    suspend fun saveExpenses(expense: ExpenseEntity)
    suspend fun getExpenses(): List<ExpenseEntity>
    suspend fun deleteExpense(id: Int)
    suspend fun deleteExpensesByCategory(categoryName: String)
}

class RemoteExpenses @Inject constructor(
    private val fireStore: FirebaseFirestore,
) : IRemoteExpenses {

    private val collectionRef get() = fireStore.collection("expenses")
    override suspend fun saveExpenses(expense: ExpenseEntity) {
        collectionRef
            .document(expense.toDocumentName())
            .set(expense)
            .await()
    }

    override suspend fun getExpenses(): List<ExpenseEntity> = collectionRef
        .get()
        .await()
        .toObjects(ExpenseEntity::class.java)

    override suspend fun deleteExpense(id: Int) {
        val docIdToDelete = collectionRef
            .whereEqualTo("id", id)
            .get()
            .await()
            .firstOrNull()
            ?.id

        docIdToDelete?.let { deleteDocument(it) }
    }

    override suspend fun deleteExpensesByCategory(categoryName: String) {
        val remoteExpenses = getExpenses()
        remoteExpenses
            .filter { it.categoryName == categoryName }
            .map { it.toDocumentName() }
            .forEach { deleteDocument(it) }
    }

    private suspend fun deleteDocument(path: String) {
        collectionRef
            .document(path)
            .delete()
            .await()
    }

    private fun ExpenseEntity.toDocumentName(): String = categoryName + "_" + id
}