package com.example.piggybank.common.di.app

import com.example.piggybank.remotedatasource.IRemoteCategories
import com.example.piggybank.remotedatasource.IRemoteExpenses
import com.example.piggybank.remotedatasource.IRemoteIncomes
import com.example.piggybank.remotedatasource.RemoteCategories
import com.example.piggybank.remotedatasource.RemoteExpenses
import com.example.piggybank.remotedatasource.RemoteIncomes
import dagger.Binds
import dagger.Module

@Module
interface DataSourceModule {

    @Binds
    fun remoteIncomes(impl: RemoteIncomes): IRemoteIncomes

    @Binds
    fun remoteExpenses(impl: RemoteExpenses): IRemoteExpenses

    @Binds
    fun remoteCategories(impl: RemoteCategories): IRemoteCategories
}