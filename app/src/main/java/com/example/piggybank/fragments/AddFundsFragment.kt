package com.example.piggybank.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.piggybank.R
import com.example.piggybank.activity.attachToolbarToMainActivity
import com.example.piggybank.databinding.AddFundsBinding
import com.example.piggybank.fragments.common.BaseFragment
import com.example.piggybank.viewmodels.AddFundsViewModel
import com.example.piggybank.viewmodels.common.ViewModelFactory
import javax.inject.Inject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class AddFundsFragment : BaseFragment(R.layout.add_funds) {

    @Inject lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: AddFundsViewModel by viewModels (factoryProducer = {viewModelFactory})

    private var _binding: AddFundsBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        injector.inject(this)
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = AddFundsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        attachToolbarToMainActivity(binding.addFundsToolbar, R.drawable.ic_arrow_left_24dp)
        setUpKeyboard()
        binding.tvEdit.setOnClickListener {
            viewModel.onEditClicked()
        }

        viewModel.loadBalance()
        observeUiState()
        observeNavigationEvents()
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    binding.tvBalance.text = uiState.balance
                    binding.keyboard.tvNumbers.text = uiState.keyBoardInput
                }
            }
        }
    }

    private fun observeNavigationEvents() {
        viewModel.navigateToEditIncomesScreenEvent
            .onEach {
                findNavController().navigate(R.id.action_addFundsFragment_to_editIncomeFragment)
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
            ivEnter.setOnClickListener {
                viewModel.onEnterClicked()
            }
        }
    }
}