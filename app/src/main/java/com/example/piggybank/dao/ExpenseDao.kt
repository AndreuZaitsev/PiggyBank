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

@Entity(tableName = ExpenseEntity.TABLE_NAME)
data class ExpenseEntity(
    @ColumnInfo(name = "date")
    val dateInMls: Long = 0L,
    @ColumnInfo(name = "name")
    val categoryName: String = "",
    @ColumnInfo(name = "expenses_value")
    val expensesValue: String = "",
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
) {

    companion object {

        const val TABLE_NAME = "table_expenses"
    }
}

@Dao
interface ExpensesDao {

    @Query("SELECT total(expenses_value) FROM ${ExpenseEntity.TABLE_NAME}")
    suspend fun sumExpenses(): Double

    @Query("SELECT * FROM ${ExpenseEntity.TABLE_NAME}")
    suspend fun getExpenses(): List<ExpenseEntity>

    @Query("DELETE FROM ${ExpenseEntity.TABLE_NAME} WHERE id = :id")
    suspend fun deleteExpense(id: Int)

    @Query("DELETE FROM ${ExpenseEntity.TABLE_NAME} WHERE name = :categoryName")
    suspend fun deleteCategoryExpense(categoryName: String)

    @Query("DELETE FROM ${ExpenseEntity.TABLE_NAME}")
    suspend fun deleteAll()

    @Insert
    suspend fun saveExpenses(vararg value: ExpenseEntity): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpenses(expenses: List<ExpenseEntity>)

    @Update
    suspend fun update(expenses: List<ExpenseEntity>)

    @Transaction
    suspend fun overrideExpensesAndGet(expenses: List<ExpenseEntity>): List<ExpenseEntity> {
        deleteAll()
        insertExpenses(expenses)
        return getExpenses()
    }

    @Transaction
    suspend fun overrideExpensesAndSum(expenses: List<ExpenseEntity>): Double {
        deleteAll()
        insertExpenses(expenses)
        return sumExpenses()
    }

}