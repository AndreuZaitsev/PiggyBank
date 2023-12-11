package com.example.piggybank.common.di.presentation

import com.example.piggybank.activity.MainActivity
import com.example.piggybank.fragments.AddCategoryFragment
import com.example.piggybank.fragments.AddFundsFragment
import com.example.piggybank.fragments.DailyStatFragment
import com.example.piggybank.fragments.EditExpensesFragment
import com.example.piggybank.fragments.EditIncomeFragment
import com.example.piggybank.fragments.ExpensesStatFragment
import com.example.piggybank.fragments.MainFragment
import com.example.piggybank.fragments.MonthlyStatFragment
import com.example.piggybank.fragments.SplashFragment
import com.example.piggybank.fragments.YearStatFragment
import dagger.Subcomponent

@PresentationScope
@Subcomponent(modules = [PresentationModule::class, ViewModelModule::class])
interface PresentationComponent {

    fun inject (activity: MainActivity)
    fun inject (addCategoryFragment: AddCategoryFragment)
    fun inject (addFundsFragment: AddFundsFragment)
    fun inject(dailyStatFragment: DailyStatFragment)
    fun inject(editExpensesFragment: EditExpensesFragment)
    fun inject(expensesStatFragment: ExpensesStatFragment)
    fun inject(mainFragment: MainFragment)
    fun inject(monthlyStatFragment: MonthlyStatFragment)
    fun inject(splashFragment: SplashFragment)
    fun inject(yearStatFragment: YearStatFragment)
    fun inject(editIncomeFragment: EditIncomeFragment)
}