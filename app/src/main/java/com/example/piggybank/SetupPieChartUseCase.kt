package com.example.piggybank

import android.graphics.Color
import android.graphics.Typeface
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.MPPointF

class SetupPieChartUseCase {

    operator fun invoke(pieChart: PieChart) {
        pieChart.apply {
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
        val dataSet = PieDataSet(mutableListOf(), "").apply {

            setDrawIcons(false)

            // on below line we are setting slice for pie
            sliceSpace = 3f
            iconsOffset = MPPointF(0f, 40f)
            selectionShift = 5f

        }

        // on below line we are setting pie data set
        val data = PieData(dataSet).apply {
            dataSet.colors = emptyList()
            dataSet.yValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
            setValueTextSize(10f)
            setValueTypeface(Typeface.DEFAULT_BOLD)
            setValueTextColor(Color.BLACK)
        }

        pieChart.apply {
            this.data = data

            data.setValueFormatter(PercentFormatter(this))
            this.setUsePercentValues(true)
            //data.setDrawValues(maxVisibleCount < 15)

            // undo all highlights
            highlightValues(null)

            // loading chart
            invalidate()
        }
    }
}