package com.example.piggybank.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.piggybank.fragments.ExpensesObjectFragment1

class PageAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        val fragment1 = ExpensesObjectFragment1()
        if (position == 0) {
            return fragment1
        }
        return fragment1
    }
}