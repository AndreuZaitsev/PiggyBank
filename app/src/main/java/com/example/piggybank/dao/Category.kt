package com.example.piggybank.dao

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query

@Entity(tableName = "categories")
data class Category(
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "icon_res_id") val iconResId: Int,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)

@Dao
interface CategoryDao {

    @Query("SELECT * FROM categories")
    suspend fun getAll(): List<Category>

    @Insert
    suspend fun insertAll(vararg category: Category)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnore(vararg category: Category)
}