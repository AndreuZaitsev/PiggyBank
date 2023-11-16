package com.example.piggybank.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle.State.STARTED
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.example.piggybank.DotsIndicatorDecoration
import com.example.piggybank.R
import com.example.piggybank.R.color
import com.example.piggybank.R.drawable
import com.example.piggybank.R.layout
import com.example.piggybank.activity.attachToolbarToMainActivity
import com.example.piggybank.adapters.CategoriesAdapter
import com.example.piggybank.adapters.CategoryItem
import com.example.piggybank.databinding.MainFragmentBinding
import com.example.piggybank.fragments.common.BaseFragment
import com.example.piggybank.viewmodels.MainViewModel
import com.example.piggybank.viewmodels.common.ViewModelFactory
import javax.inject.Inject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MainFragment : BaseFragment(layout.main_fragment) {

    @Inject lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: MainViewModel by viewModels(factoryProducer = { viewModelFactory })

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    private val categoriesAdapter = CategoriesAdapter(
        onClick = { clickedCategory ->
            viewModel.onCategoryClicked(clickedCategory)
        },
        onLongClick = {
            if (!viewModel.isAddCategoryItem(it)) {
                showCategoryDeletionDialog(it)
            }
        }
    )

    private val snapHelper: SnapHelper = PagerSnapHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        injector.inject(this)
        super.onCreate(savedInstanceState)
    }

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
        attachToolbarToMainActivity(binding.toolbar, drawable.ic_menu_black_24dp)

        with(binding.listCategories) {
            snapHelper.attachToRecyclerView(this)
            adapter = categoriesAdapter
            addItemDecoration(
                DotsIndicatorDecoration(
                    colorInactive = ContextCompat.getColor(context, color.unselected_item),
                    colorActive = ContextCompat.getColor(context, color.selected_item)
                )
            )
        }

        setUpKeyboard()
        binding.ivAddBalance.setOnClickListener {
            viewModel.onAddBalanceClicked()
        }
        binding.tvStatistic.setOnClickListener {
            viewModel.onStatisticClicked()
        }
        viewModel.reloadState()
        observeErrorEvent()
        observeUiState()
        observeNavigationEvents()
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(STARTED) {
                viewModel.uiState.collect { uiState ->
                    categoriesAdapter.submitList(uiState.categories)
                    binding.tvBalance.text = uiState.balance
                    binding.keyboard.tvNumbers.text = uiState.keyboardInput
                }
            }
        }
    }

    private fun observeErrorEvent() {
        viewModel.showErrorEvent
            .onEach {
                Toast.makeText(binding.root.context, it, Toast.LENGTH_SHORT).show()
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
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

            ivEnter.setOnClickListener {
                viewModel.onEnteredClicked(binding.keyboard.tvNumbers.text.toString())
            }
        }
    }

    private fun showCategoryDeletionDialog(categoryItem: CategoryItem) {
        val builder = AlertDialog.Builder(requireActivity())
        with(builder) {
            setTitle("Deletion Category")
            setMessage("Deletion of category will delete the all previous expenses liked to it")
            setPositiveButton("OK") { _, _ ->
                viewModel.deleteCategory(categoryItem)
            }
            setNegativeButton("CANCEL") { dialog, _ ->
                dialog.dismiss()
            }
        }
        builder.show()
    }
}


