package com.example.piggybank.adapters

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.piggybank.R
import com.example.piggybank.adapters.EditIncomeAdapter.EditIncomeItem

class IncomeItemTouchHelperCallback(
    private val adapter: EditIncomeAdapter,
    private val swipeListener: (EditIncomeItem) -> Unit
) : ItemTouchHelper.Callback() {

    private val bgRectangle = RectF()
    private val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val iconBounds = Rect()

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: ViewHolder): Int =
        if (viewHolder is ItemTouchViewHolder) {
            makeFlag(ItemTouchHelper.ACTION_STATE_SWIPE, ItemTouchHelper.START.plus(ItemTouchHelper.END))
        } else {
            makeFlag(ItemTouchHelper.ACTION_STATE_IDLE, ItemTouchHelper.START.plus(ItemTouchHelper.END))
        }

    override fun onMove(recyclerView: RecyclerView, viewHolder: ViewHolder, target: ViewHolder): Boolean = false

    override fun onSwiped(viewHolder: ViewHolder, direction: Int) {
        swipeListener.invoke(adapter.currentList[viewHolder.adapterPosition])
    }

    override fun onSelectedChanged(viewHolder: ViewHolder?, actionState: Int) {
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE && viewHolder is ItemTouchViewHolder) {
            viewHolder.onItemSelected()
        }
        super.onSelectedChanged(viewHolder, actionState)
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: ViewHolder) {
        if (viewHolder is ItemTouchViewHolder) viewHolder.onItemCleared()
        super.clearView(recyclerView, viewHolder)
    }

    override fun onChildDraw(
        canvas: Canvas,
        recyclerView: RecyclerView,
        viewHolder: ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            val itemView = viewHolder.itemView
            drawBackground(canvas, itemView, dX)
            drawItem(canvas, itemView, dX)
        }
        super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    private fun drawItem(canvas: Canvas, itemView: View, swipedX: Float) {
        val icon = ContextCompat.getDrawable(itemView.context, R.drawable.ic_delete_white_24dp) ?: return
        val iconSize = itemView.resources.getDimensionPixelSize(R.dimen.icon_size)

        val margin = (itemView.bottom - itemView.top - iconSize) / 2
        with(iconBounds) {
            top = itemView.top + margin
            bottom = itemView.bottom - margin

            if (swipedX < 0) {
                left = itemView.right - margin - iconSize
                right = itemView.right - margin
            } else if (swipedX > 0) {
                left = itemView.left + margin
                right = itemView.left + margin + iconSize
            }
        }
        icon.bounds = iconBounds
        icon.draw(canvas)
    }

    private fun drawBackground(canvas: Canvas, itemView: View, dX: Float) {
        with(bgRectangle) {
            left = itemView.left.toFloat()
            right = itemView.right.toFloat()
            top = itemView.top.toFloat()
            bottom = itemView.bottom.toFloat()
        }
        with(bgPaint) {
            color = itemView.resources.getColor(R.color.color_pie4, itemView.context.theme)
            canvas.drawRect(bgRectangle, bgPaint)
        }
    }

    interface ItemTouchViewHolder {

        fun onItemSelected()
        fun onItemCleared()
    }
}