package com.example.piggybank.common.di

import com.example.piggybank.dao.CategoryEntity
import com.example.piggybank.dao.ExpenseEntity
import com.example.piggybank.dao.IncomeEntity
import com.example.piggybank.remotedatasource.IRemoteCategories
import com.example.piggybank.remotedatasource.IRemoteExpenses
import com.example.piggybank.remotedatasource.IRemoteIncomes
import dagger.Binds
import dagger.Module
import javax.inject.Inject

@Module
interface TestDataSourceModule {

    @Binds
    fun remoteIncomes(impl: StubRemoteIncomes): IRemoteIncomes

    @Binds
    fun remoteExpenses(impl: StubRemoteExpenses): IRemoteExpenses

    @Binds
    fun remoteCategories(impl: StubRemoteCategories): IRemoteCategories
}

class StubRemoteCategories @Inject constructor() : IRemoteCategories {

    override fun saveCategoryAsync(category: CategoryEntity) {
        // do nothing
    }

    override fun deleteCategoryAsync(category: CategoryEntity) {
        // do nothing
    }

    override suspend fun getCategories(): List<CategoryEntity> = emptyList()
}

class StubRemoteExpenses @Inject constructor() : IRemoteExpenses {

    override suspend fun saveExpenses(expense: ExpenseEntity) {
        // do nothing
    }

    override suspend fun getExpenses(): List<ExpenseEntity> {
        return emptyList()
    }

    override suspend fun deleteExpense(id: Int) {
        // do nothing
    }

    override suspend fun deleteExpensesByCategory(categoryName: String) {
        // do nothing
    }
}

class StubRemoteIncomes @Inject constructor() : IRemoteIncomes {

    override suspend fun saveIncomes(income: IncomeEntity) {
        // do nothing
    }

    override suspend fun getIncomes(): List<IncomeEntity> {
        return emptyList()
    }

    override suspend fun deleteIncome(id: Int) {
        // do nothing
    }
}