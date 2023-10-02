package com.example.piggybank.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.piggybank.fragments.DailyStatFragment
import com.example.piggybank.fragments.MonthlyStatFragment

class PageAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        val fragment1 = DailyStatFragment()
        val fragment2 = MonthlyStatFragment()
        return when (position) {
            0 -> fragment1
            else -> fragment2
        }
    }
}
