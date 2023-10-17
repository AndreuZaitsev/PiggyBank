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
import com.example.piggybank.databinding.DailyStatBinding
import com.example.piggybank.viewmodels.DailyStatViewModel
import kotlinx.coroutines.launch

class DailyStatFragment : Fragment(R.layout.daily_stat) {

    private val viewModel: DailyStatViewModel by viewModels()

    private var _binding: DailyStatBinding? = null
    private val binding get() = _binding!!

    private val adapter by lazy { ExpensesStatAdapter() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = DailyStatBinding.inflate(inflater, container, false)
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
                viewModel.dailyState.collect{ uiState->
                    adapter.submitList(uiState.expenses)
                }
            }
        }
    }
}