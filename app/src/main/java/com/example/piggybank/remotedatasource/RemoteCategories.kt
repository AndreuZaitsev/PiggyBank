package com.example.piggybank.remotedatasource

import com.example.piggybank.dao.CategoryEntity
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import kotlinx.coroutines.tasks.await

interface IRemoteCategories {

    fun saveCategoryAsync(category: CategoryEntity)

    fun deleteCategoryAsync(category: CategoryEntity)

    suspend fun getCategories(): List<CategoryEntity>
}

class StubRemoteCategories @Inject constructor(): IRemoteCategories {

    override fun saveCategoryAsync(category: CategoryEntity) {
        // do nothing
    }

    override fun deleteCategoryAsync(category: CategoryEntity) {
        // do nothing
    }

    override suspend fun getCategories(): List<CategoryEntity> = emptyList()
}

class RemoteCategories @Inject constructor(
    private val fireStore: FirebaseFirestore,
) : IRemoteCategories {

    private val categoriesCollectionRef
        get() = fireStore.collection("categories")

    override fun saveCategoryAsync(category: CategoryEntity) {
        categoriesCollectionRef
            .document(category.toDocumentPath())
            .set(category)
    }

    override suspend fun getCategories(): List<CategoryEntity> =
        categoriesCollectionRef
            .get()
            .await()
            .toObjects(CategoryEntity::class.java)

    override fun deleteCategoryAsync(category: CategoryEntity) {
        categoriesCollectionRef
            .document(category.toDocumentPath())
            .delete()
    }

    private fun CategoryEntity.toDocumentPath(): String = name + "_" + id
}