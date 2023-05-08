package com.example.piggybank

import android.app.Application
import androidx.room.Room

class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        DataBaseHolder.dataBase  = Room.databaseBuilder(
            applicationContext,
            DataBase::class.java, "data_base"
        ).build()
    }
}

object DataBaseHolder {

    lateinit var dataBase: DataBase
}