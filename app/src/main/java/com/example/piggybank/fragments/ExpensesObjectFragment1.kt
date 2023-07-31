package com.example.piggybank.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.piggybank.R
import com.example.piggybank.adapters.ExpensesStatAdapter
import com.example.piggybank.databinding.Fragment1Binding
import com.example.piggybank.viewmodels.ExpensesObject1ViewModel
import kotlinx.coroutines.launch

class ExpensesObjectFragment1 : Fragment(R.layout.fragment_1) {

    private val viewModel: ExpensesObject1ViewModel by viewModels()

    private var _binding: Fragment1Binding? = null
    private val binding get() = _binding!!

    private val adapter by lazy { ExpensesStatAdapter() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = Fragment1Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.rvExpenses.adapter = adapter
        viewModel.showExpenses()
        observeUiState()
    }

    private fun observeUiState(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.expenseState.collect{uiState->
                    adapter.submitList(uiState.expenses)
                }
            }
        }
    }
}