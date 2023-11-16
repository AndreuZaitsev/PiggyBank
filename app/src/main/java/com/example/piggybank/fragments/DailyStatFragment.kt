package com.example.piggybank.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.piggybank.R
import com.example.piggybank.adapters.ExpensesStatAdapter
import com.example.piggybank.databinding.DailyStatBinding
import com.example.piggybank.fragments.common.BaseFragment
import com.example.piggybank.viewmodels.DailyStatViewModel
import com.example.piggybank.viewmodels.common.ViewModelFactory
import javax.inject.Inject
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

class DailyStatFragment :BaseFragment(R.layout.daily_stat) {

    @Inject lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: DailyStatViewModel by viewModels(factoryProducer = {viewModelFactory})

    private var _binding: DailyStatBinding? = null
    private val binding get() = _binding!!

    private val adapter by lazy { ExpensesStatAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        injector.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = DailyStatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.rvExpenses.adapter = adapter
        viewModel.showExpenses()
        observeUiState()
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.dailyState
                    .debounce(100)
                    .collect { uiState ->
                        adapter.submitList(uiState.expenses)
                        binding.tvSpentNothing.isVisible = uiState.expenses.isEmpty()
                    }
            }
        }
    }
}