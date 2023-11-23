package com.example.piggybank.dao

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update

@Entity(tableName = CategoryEntity.TABLE_NAME)
data class CategoryEntity(
    @ColumnInfo(name = "name") val name: String = "",
    @ColumnInfo(name = "icon_res_id") val iconResId: Int = -1,
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
) {

    companion object {

        const val TABLE_NAME = "table_categories"
    }
}

@Dao
interface CategoryDao {

    @Query("SELECT * FROM ${CategoryEntity.TABLE_NAME}")
    suspend fun getAll(): List<CategoryEntity>

    @Query("DELETE FROM ${CategoryEntity.TABLE_NAME} WHERE id = :id")
    suspend fun deleteCategory(id: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(vararg category: CategoryEntity): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(categories: List<CategoryEntity>)

    @Query("DELETE FROM ${CategoryEntity.TABLE_NAME}")
    suspend fun deleteAll()

    @Update
    suspend fun update(category: CategoryEntity)

    @Transaction
    suspend fun overrideCategoryAndGet(categories: List<CategoryEntity>): List<CategoryEntity> {
        deleteAll()
        insertAll(categories)
        return getAll()
    }
}
