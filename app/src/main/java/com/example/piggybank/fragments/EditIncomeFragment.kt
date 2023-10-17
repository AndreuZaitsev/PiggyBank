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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.piggybank.R
import com.example.piggybank.adapters.EditIncomeAdapter
import com.example.piggybank.adapters.IncomeItemTouchHelperCallback
import com.example.piggybank.attachToolbarToMainActivity
import com.example.piggybank.databinding.EditIncomeBinding
import com.example.piggybank.viewmodels.EditIncomeViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class EditIncomeFragment : Fragment(R.layout.edit_income) {

    private val viewModel: EditIncomeViewModel by viewModels()

    private var _binding: EditIncomeBinding? = null
    private val binding get() = _binding!!

    private val adapter by lazy { EditIncomeAdapter() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = EditIncomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        attachToolbarToMainActivity(binding.editIncomesToolbar, R.drawable.ic_menu_black_24dp)

        binding.rvIncome.adapter = adapter
        viewModel.showIncomes()

        binding.tvStatistic.setOnClickListener {
            viewModel.onStatisticsClicked()
        }

        val touchCallBack = IncomeItemTouchHelperCallback(adapter) {
            viewModel.onDelete(it.id)
            Snackbar
                .make(binding.root, "Income Removed", Snackbar.LENGTH_LONG)
                .setAction("UNDO") {
                    viewModel.onUndo()
                }
                .show()
        }
        val touchHelper = ItemTouchHelper(touchCallBack)
        touchHelper.attachToRecyclerView(binding.rvIncome)

        observeUiState()
        observeNavigationEvent()
    }

    private fun observeNavigationEvent() {
        viewModel.navigateToExpensesStatFragmentEvent
            .onEach {
                findNavController().navigate(R.id.action_editIncomeFragment_to_expensesStatFragment)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.incomeState
                    .debounce(100)
                    .collect { uiState ->
                    adapter.submitList(uiState.incomes)
                    binding.tvEmptyWallet.isVisible = uiState.incomes.isEmpty()
                }
            }
        }
    }
}

