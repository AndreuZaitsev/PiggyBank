package com.example.piggybank.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle.State.STARTED
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.example.piggybank.R
import com.example.piggybank.adapters.CategoriesAdapter
import com.example.piggybank.databinding.MainFragmentBinding
import com.example.piggybank.viewmodels.MainViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MainFragment : Fragment(R.layout.main_fragment) {

    private val viewModel: MainViewModel by viewModels()

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    private val adapter = CategoriesAdapter { clickedCategory ->
        viewModel.onCategoryClicked(clickedCategory)
    }
    private val snapHelper: SnapHelper = PagerSnapHelper()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        snapHelper.attachToRecyclerView(null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        snapHelper.attachToRecyclerView(binding.listCategories)
        binding.listCategories.adapter = adapter

        setUpKeyboard()

        binding.ivAddBalance.setOnClickListener {
            viewModel.onAddBalanceClicked()
        }

        binding.tvStatistic.setOnClickListener {
            viewModel.onStatisticClicked()
        }

        viewModel.showCategories()
        observeUiState()
        observeNavigationEvents()
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(STARTED) {
                viewModel.uiState.collect { uiState ->
                    adapter.submitList(uiState.categories)
                    binding.tvBalance.text = uiState.balance
                    binding.keyboard.tvNumbers.text = uiState.keyboardInput
                }
            }
        }
    }

    private fun observeNavigationEvents() {
        viewModel.navigateToCategoryCreationEvent
            .onEach {
                findNavController().navigate(R.id.action_mainFragment_to_addFragment)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.navigateToAddFundsEvent
            .onEach {
                findNavController().navigate(R.id.action_mainFragment_to_addFundsFragment)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.navigateToExpensesStatEvent
            .onEach {
                findNavController().navigate(R.id.action_mainFragment_to_expensesStatFragment)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun setUpKeyboard() {
        with(binding.keyboard) {
            arrayOf(
                tv0,
                tv1,
                tv2,
                tv3,
                tv4,
                tv5,
                tv6,
                tv7,
                tv8,
                tv9,
                tvDot,
                tvPlus,
                tvMinus
            ).forEach { textView ->
                textView.setOnClickListener {
                    viewModel.onKeyClicked(textView.text.toString())
                }
            }

            tvReset.setOnClickListener {
                viewModel.onResetClicked()
            }
            btnDelete.setOnClickListener {
                viewModel.onDeleteClicked()
            }

            tvSum.setOnClickListener {
                viewModel.onSumClicked()
            }
        }
    }
}


