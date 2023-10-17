package com.example.piggybank.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.piggybank.fragments.DailyStatFragment
import com.example.piggybank.fragments.MonthlyStatFragment
import com.example.piggybank.fragments.YearStatFragment

class PageAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        val daily= DailyStatFragment()
        val monthly = MonthlyStatFragment()
        val year = YearStatFragment()
        return when (position) {
            0 -> daily
            1 -> monthly
            else -> year
        }
    }
}
