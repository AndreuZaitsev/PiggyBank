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
import com.example.piggybank.MainActivity
import com.example.piggybank.R
import com.example.piggybank.databinding.AddFundsBinding
import com.example.piggybank.viewmodels.AddFundsViewModel
import kotlinx.coroutines.launch

class AddFundsFragment : Fragment(R.layout.add_funds) {

    private val viewModel: AddFundsViewModel by viewModels()

    private var _binding: AddFundsBinding? = null
    private val binding get() = _binding!!

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
        (requireActivity() as? MainActivity)?.setUpActionBar(binding.addFundsToolbar)
        binding.addFundsToolbar.setNavigationIcon(R.drawable.ic_arrow_left_24dp)

        setUpKeyboard()

        viewModel.loadBalance()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    binding.tvBalance.text = uiState.balance
                    binding.keyboard.tvNumbers.text = uiState.keyBoardInput
                }
            }
        }
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