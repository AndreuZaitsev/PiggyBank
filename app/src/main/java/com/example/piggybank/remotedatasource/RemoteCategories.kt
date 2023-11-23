package com.example.piggybank.remotedatasource

import com.example.piggybank.dao.CategoryEntity
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import kotlinx.coroutines.tasks.await

interface IRemoteCategories {

    suspend fun getCategories(): List<CategoryEntity>

    suspend fun saveCategory(category: CategoryEntity)

    suspend fun deleteCategory(category: CategoryEntity)
}

class RemoteCategories @Inject constructor(
    private val fireStore: FirebaseFirestore,
) : IRemoteCategories {

    private val categoriesCollectionRef
        get() = fireStore.collection("categories")

    override suspend fun saveCategory(category: CategoryEntity) {
        categoriesCollectionRef
            .document(category.toDocumentPath())
            .set(category)
            .await()
    }

    override suspend fun getCategories(): List<CategoryEntity> =
        categoriesCollectionRef
            .get()
            .await()
            .toObjects(CategoryEntity::class.java)

    override suspend fun deleteCategory(category: CategoryEntity) {
        categoriesCollectionRef
            .document(category.toDocumentPath())
            .delete()
            .await()
    }

    private fun CategoryEntity.toDocumentPath(): String = name + "_" + id
}