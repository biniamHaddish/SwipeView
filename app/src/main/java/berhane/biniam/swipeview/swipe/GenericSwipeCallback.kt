


package berhane.biniam.swipeview.swipe

import android.graphics.Canvas
import android.util.Log
import android.view.View
import androidx.annotation.RestrictTo
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs

enum class SwipeDirections {
    LEFT,
    RIGHT,
    LEFT_LONG,
    RIGHT_LONG,
    IDLE
}


internal const val ACTION_NAME = "swipe_actions"

internal data class ActionKey(
    val direction: SwipeDirections
)


@Suppress("IMPLICIT_CAST_TO_ANY")
abstract class GenericSwipeCallback(
    private var swipeLeft: SwipeAction? = null,
    private var swipeRight: SwipeAction? = null,
    private var swipeLongRight: SwipeAction? = null,
    private var swipeLongLeft: SwipeAction? = null
) : ItemTouchHelper.Callback() {

    // internal val actions: MutableMap<ActionKey, String> = mutableMapOf()

    private val BASE_ALPHA = 1.0f
    private var isViewBeingCleared = false

    private var leftDistance: Float = -1f
    private var leftIsLong: Boolean = false
    private var rightDistance: Float = -1f
    private var rightIsLong: Boolean = false

    companion object {
        private const val DEFAULT_POSITION = -1f
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

    // Will Enable the Swipe Action
    override fun isItemViewSwipeEnabled(): Boolean {
        return true
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    override fun onSwiped(
        viewHolder: RecyclerView.ViewHolder,
        direction: Int
    ) {
        val position = viewHolder.adapterPosition

        return when (direction) {
            LEFT -> {
                //onSwipeLeftAction(position)
                if (leftIsLong) {
                    onSwipeLongLeftAction(position)

                } else {
                    onSwipeLeftAction(position)
                }
            }
            START -> {
                onSwipeLeftAction(position)
            }
            RIGHT -> {
                onSwipeRightAction(position)
                if (rightIsLong) onSwipeLongRightAction(position) else onSwipeRightAction(position)
            }
            END -> {
                onSwipeRightAction(position)
            }
            else -> {
                throw IllegalStateException("Unknown direction What is that ?: $direction")
            }
        }

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
                super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        }
    }


    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    private fun processSwipeAction(canvas: Canvas, itemView: View, dX: Float) {
        hideItemDependingOnDx(dX, itemView)

        when {
            getSwipeDirection(dX, itemView) == SwipeDirections.RIGHT_LONG -> {
                drawBackgroundWhenSwipeLongRight(canvas, dX, itemView)
                drawSwipeLongRightIcon(canvas, itemView)
            }
            getSwipeDirection(dX, itemView) == SwipeDirections.RIGHT -> {
                drawBackgroundWhenSwipeRight(canvas, dX, itemView)
                drawSwipeRightIcon(canvas, itemView)
            }
            getSwipeDirection(dX, itemView) == SwipeDirections.LEFT_LONG -> {
                drawBackgroundWhenSwipeLongLeft(canvas, dX, itemView)
                drawSwipeLongLeftIcon(canvas, itemView)
            }
            getSwipeDirection(dX, itemView) == SwipeDirections.LEFT -> {
                drawBackgroundWhenSwipeLeft(canvas, dX, itemView)
                drawSwipeLeftIcon(canvas, itemView)
            }
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

    private fun drawBackgroundWhenSwipeLongRight(c: Canvas, dX: Float, itemView: View) {
        val actualRight = itemView.left + dX.toInt()
        swipeLongRight?.background?.apply {
            setBounds(
                itemView.left,
                itemView.top,
                actualRight,
                itemView.bottom
            )
            draw(c)
        }
    }

    private fun drawBackgroundWhenSwipeLongLeft(c: Canvas, dX: Float, itemView: View) {

        val newLeft = itemView.right + dX.toInt()
        swipeLongLeft?.background?.apply {
            setBounds(
                newLeft,
                itemView.top,
                itemView.right,
                itemView.bottom
            )
            draw(c)
        }
    }

    private fun drawBackgroundWhenSwipeRight(c: Canvas, dX: Float, itemView: View) {
        swipeRight?.background?.apply {
            setBounds(0, itemView.top, (itemView.left + dX).toInt(), itemView.bottom)
            draw(c)
        }
    }

    private fun getSwipeDirection(dX: Float, itemView: View): SwipeDirections {

        when {
            // right
            dX > 0 -> {
                rightDistance = dX
                rightIsLong = rightDistance >= (LONG_SWIPE_THRESHOLD_PERCENT * itemView.measuredWidth)
                return if (rightIsLong) {
                    Log.e("Long Right", "Long Right....")

                    SwipeDirections.RIGHT_LONG
                } else {

                    Log.e("Right", "Right....")
                    SwipeDirections.RIGHT

                }
            }
            //Left
            dX < 0 -> {
                leftDistance = dX
                leftIsLong = abs(leftDistance) >= (LONG_SWIPE_THRESHOLD_PERCENT * itemView.measuredWidth)
                return if (leftIsLong) {
                    Log.e("Long Left", "Long Left....")
                    SwipeDirections.LEFT_LONG
                } else {
                    Log.e("Left", "Left....")
                    SwipeDirections.LEFT
                }
            }

        }

        return SwipeDirections.IDLE

    }

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

    private fun drawSwipeLongLeftIcon(c: Canvas, itemView: View) {
        val margin = swipeLongLeft?.margin ?: 0
        swipeLongLeft?.icon?.let {
            val iconTop = itemView.top + (itemView.height - it.intrinsicHeight) / 2
            val iconBottom = iconTop + it.intrinsicHeight
            val iconRight = itemView.right - margin
            val iconLeft = iconRight - it.intrinsicWidth
            it.setBounds(iconLeft, iconTop, iconRight, iconBottom)
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


    /**
     * Long Right Icon setUp
     */
    private fun drawSwipeLongRightIcon(c: Canvas, itemView: View) {
        val margin = swipeLongRight?.margin ?: 0
        swipeLongRight?.icon?.let {
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

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        return 0.7f
    }


    private fun getItemMiddlePoint(itemView: View) = (itemView.bottom + itemView.top) / 2

    /**
     * Swipe Actions below
     */
    private fun onSwipeLeftAction(adapterPosition: Int) {
        swipeLeft?.action?.invoke(adapterPosition)
    }

    private fun onSwipeLongLeftAction(adapterPosition: Int) {
        swipeLongLeft?.action?.invoke(adapterPosition)
    }


    private fun onSwipeRightAction(adapterPosition: Int) {
        swipeRight?.action?.invoke(adapterPosition)
    }

    private fun onSwipeLongRightAction(adapterPosition: Int) {
        swipeLongRight?.action?.invoke(adapterPosition)
    }

}