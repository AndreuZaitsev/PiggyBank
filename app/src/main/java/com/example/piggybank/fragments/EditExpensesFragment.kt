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
import com.example.piggybank.adapters.EditExpensesAdapter
import com.example.piggybank.adapters.ExpenseItemTouchHelperCallBack
import com.example.piggybank.activity.attachToolbarToMainActivity
import com.example.piggybank.databinding.EditExpenseBinding
import com.example.piggybank.fragments.common.BaseFragment
import com.example.piggybank.viewmodels.EditExpensesViewModel
import com.example.piggybank.viewmodels.common.ViewModelFactory
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class EditExpensesFragment : BaseFragment(R.layout.edit_expense) {

    @Inject lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: EditExpensesViewModel by viewModels(factoryProducer = {viewModelFactory})

    private var _binding: EditExpenseBinding? = null
    private val binding get() = _binding!!

    private val adapter by lazy { EditExpensesAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        injector.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = EditExpenseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        attachToolbarToMainActivity(binding.editExpenseToolbar, R.drawable.ic_menu_black_24dp)

        binding.rvEditExpenses.adapter = adapter

        viewModel.showExpenses()

        binding.tvStatistic.setOnClickListener {
            viewModel.onStatisticsClicked()
        }
        val touchCallBack = ExpenseItemTouchHelperCallBack(adapter) {
            viewModel.onDelete(it.id)
            Snackbar
                .make(binding.root, "Expense Removed", Snackbar.LENGTH_LONG)
                .setAction("UNDO") {
                    viewModel.onUndo()
                }
                .show()
        }

        val touchHelper = ItemTouchHelper(touchCallBack)
        touchHelper.attachToRecyclerView(binding.rvEditExpenses)

        observeUiState()
        observeNavigationEvents()
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState
                    .debounce(100)
                    .collect { uiState ->
                        adapter.submitList(uiState.expenses)
                        binding.tvSpentNothing.isVisible = uiState.expenses.isEmpty()
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