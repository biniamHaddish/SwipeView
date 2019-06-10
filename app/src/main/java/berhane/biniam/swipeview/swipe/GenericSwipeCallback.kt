package berhane.biniam.swipeview.swipe

import android.graphics.Canvas
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.RecyclerView
import berhane.biniam.swipeview.swipe.SwipeDirections.*



internal const val ACTION_NAME = "swipe_actions"

internal data class ActionKey(

    val direction: SwipeDirections
)


@Suppress("IMPLICIT_CAST_TO_ANY")
abstract class GenericSwipeCallback(
    private var swipeLeft: SwipeAction? = null,
    private var swipeRight: SwipeAction? = null
) : ItemTouchHelper.Callback() {

    private val BASE_ALPHA = 1.0f
    private var isViewBeingCleared = false

    private var leftDistance: Float = -1f
    private var leftIsLong: Boolean = false
    private var rightDistance: Float = -1f
    private var rightIsLong: Boolean = false

    companion object {
        private const val DEFAULT_WIDTH = -1f
        private const val LONG_SWIPE_THRESHOLD_PERCENT = 45f / 100f
    }

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int = makeMovementFlags(ACTION_STATE_IDLE, getAvailableDirections())

    abstract fun getAvailableDirections(): Int

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = false

    override fun onSwiped(
        viewHolder: RecyclerView.ViewHolder,
        direction: Int
    ) {

        val adapterPosition = viewHolder.adapterPosition
        when (direction) {
            LEFT -> {
                val leftDirection = if (leftIsLong) LONG_LEFT else LEFT
                Log.e("Directions:", "$leftDirection")
            }
            RIGHT -> {
                val rightDirection = if (rightIsLong) LONG_RIGHT else RIGHT
                Log.e("Directions:", "$rightDirection")
            }
            else -> throw IllegalStateException("Unknown direction What is that ?: $direction")
        }
//        return when (direction) {
//            LEFT -> onSwipeLeft(adapterPosition)
//            START -> onSwipeLeft(adapterPosition)
//            RIGHT -> onSwipeRight(adapterPosition)
//            END -> onSwipeRight(adapterPosition)
//            else -> {
//            }
//        }

        leftDistance = DEFAULT_WIDTH
        rightDistance = DEFAULT_WIDTH
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        isViewBeingCleared = true
        val view = viewHolder.itemView
        ItemTouchHelper.Callback.getDefaultUIUtil().clearView(view)
        view.alpha = BASE_ALPHA
    }

    override fun onChildDraw(
        canvas: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
        dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean
    ) {
        val itemView = viewHolder.itemView
        val index = viewHolder.adapterPosition
        if (index == -1) return


        when {
            // right
            dX > 0 -> {
                rightDistance = dX
                rightIsLong = rightDistance >= (LONG_SWIPE_THRESHOLD_PERCENT * itemView.measuredWidth)
            }
        }
        if (isViewBeingCleared) {
            isViewBeingCleared = false
        } else {
            if (actionState == ACTION_STATE_SWIPE) {
                if (viewHolder.adapterPosition == -1) {
                    return
                }

                processSwipeAction(canvas, itemView, dX)
            } else {
                ItemTouchHelper.Callback.getDefaultUIUtil()
                    .onDraw(canvas, recyclerView, itemView, dX, dY, actionState, isCurrentlyActive)
            }
        }
    }

    private fun processSwipeAction(canvas: Canvas, itemView: View, dX: Float) {
        hideItemDependingOnDx(dX, itemView)

        if (getSwipeDirection(dX) == RIGHT) {
            drawBackgroundWhenSwipeRight(canvas, dX, itemView)
            drawSwipeRightIcon(canvas, itemView)
        } else {
            drawBackgroundWhenSwipeLeft(canvas, dX, itemView)
            drawSwipeLeftIcon(canvas, itemView)
        }
    }

    private fun hideItemDependingOnDx(dX: Float, itemView: View) {
        val alpha = BASE_ALPHA - Math.abs(dX) / itemView.width.toFloat()
        itemView.alpha = alpha
        itemView.translationX = dX
    }

    private fun drawBackgroundWhenSwipeLeft(c: Canvas, dX: Float, itemView: View) {
        swipeLeft?.background?.apply {
            setBounds((itemView.right + dX).toInt(), itemView.top, itemView.right, itemView.bottom)
            draw(c)
        }
    }

    private fun drawBackgroundWhenSwipeRight(c: Canvas, dX: Float, itemView: View) {
        swipeRight?.background?.apply {
            setBounds(0, itemView.top, (itemView.left + dX).toInt(), itemView.bottom)
            draw(c)
        }
    }

    private fun getSwipeDirection(dX: Float) = if (dX > 0) RIGHT else LEFT

    private fun drawSwipeLeftIcon(c: Canvas, itemView: View) {

        val margin = swipeLeft?.margin ?: 0
        swipeLeft?.icon?.let {
            val itemMiddlePoint = getItemMiddlePoint(itemView)
            val intrinsicHalfHeight = it.intrinsicHeight / 2
            val intrinsicWidth = it.intrinsicWidth

            val right = itemView.right - margin
            val left = right - intrinsicWidth
            val top = itemMiddlePoint - intrinsicHalfHeight
            val bottom = itemMiddlePoint + intrinsicHalfHeight

            it.setBounds(left, top, right, bottom)
            it.draw(c)
        }
    }

    private fun drawSwipeRightIcon(c: Canvas, itemView: View) {
        val margin = swipeRight?.margin ?: 0
        swipeRight?.icon?.let {
            val itemMiddlePoint = getItemMiddlePoint(itemView)
            val intrinsicHalfHeight = it.intrinsicHeight / 2
            val intrinsicWidth = it.intrinsicWidth

            val left = itemView.left + margin
            val right = left + intrinsicWidth
            val top = itemMiddlePoint - intrinsicHalfHeight
            val bottom = itemMiddlePoint + intrinsicHalfHeight

            it.setBounds(left, top, right, bottom)
            it.draw(c)
        }
    }

    private fun getItemMiddlePoint(itemView: View) = (itemView.bottom + itemView.top) / 2

    private fun onSwipeLeft(adapterPosition: Int) {
        swipeLeft?.action?.invoke(adapterPosition)
    }


    private fun onSwipeRight(adapterPosition: Int) {
        swipeRight?.action?.invoke(adapterPosition)
    }
}