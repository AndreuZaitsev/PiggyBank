package com.example.piggybank.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle.State.STARTED
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.piggybank.R
import com.example.piggybank.adapters.AddCategoriesAdapter
import com.example.piggybank.attachToolbarToMainActivity
import com.example.piggybank.databinding.AddCategoryBinding
import com.example.piggybank.viewmodels.AddCategoryViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class AddCategoryFragment : Fragment(R.layout.add_category) {

    private val viewModel: AddCategoryViewModel by viewModels()

    private var _binding: AddCategoryBinding? = null
    private val binding get() = _binding!!

    private val adapter = AddCategoriesAdapter {
        viewModel.onCategoryClicked(it)
    }

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

        viewModel.navigateToCategoryCreationEvent
            .onEach {
                findNavController().popBackStack()
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.showErrorEvent
            .onEach {
                Toast.makeText(binding.root.context, it, Toast.LENGTH_SHORT).show()
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        binding.ivAddCategory.setOnClickListener {
            viewModel.onAddCategoryClicked(binding.etCategoryName.text.toString())
        }

        attachToolbarToMainActivity(binding.addToolbar, R.drawable.ic_arrow_left_24dp)
    }
}