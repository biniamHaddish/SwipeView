package berhane.biniam.swipy.swipe

import android.content.Context
import android.graphics.Canvas
import android.util.Log
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.RestrictTo
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.RecyclerView
import berhane.biniam.swipy.R
import berhane.biniam.swipy.swipe.SwipeDirections.*
import berhane.biniam.swipy.swipe.utils.RippleDirection
import berhane.biniam.swipy.swipe.utils.SwipeTriggerRippleDrawable
import java.security.AccessController.getContext
import kotlin.math.abs

/** @author Biniam Berhane **/
enum class SwipeDirections {
    LEFT,
    RIGHT,
    LONG_LEFT,
    LONG_RIGHT,
    IDLE
}

@Suppress("IMPLICIT_CAST_TO_ANY")
abstract class SwipeActionListener(
    private var swipeLeft: SwipeAction? = null,
    private var swipeRight: SwipeAction? = null,
    private var swipeLongRight: SwipeAction? = null,
    private var swipeLongLeft: SwipeAction? = null

) : ItemTouchHelper.Callback(), SwipeActionsTrigger {

    private val ALPHA = 1.0f
    private var isViewBeingCleared = false
    private var leftDistance: Float = -1f
    private var leftIsLong: Boolean = false
    private var rightDistance: Float = -1f
    private var rightIsLong: Boolean = false

    private val swipeActionTriggerDrawable: SwipeTriggerRippleDrawable
        get() = SwipeTriggerRippleDrawable()

    companion object {
        private const val LONG_SWIPE_THRESHOLD_PERCENT = 40f / 100f
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

    /** Will Enable the Swipe Action**/
    override fun isItemViewSwipeEnabled() = true

    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    override fun onSwiped(
        viewHolder: RecyclerView.ViewHolder,
        direction: Int
    ) {
        /** Now here i have to be able to implement the ripple animation
        after swipe is performed
         **/
        return when (direction) {
            LEFT -> {
                when {
                    leftIsLong -> {
                        // far Left

                    }
                    else -> {
                        //Just Left
                    }
                }
            }
            RIGHT -> {
                when {
                    rightIsLong -> {
                        // far right
                    }
                    else -> {
                        // just Right
                    }
                }
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
        view.alpha = ALPHA
    }

    /**All the drawing stuff is getting done down here on this method **/
    override fun onChildDraw(
        canvas: Canvas, recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float, dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
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
                calculateSwipeVelocityAction(isCurrentlyActive, itemView, index, dX)
                paintSwipeAction(canvas, itemView, dX)
            } else {
                ItemTouchHelper.Callback.getDefaultUIUtil()
                    .onDraw(canvas, recyclerView, itemView, dX, dY, actionState, isCurrentlyActive)
                super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        }
    }

    /*** Calculate the swipe directions and decide the action on that direction***/
    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    private fun calculateSwipeVelocityAction(
        isCurrentlyActive: Boolean,
        itemView: View,
        viewIndex: Int,
        dX: Float
    ) {
        hideItemDependingOnDx(dX, itemView)
        when {
            getSwipeDirection(dX, itemView) == LONG_RIGHT -> {
                // long right interface implementation
                swipeAction(isCurrentlyActive, viewIndex, LONG_RIGHT)
            }
            getSwipeDirection(dX, itemView) == SwipeDirections.RIGHT -> {
                // Called when the View is swiped Right
                swipeAction(isCurrentlyActive, viewIndex, SwipeDirections.RIGHT)
            }
            getSwipeDirection(dX, itemView) == LONG_LEFT -> {
                // called when view Swiped far left
                swipeAction(isCurrentlyActive, viewIndex, LONG_LEFT)
            }
            getSwipeDirection(dX, itemView) == SwipeDirections.LEFT -> {
                // Called when the View is swiped Left
                swipeAction(isCurrentlyActive, viewIndex, SwipeDirections.LEFT)
            }
        }
    }

    /** Start to decorate the Swiped gutter with the give color and icon and later text **/
    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    private fun paintSwipeAction(canvas: Canvas, itemView: View, dX: Float) {
        hideItemDependingOnDx(dX, itemView)
        when {
            getSwipeDirection(dX, itemView) == LONG_RIGHT -> {
                drawBackgroundWhenSwipeLongRight(canvas, dX, itemView)
                drawSwipeLongRightIcon(canvas, itemView)
            }
            getSwipeDirection(dX, itemView) == SwipeDirections.RIGHT -> {
                drawBackgroundWhenSwipeRight(canvas, dX, itemView)
                drawSwipeRightIcon(canvas, itemView)
            }
            getSwipeDirection(dX, itemView) == LONG_LEFT -> {
                drawBackgroundWhenSwipeLongLeft(canvas, dX, itemView)
                drawSwipeLongLeftIcon(canvas, itemView)
            }
            getSwipeDirection(dX, itemView) == SwipeDirections.LEFT -> {
                drawBackgroundWhenSwipeLeft(canvas, dX, itemView)
                drawSwipeLeftIcon(canvas, itemView)
            }
        }
    }



    /** Blurring the View as the slide is progressing **/
    private fun hideItemDependingOnDx(dX: Float, itemView: View) {
        val alpha = ALPHA - abs(dX) / itemView.width.toFloat()
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

    /**Will identify the swipe direction depending to which direction the user is swiping the itew**/
    private fun getSwipeDirection(dX: Float, itemView: View): SwipeDirections {

        when {
            /** Swiped right**/
            dX > 0 -> {
                rightDistance = dX
                rightIsLong = rightDistance >= (LONG_SWIPE_THRESHOLD_PERCENT * itemView.measuredWidth)
                return if (rightIsLong) {
                    LONG_RIGHT
                } else {
                    SwipeDirections.RIGHT
                }
            }
            /**Swiped Left**/
            dX < 0 -> {
                leftDistance = dX
                leftIsLong = abs(leftDistance) >= (LONG_SWIPE_THRESHOLD_PERCENT * itemView.measuredWidth)
                return if (leftIsLong) {
                    LONG_LEFT
                } else {
                    SwipeDirections.LEFT
                }
            }
        }
        return IDLE
    }

    /** Draw Icon to the left of the slide gutter **/
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

    /** Draw Icon to the far-left of the slide gutter **/
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

    /** Draw Icon to the Right of the slide gutter **/
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


    /** Draw Icon to the far-Right of the slide gutter **/
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

    /** Swipe SwipeThreshold for our recyclerView  **/
    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        return 0.7f
    }

    private fun getItemMiddlePoint(itemView: View) = (itemView.bottom + itemView.top) / 2
    /**
     * Swipe Actions depending to the direction below
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


    /** lets do the Swipe action performed on this thing**/
    override fun swipeAction(
        isCurrentlyActive: Boolean,
        index: Int,
        swipeDir: SwipeDirections
    ) {
        when (swipeDir) {
            SwipeDirections.LEFT -> {
                if (!isCurrentlyActive) onSwipeLeftAction(index)
            }
            SwipeDirections.RIGHT -> {
                if (!isCurrentlyActive) onSwipeRightAction(index)
            }
            LONG_RIGHT -> {
                if (!isCurrentlyActive) onSwipeLongRightAction(index)
            }
            LONG_LEFT -> {
                if (!isCurrentlyActive) onSwipeLongLeftAction(index)
            }
            IDLE -> {
                Log.e("IDLE State ", "$isCurrentlyActive")
            }
        }
    }
}