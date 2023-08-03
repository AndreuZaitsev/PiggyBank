package com.example.piggybank.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.piggybank.MainActivity
import com.example.piggybank.R
import com.example.piggybank.adapters.PageAdapter
import com.example.piggybank.databinding.ExpensesStatBinding
import com.example.piggybank.viewmodels.ExpensesStatViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class ExpensesStatFragment : Fragment(R.layout.expenses_stat) {

    private val viewModel: ExpensesStatViewModel by viewModels()

    private lateinit var expensesStatAdapter: PageAdapter
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout

    private var _binding: ExpensesStatBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = ExpensesStatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (requireActivity() as? MainActivity)?.setUpActionBar(binding.statisticExpToolbar)
        binding.statisticExpToolbar.setNavigationIcon(R.drawable.ic_arrow_left_24dp)

        binding.tvEdit.setOnClickListener {
            viewModel.onEditClicked()
        }
        observeNavigationEvents()

        expensesStatAdapter = PageAdapter(this)
        viewPager = binding.pager
        viewPager.adapter = expensesStatAdapter
        tabLayout = binding.tabLayout
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "DAY"

                1 -> "MONTH"

                2 -> "YEAR"

                else -> "DAY"
            }
        }.attach()
    }

    private fun observeNavigationEvents() {
        viewModel.navigateToEditExpensesScreenEvent
            .onEach {
                findNavController().navigate(R.id.action_expensesStatFragment_to_editExpensesFragment)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }
}
