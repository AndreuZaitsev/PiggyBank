package com.example.piggybank.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.piggybank.R
import com.example.piggybank.SetupPieChartUseCase
import com.example.piggybank.adapters.YearStatAdapter
import com.example.piggybank.adapters.YearStatAdapter.YearStatItem
import com.example.piggybank.databinding.YearStatBinding
import com.example.piggybank.fragments.common.BaseFragment
import com.example.piggybank.uistates.YearStatUIState
import com.example.piggybank.viewmodels.YearStatViewModel
import com.example.piggybank.viewmodels.common.ViewModelFactory
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import java.math.RoundingMode
import java.util.Calendar
import java.util.Date
import javax.inject.Inject
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

class YearStatFragment : BaseFragment(R.layout.year_stat) {

    @Inject lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: YearStatViewModel by viewModels(factoryProducer = { viewModelFactory })

    private var _binding: YearStatBinding? = null
    private val binding get() = _binding!!

    private val adapter by lazy { YearStatAdapter() }

    private val setupPieChartUseCase = SetupPieChartUseCase()

    override fun onCreate(savedInstanceState: Bundle?) {
        injector.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = YearStatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvYearExpenses.adapter = adapter
        binding.yearPikerButton.setOnClickListener {
            setupDatePicker().show()
        }
        observeUiState()
        setupPieChartUseCase.invoke(binding.pieChart)
        viewModel.onDateSelected(Date())
    }

    private fun setupDatePicker(): DatePickerDialog {
        val calendar: Calendar = Calendar.getInstance()
        return DatePickerDialog(
            this.requireContext(),
            { _, year, _, _ ->
                val date = calendar.apply {
                    set(Calendar.YEAR, year)
                }.time

                viewModel.onDateSelected(date)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).apply {
            hideDayColumn()
            hideMonthColumn()
        }
    }

    private fun DatePickerDialog.hideDayColumn() {
        this.datePicker.findViewById<NumberPicker>(resources.getIdentifier("day", "id", "android"))?.isGone = true
    }

    private fun DatePickerDialog.hideMonthColumn() {
        this.datePicker.findViewById<NumberPicker>(resources.getIdentifier("month", "id", "android"))?.isGone = true
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState
                    .debounce(100)
                    .collect { uiState ->
                        binding.currentYear.text = uiState.selectedDate
                        updatePieData(uiState)
                        if (binding.pieChart.isEmpty) {
                            binding.pieChart.centerText = ""
                        } else {
                            binding.pieChart.centerText = uiState.sumOfExpences
                        }
                        binding.tvSpentNothing.isVisible = uiState.expenses.isEmpty()
                        binding.pieChart.showLegend()
                    }
            }
        }
    }

    private fun updatePieData(currentState: YearStatUIState) {
        val pieEntries = currentState.expenses
            .map { PieEntry(it.expenseValue.toFloat(), it.name) }
        binding.pieChart.apply {
            data.dataSet.clear()
            pieEntries.forEach {
                data.dataSet.addEntry(it)
            }
            (data.dataSet as PieDataSet).colors = currentState.colors
            (data.dataSet as PieDataSet).setDrawValues(maxVisibleCount <= 15)
            notifyDataSetChanged()
            invalidate()
        }
    }

    private fun PieChart.showLegend() {
        val statItems: MutableList<YearStatItem> = mutableListOf()

        for (i in 0 until data.dataSet.entryCount) {
            val color = data.dataSet.colors[i]
            val entry = data.dataSet.getEntryForIndex(i)
            val percentage = entry.y / data.yValueSum * 100f
            val roundedPercentage = percentage.toBigDecimal().setScale(1, RoundingMode.HALF_EVEN).toDouble()
            statItems += YearStatItem(
                expensesColor = color,
                expensesName = entry.label,
                expensesPercent = roundedPercentage,
                expensesValue = entry.value,
            )
        }
        adapter.submitList(statItems)
    }
}