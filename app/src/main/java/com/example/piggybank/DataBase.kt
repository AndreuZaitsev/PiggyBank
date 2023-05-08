package com.example.piggybank

import androidx.room.Database
import androidx.room.RoomDatabase

@Database (entities = [Category::class], version = 1)
abstract class DataBase: RoomDatabase() {
    abstract fun categoryDao(): CategoryDao


}