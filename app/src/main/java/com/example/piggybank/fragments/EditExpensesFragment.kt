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
import androidx.navigation.fragment.findNavController
import com.example.piggybank.R
import com.example.piggybank.adapters.EditExpensesAdapter
import com.example.piggybank.databinding.EditExpenseBinding
import com.example.piggybank.viewmodels.EditExpensesViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class EditExpensesFragment : Fragment(R.layout.edit_expense) {

    private val viewModel: EditExpensesViewModel by viewModels()

    private var _binding: EditExpenseBinding? = null
    private val binding get() = _binding!!

    private val adapter by lazy { EditExpensesAdapter() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = EditExpenseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.rvEditExpenses.adapter = adapter

        viewModel.showExpenses()

        binding.tvStatistic.setOnClickListener {
            viewModel.onStatisticsClicked()
        }

        observeUiState()
        observeNavigationEvents()
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    adapter.submitList(uiState.expenses)
                }
            }
        }
    }

    private fun observeNavigationEvents() {
        viewModel.navigateToExpensesStatScreenEvent
            .onEach {
                findNavController().navigate(R.id.action_editExpensesFragment_to_expensesStatFragment)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }
}