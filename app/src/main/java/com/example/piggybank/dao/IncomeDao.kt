package com.example.piggybank.dao

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Transaction

@Entity(tableName = IncomeEntity.TABLE_NAME)
data class IncomeEntity(
    @ColumnInfo(name = "date")
    val timeInMillis: Long = 0L,
    @ColumnInfo(name = "income_value")
    val incomeValue: Double = 0.0,
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

    @Query("DELETE FROM ${IncomeEntity.TABLE_NAME} WHERE id = :id")
    suspend fun deleteIncome(id: Int)

    @Query("SELECT * FROM ${IncomeEntity.TABLE_NAME}")
    suspend fun getIncomes(): List<IncomeEntity>

    @Insert
    suspend fun saveIncome(vararg value: IncomeEntity): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(incomes: List<IncomeEntity>)

    @Query("DELETE FROM ${IncomeEntity.TABLE_NAME}")
    suspend fun deleteAll()

    @Transaction
    suspend fun overrideIncomesAndGet(incomes: List<IncomeEntity>): List<IncomeEntity> {
        deleteAll()
        insert(incomes)
        return getIncomes()
    }

    @Transaction
    suspend fun overrideIncomesAndSum(incomes: List<IncomeEntity>): Double {
        deleteAll()
        insert(incomes)
        return sumIncomes()
    }
}