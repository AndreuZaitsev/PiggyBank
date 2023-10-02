package com.example.piggybank.dao

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query

@Entity(tableName = ExpenseEntity.TABLE_NAME)
data class ExpenseEntity(
    @ColumnInfo(name = "date")
    val dateInMls: Long,
    @ColumnInfo(name = "name")
    val categoryName: String,
    @ColumnInfo(name = "expenses_value")
    val expensesValue: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
) {

    companion object {

        const val TABLE_NAME = "table_expenses"
    }
}

@Dao
interface ExpensesDao{

    @Query("SELECT total(expenses_value) FROM ${ExpenseEntity.TABLE_NAME}")
    suspend fun sumExpenses(): Double

    @Query("SELECT * FROM ${ExpenseEntity.TABLE_NAME}")
    suspend fun getExpenses(): List<ExpenseEntity>

    @Query("DELETE FROM ${ExpenseEntity.TABLE_NAME} WHERE id = :id")
    suspend fun deleteExpense(id:Int)

    @Query("DELETE FROM ${ExpenseEntity.TABLE_NAME} WHERE name = :categoryName")
    suspend fun deleteExpense(categoryName: String)

    @Insert
    suspend fun saveExpenses(vararg value: ExpenseEntity)
}