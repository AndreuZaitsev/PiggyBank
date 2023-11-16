package com.example.piggybank.common.dependencyInjection.app

import androidx.room.Room
import com.example.piggybank.application.MyApplication
import com.example.piggybank.dao.CategoryDao
import com.example.piggybank.dao.ExpensesDao
import com.example.piggybank.dao.IncomeDao
import com.example.piggybank.database.DataBase
import dagger.Module
import dagger.Provides

@Module
class AppModule(val application: MyApplication) {

    @Provides
    @AppScope
    fun dataBase(): DataBase {
        return Room.databaseBuilder(
            application.applicationContext,
            DataBase::class.java, "data_base"
        ).build()
    }

    @Provides
    fun application(): MyApplication = application

    @Provides
    fun categoryDao(dataBase: DataBase): CategoryDao = dataBase.categoryDao()

    @Provides
    fun expenseDao(dataBase: DataBase): ExpensesDao = dataBase.expensesDao()

    @Provides
    fun incomeDao(dataBase: DataBase): IncomeDao = dataBase.incomeDao()
}