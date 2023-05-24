package com.example.piggybank.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.piggybank.dao.Category
import com.example.piggybank.dao.CategoryDao

@Database (entities = [Category::class], version = 1)
abstract class DataBase: RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
}