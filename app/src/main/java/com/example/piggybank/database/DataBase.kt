package com.example.piggybank.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.piggybank.dao.CategoryEntity
import com.example.piggybank.dao.CategoryDao
import com.example.piggybank.dao.ExpensesDao
import com.example.piggybank.dao.IncomeEntity
import com.example.piggybank.dao.IncomeDao

@Database (entities = [CategoryEntity::class, IncomeEntity::class], version = 1)
abstract class DataBase: RoomDatabase() {
    abstract fun categoryDao(): CategoryDao

    abstract fun incomeDao(): IncomeDao

    abstract fun expensesDao(): ExpensesDao
}