package com.example.piggybank.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.piggybank.dao.CategoryDao
import com.example.piggybank.dao.CategoryEntity
import com.example.piggybank.dao.ExpenseEntity
import com.example.piggybank.dao.ExpensesDao
import com.example.piggybank.dao.IncomeDao
import com.example.piggybank.dao.IncomeEntity

@Database(entities = [CategoryEntity::class, IncomeEntity::class, ExpenseEntity::class], version = 2, exportSchema = false)
abstract class DataBase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao

    abstract fun incomeDao(): IncomeDao

    abstract fun expensesDao(): ExpensesDao
}
