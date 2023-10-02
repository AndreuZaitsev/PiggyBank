package com.example.piggybank.dao

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query

@Entity(tableName = CategoryEntity.TABLE_NAME)
data class CategoryEntity(
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "icon_res_id") val iconResId: Int,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
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
    suspend fun deleteCategory(id:Int)

    @Insert
    suspend fun insertAll(vararg category: CategoryEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnore(vararg category: CategoryEntity)

    @Insert
    suspend fun insertCategory(vararg category: CategoryEntity)
}
