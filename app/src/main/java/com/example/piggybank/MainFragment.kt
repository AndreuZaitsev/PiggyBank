package com.example.piggybank

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.example.piggybank.databinding.MainFragmentBinding
import kotlinx.coroutines.launch

class MainFragment : Fragment(R.layout.main_fragment) {

    private val viewModel: MainViewModel by viewModels()

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    private val adapter = CategoriesAdapter {
        viewModel.onCategoryClicked(it)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.listCategories.adapter = adapter
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    adapter.submitList(uiState.categories)

                    binding.tvBalance.text = uiState.balance

                    binding.keyboard.tvNumbers.text = uiState.keyboardInput
                }
            }
        }

        val snapHelper: SnapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding.listCategories)

        setUpKeyboard()
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


