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
import com.example.piggybank.R
import com.example.piggybank.adapters.AddCategoriesAdapter
import com.example.piggybank.databinding.AddCategoryBinding
import com.example.piggybank.viewmodels.AddViewModel
import kotlinx.coroutines.launch

class AddFragment : Fragment(R.layout.add_category) {

    private val viewModel: AddViewModel by viewModels()

    private var _binding: AddCategoryBinding? = null
    private val binding get() = _binding!!

    private val adapter = AddCategoriesAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = AddCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addCategory.adapter = adapter
        viewLifecycleOwner.lifecycleScope.launch {

            viewLifecycleOwner.repeatOnLifecycle(STARTED) {
                viewModel.addUiState.collect { uistate ->
                    adapter.submitList(uistate.categories)
                }
            }
        }
    }
}