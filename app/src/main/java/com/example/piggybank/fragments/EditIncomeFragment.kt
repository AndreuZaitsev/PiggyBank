package com.example.piggybank.fragments

import android.app.ProgressDialog.show
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.piggybank.R
import com.example.piggybank.adapters.EditIncomeAdapter
import com.example.piggybank.adapters.IncomeItemTouchHelperCallback
import com.example.piggybank.databinding.EditIncomeBinding
import com.example.piggybank.viewmodels.EditIncomeViewModel
import com.google.android.material.snackbar.Snackbar
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
        binding.rvIncome.adapter = adapter
        viewModel.showIncomes()

        val touchCallBack = IncomeItemTouchHelperCallback(adapter) {
            viewModel.onDelete(it.id)
            Snackbar
                .make(binding.root, "Income Removed", Snackbar.LENGTH_LONG)
                .setAction("UNDO"){
                viewModel.onUndo()}
                .show()
        }
        val touchHelper = ItemTouchHelper(touchCallBack)
        touchHelper.attachToRecyclerView(binding.rvIncome)

        observeUiState()
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.incomeState.collect { uiState ->
                    adapter.submitList(uiState.incomes)
                }
            }
        }
    }
}

