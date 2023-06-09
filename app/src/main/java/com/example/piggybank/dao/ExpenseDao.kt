package com.example.piggybank.dao

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = ExpenseEntity.TABLE_NAME)
data class ExpenseEntity(
    @ColumnInfo(name = "category_name") val categoryName: String,
    @ColumnInfo(name = "date") val dateInMls: Long,
    @ColumnInfo(name = "expenses") val expensesValue: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
) {

    companion object {

        const val TABLE_NAME = "table_expenses"
    }
}

@Dao
interface ExpensesDao