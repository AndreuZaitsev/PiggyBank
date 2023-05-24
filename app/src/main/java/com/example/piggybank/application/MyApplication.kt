package com.example.piggybank.application

import android.app.Application
import androidx.room.Room
import com.example.piggybank.database.DataBase

class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        DataBaseHolder.dataBase = Room.databaseBuilder(
            applicationContext,
            DataBase::class.java, "data_base"
        ).build()
    }
}

object DataBaseHolder {

    lateinit var dataBase: DataBase
}