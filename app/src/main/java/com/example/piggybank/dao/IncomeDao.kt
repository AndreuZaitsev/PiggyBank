package com.example.piggybank.dao

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query

@Entity(tableName = IncomeEntity.TABLE_NAME)
data class IncomeEntity(
    @ColumnInfo(name = "date")
    val timeInMillis: Long,
    @ColumnInfo(name = "income_value")
    val incomeValue: Double,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "name")
    val name: String = "Income",
) {

    companion object {

        const val TABLE_NAME = "table_incomes"
    }
}

@Dao
interface IncomeDao {

    @Query("SELECT total(income_value) FROM ${IncomeEntity.TABLE_NAME}")
    suspend fun sumIncomes(): Double

    // TODO
    @Insert
    suspend fun saveIncome(vararg value: IncomeEntity)
}