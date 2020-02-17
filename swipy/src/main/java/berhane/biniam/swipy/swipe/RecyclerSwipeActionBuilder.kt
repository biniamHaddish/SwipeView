@file:Suppress("unused")

package berhane.biniam.swipy.swipe

import android.content.Context
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/** @author Biniam Berhane **/
@SwipyDsl
class SwipeBuilder {

    private var swipeLeftBuilder: SwipeActionBuilders? = null
    private var swipeRightBuilder: SwipeActionBuilders? = null
    private var swipeLongRightBuilder: SwipeActionBuilders? = null
    private var swipeLongLeftBuilder: SwipeActionBuilders? = null

    companion object {

        const val LEFT_LONG = 4
        const val RIGHT_LONG = 3

        fun setUp(swipeBuilder: SwipeBuilder): ItemTouchHelper.Callback? {

            val swipeLeftAction = swipeBuilder.swipeLeftBuilder?.build()
            val swipeRightAction = swipeBuilder.swipeRightBuilder?.build()
            val swipeLongRightAction = swipeBuilder.swipeLongRightBuilder?.build()
            val swipeLongLeftAction = swipeBuilder.swipeLongLeftBuilder?.build()

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
     * long Left Swipe Action for the RecyclerView view-holder
     */
    fun longLeft(setup: SwipeActionBuilders.() -> Unit) {
        this.swipeLongLeftBuilder = SwipeActionBuilders().apply(setup)
    }
}


/****/
fun RecyclerView.whenSwipedTo(
    context: Context,
    block: SwipeBuilder.() -> Unit
) {

    with(SwipeBuilder()) {

        block()
        SwipeBuilder.setUp(this)
    }?.let {
        this.layoutManager = LinearLayoutManager(context)
        this.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        ItemTouchHelper(it).attachToRecyclerView(this)
    }
}

inline fun <reified IT : Any> RecyclerView.whenSwipedOn(
    context: Context,
    noinline  block: SwipeBuilder.() -> Unit
) {
    @Suppress("UNCHECKED_CAST")
    return whenSwipedTo(
        context=context,
        block = block
    )
}

