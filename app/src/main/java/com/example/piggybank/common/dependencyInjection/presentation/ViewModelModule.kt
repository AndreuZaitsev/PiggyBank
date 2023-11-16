package com.example.piggybank.common.dependencyInjection.presentation

import androidx.lifecycle.ViewModel
import com.example.piggybank.viewmodels.AddCategoryViewModel
import com.example.piggybank.viewmodels.AddFundsViewModel
import com.example.piggybank.viewmodels.DailyStatViewModel
import com.example.piggybank.viewmodels.EditExpensesViewModel
import com.example.piggybank.viewmodels.EditIncomeViewModel
import com.example.piggybank.viewmodels.ExpensesStatViewModel
import com.example.piggybank.viewmodels.MainViewModel
import com.example.piggybank.viewmodels.MonthlyStatViewModel
import com.example.piggybank.viewmodels.YearStatViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class) fun mainViewModel(mainViewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddCategoryViewModel::class) fun addCategoryViewModel(addCategoryViewModel: AddCategoryViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddFundsViewModel::class) fun addFundsViewModel(addFundsViewModel: AddFundsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DailyStatViewModel::class) fun dailyStatViewModel(dailyStatViewModel: DailyStatViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EditExpensesViewModel::class) fun editExpensesViewModel(editExpensesViewModel: EditExpensesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EditIncomeViewModel::class) fun editIncomeViewModel(editIncomeViewModel: EditIncomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ExpensesStatViewModel::class) fun expensesStatViewModel(expensesStatViewModel: ExpensesStatViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MonthlyStatViewModel::class) fun monthlyStatViewModel(monthlyStatViewModel: MonthlyStatViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(YearStatViewModel::class) fun yearStatViewModel(yearStatViewModel: YearStatViewModel): ViewModel
}