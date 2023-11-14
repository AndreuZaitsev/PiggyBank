package com.example.piggybank

import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.roundToInt

val Int.dp: Int get() = (this * Resources.getSystem().displayMetrics.density).roundToInt()

class DotsIndicatorDecoration(
    @ColorInt colorInactive: Int,
    @ColorInt colorActive: Int,
) : RecyclerView.ItemDecoration() {

    private val inactivePaint = Paint()
    private val activePaint = Paint()
    private val RecyclerView.activePosition: Int
        get() = when (val lm = layoutManager) {
            is GridLayoutManager ->
                lm.findFirstCompletelyVisibleItemPosition()
                    .coerceAtLeast(0) / lm.spanCount

            is LinearLayoutManager ->
                lm.findFirstCompletelyVisibleItemPosition()

            else ->
                RecyclerView.NO_POSITION
        }

    private val RecyclerView.dotsCount: Int
        get() {
            val itemsCount = adapter?.itemCount ?: 0
            if (cachedItemsCount == itemsCount) return cachedDotsCount
            if (itemsCount <= 0) return 0

            return when (val lm = layoutManager) {
                is GridLayoutManager -> {
                    val itemsOnPage =
                        lm.findLastCompletelyVisibleItemPosition() -
                        lm.findFirstCompletelyVisibleItemPosition() + 1

                    val firstDot = 1
                    val extraItems = itemsCount - itemsOnPage

                    val wholeColumns = extraItems / lm.spanCount
                    val partialColumns = extraItems % lm.spanCount

                    firstDot + wholeColumns + partialColumns
                }

                else -> itemsCount
            }.also {
                cachedDotsCount = it
                cachedItemsCount = itemsCount
            }
        }

    private var cachedItemsCount = 0
    private var cachedDotsCount = 0

    init {
        inactivePaint.style = Paint.Style.FILL
        inactivePaint.isAntiAlias = true
        inactivePaint.color = colorInactive

        activePaint.style = Paint.Style.FILL_AND_STROKE
        activePaint.strokeCap = Paint.Cap.ROUND
        activePaint.strokeWidth = RADIUS.toFloat()
        activePaint.isAntiAlias = true
        activePaint.color = colorActive
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)

        parent.adapter ?: return

        val dotsCount = parent.dotsCount

        // center horizontally, calculate width and subtract half from center
        val totalLength = (RADIUS * 2 * dotsCount).toFloat()
        val paddingBetweenItems =
            (0.coerceAtLeast(dotsCount - 1) * INDICATOR_ITEM_PADDING).toFloat()
        val indicatorTotalWidth = totalLength + paddingBetweenItems
        val indicatorStartX = (parent.width - indicatorTotalWidth) / 2f

        // center vertically in the allotted space
        val indicatorPosY = parent.height - INDICATOR_HEIGHT / 2f

        drawInactiveDots(c, indicatorStartX, indicatorPosY, dotsCount, parent.activePosition)
        drawActiveDot(c, indicatorStartX, indicatorPosY, parent.activePosition)
    }

    private fun drawInactiveDots(
        c: Canvas,
        indicatorStartX: Float,
        indicatorPosY: Float,
        dotsCount: Int,
        activeIndex: Int,
    ) {
        // width of item indicator including padding
        val dotWidth = (RADIUS * 2 + INDICATOR_ITEM_PADDING).toFloat()
        var start = indicatorStartX + RADIUS
        for (i in 0 until dotsCount) {
            //
            c.drawCircle(start, indicatorPosY, RADIUS.toFloat(), inactivePaint)
            start += dotWidth
        }
    }

    private fun drawActiveDot(
        c: Canvas,
        indicatorStartX: Float,
        indicatorPosY: Float,
        highlightPosition: Int,
    ) {
        // width of item indicator including padding
        val dotWidth = (RADIUS * 2) + INDICATOR_ITEM_PADDING
        val dotOffset = dotWidth * highlightPosition
        val highlightStart = indicatorStartX + RADIUS + dotOffset
        c.drawCircle(highlightStart, indicatorPosY, RADIUS.toFloat(), activePaint)
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.bottom = INDICATOR_HEIGHT
    }

    companion object {

        private val INDICATOR_HEIGHT: Int = 20.dp
        private val INDICATOR_ITEM_PADDING: Int = 6.dp
        private val RADIUS: Int = 4.dp
    }
}