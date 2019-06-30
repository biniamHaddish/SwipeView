@file:Suppress("unused")

package berhane.biniam.swipy.swipe

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView


@SwipyDsl
class SwipeBuilder {

    companion object {
        const val LEFT_LONG = 4
        const val RIGHT_LONG = 3
    }

    private var swipeLeftBuilder: SwipeActionBuilders? = null
    private var swipeRightBuilder: SwipeActionBuilders? = null
    private var swipeLongRightBuilder: SwipeActionBuilders? = null
    private var swipeLongLeftBuilder: SwipeActionBuilders? = null


    fun build(): ItemTouchHelper.Callback? {

        val swipeLeftAction = swipeLeftBuilder?.build()
        val swipeRightAction = swipeRightBuilder?.build()
        val swipeLongRightAction = swipeLongRightBuilder?.build()
        val swipeLongLeftAction = swipeLongLeftBuilder?.build()

        return object : SwipeActionListener(
            swipeLeftAction,
            swipeRightAction,
            swipeLongRightAction,
            swipeLongLeftAction
        ) {


            override fun getAvailableDirections(): Int {
                return when {
                    swipeLeftAction != null && swipeRightAction != null -> ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                    swipeLeftAction != null -> ItemTouchHelper.LEFT
                    swipeRightAction != null -> ItemTouchHelper.RIGHT
                    swipeLongRightAction != null -> RIGHT_LONG
                    swipeLongLeftAction != null -> LEFT_LONG
                    else -> 0
                }
            }

        }
    }

    /**
     * When swiped to left
     */
    fun left(setup: SwipeActionBuilders.() -> Unit) {
        this.swipeLeftBuilder = SwipeActionBuilders().apply(setup)
    }

    /**
     * When Swiped Right
     */
    fun right(setup: SwipeActionBuilders.() -> Unit) {
        this.swipeRightBuilder = SwipeActionBuilders().apply(setup)
    }

    /**
     * long Right Swipe Action for the RecyclerView
     */
    fun longRight(setup: SwipeActionBuilders.() -> Unit) {
        this.swipeLongRightBuilder = SwipeActionBuilders().apply(setup)
    }

    /**
     * long Left Swipe Action for the RecyclerView
     */
    fun longLeft(setup: SwipeActionBuilders.() -> Unit) {
        this.swipeLongLeftBuilder = SwipeActionBuilders().apply(setup)
    }
}

fun RecyclerView.whenSwipedTo(
    setup: SwipeBuilder.() -> Unit
) {
    with(SwipeBuilder()) {
        setup()
        build()
    }?.let {
        ItemTouchHelper(it).attachToRecyclerView(this)
    }
}

