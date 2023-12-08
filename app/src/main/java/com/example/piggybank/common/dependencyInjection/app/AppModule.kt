package com.example.piggybank.common.dependencyInjection.app

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.example.piggybank.application.MyApplication
import com.example.piggybank.dao.CategoryDao
import com.example.piggybank.dao.ExpensesDao
import com.example.piggybank.dao.IncomeDao
import com.example.piggybank.database.DataBase
import com.example.piggybank.remotedatasource.IRemoteCategories
import com.example.piggybank.remotedatasource.IRemoteExpenses
import com.example.piggybank.remotedatasource.IRemoteIncomes
import com.example.piggybank.remotedatasource.RemoteCategories
import com.example.piggybank.remotedatasource.RemoteExpenses
import com.example.piggybank.remotedatasource.RemoteIncomes
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
class AppModule(private val application: MyApplication) {

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

    @Provides
    fun sharedPreferences(application: MyApplication): SharedPreferences =
        application.getSharedPreferences("Prefs", Context.MODE_PRIVATE)

    @Provides
    fun fireStore(): FirebaseFirestore = Firebase.firestore

}

@Module
interface DataSourceModule {

    @Binds
    fun remoteCategories(impl: RemoteCategories): IRemoteCategories

    @Binds
    fun remoteIncomes(impl: RemoteIncomes): IRemoteIncomes

    @Binds
    fun remoteExpenses(impl: RemoteExpenses): IRemoteExpenses
}