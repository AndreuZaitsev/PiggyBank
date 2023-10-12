package com.example.piggybank.fragments

import android.app.DatePickerDialog
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.piggybank.R
import com.example.piggybank.databinding.MonthlyStatBinding
import com.example.piggybank.uistates.MonthlyStatUIState
import com.example.piggybank.viewmodels.MonthlyStatViewModel
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import kotlinx.coroutines.launch

class MonthlyStatFragment : Fragment(R.layout.monthly_stat) {

    private val viewModel: MonthlyStatViewModel by viewModels()

    private var _binding: MonthlyStatBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = MonthlyStatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.monthPikerButton.setOnClickListener {
            setupDatePicker().show()
        }
        observeUIState()
        setUpPieChart()
        viewModel.onDateSelected(Date())
    }

    private fun setupDatePicker(): DatePickerDialog {
        val calendar: Calendar = Calendar.getInstance()
        return DatePickerDialog(
            this.requireContext(),
            { _, year, month, _ ->
                val date = calendar.apply {
                    set(Calendar.YEAR, year)
                    set(Calendar.MONTH, month)
                }.time

                viewModel.onDateSelected(date)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).apply {
            hideDayColumn()
        }
    }

    private fun DatePickerDialog.hideDayColumn() {
        this.datePicker.findViewById<NumberPicker>(resources.getIdentifier("day", "id", "android"))?.isGone = true
    }

    private fun setUpPieChart() {
        binding.pieChart.apply {
            setUsePercentValues(true)
            description.isEnabled = false
            setExtraOffsets(25f, 10f, 25f, 10f)

            // on below line we are setting drag for our pie chart
            dragDecelerationFrictionCoef = 0.95f

            // on below line we are setting hole
            // and hole color for pie chart
            isDrawHoleEnabled = true
            setHoleColor(Color.WHITE)

            // on below line we are setting circle color and alpha
            setTransparentCircleColor(Color.WHITE)
            setTransparentCircleAlpha(110)

            // on  below line we are setting hole radius
            holeRadius = 55f
            transparentCircleRadius = 60f

            // on below line we are setting center text
            setDrawCenterText(true)

            setCenterTextSize(25f)

            // on below line we are setting
            // rotation for our pie chart
            rotationAngle = 0f

            // enable rotation of the pieChart by touch
            isRotationEnabled = true
            isHighlightPerTapEnabled = true

            // on below line we are setting animation for our pie chart
            animateY(1400, Easing.EaseInOutQuad)

            // on below line we are disabling our legend for pie chart
            legend.isEnabled = false
            this.setDrawEntryLabels(false)
        }
        // on below line we are creating array list and
        // adding data to it to display in pie chart
        // on below line we are setting pie data set
        val dataSet = PieDataSet(mutableListOf(), "Mobile OS").apply {
            // on below line we are setting icons.
            setDrawIcons(false)

            // on below line we are setting slice for pie
            sliceSpace = 3f
            iconsOffset = MPPointF(0f, 40f)
            selectionShift = 5f

        }

        // on below line we are setting pie data set
        val data = PieData(dataSet).apply {
            dataSet.colors = ColorTemplate.COLORFUL_COLORS.toList()
            dataSet.xValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
            dataSet.yValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
            setValueTextSize(15f)
            setValueTypeface(Typeface.DEFAULT_BOLD)
            setValueTextColor(Color.BLACK)
        }

        binding.pieChart.apply {
            this.data = data
            // undo all highlights
            data.setValueFormatter(PercentFormatter(this))
            this.setUsePercentValues(true)

            highlightValues(null)

            // loading chart
            invalidate()
        }
    }

    private fun observeUIState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    updatePieData(uiState)
                    binding.pieChart.centerText = uiState.sumOfExpences
                    binding.currentMonth.text = uiState.selectedDate
                }
            }
        }
    }

    private fun updatePieData(currentState: MonthlyStatUIState) {
        val pieEntries = currentState.expenses
            .map { PieEntry(it.expenseValue.toFloat(), it.name) }

        binding.pieChart.apply {
            data.dataSet.clear()
            pieEntries.forEach {
                data.dataSet.addEntry(it)
            }
            notifyDataSetChanged()
            invalidate()
        }
    }
}

