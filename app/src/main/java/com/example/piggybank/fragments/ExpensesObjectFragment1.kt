package com.example.piggybank.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.piggybank.R
import com.example.piggybank.adapters.ExpensesStatAdapter
import com.example.piggybank.databinding.Fragment1Binding


class ExpensesObjectFragment1 : Fragment(R.layout.fragment_1) {

    private var _binding: Fragment1Binding? = null
    private val binding get() = _binding!!

    private val adapter by lazy { ExpensesStatAdapter() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = Fragment1Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.rvExpenses.adapter = adapter

    }

}