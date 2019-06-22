package berhane.biniam.swipeview.swipe

import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.text.TextPaint
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView


@DslMarker
annotation class SwipeDsl

@SwipeDsl
class SwipeActionBuilder {

    var color: Int? = null
    var background: Drawable? = null
    var icon: Drawable? = null
    var iconMargin: Int = 0

    // private lateinit var context: Context

    internal var text: String? = null
    internal var textPaint: TextPaint? = null
    private var textBounds: Rect? = null

    lateinit var callback: (Int) -> Unit

    fun build(): SwipeAction = SwipeAction(
        text, background ?: color?.let { ColorDrawable(it) }, icon, iconMargin, callback
    )



}

@SwipeDsl
class SwipeBuilder {

    var swipeLeftBuilder: SwipeActionBuilder? = null
    var swipeRightBuilder: SwipeActionBuilder? = null
    var swipeLongRightBuilder: SwipeActionBuilder? = null
    var swipeLongLeftBuilder: SwipeActionBuilder? = null


    fun build(): ItemTouchHelper.Callback? {

        val swipeLeftAction = swipeLeftBuilder?.build()
        val swipeRightAction = swipeRightBuilder?.build()
        val swipeLongRightAction = swipeLongRightBuilder?.build()
        val swipeLongLeftAction = swipeLongLeftBuilder?.build()

        return object : SwipeListenerCallback(
            swipeLeftAction,
            swipeRightAction,
            swipeLongRightAction,
            swipeLongLeftAction) {

            override fun getAvailableDirections(): Int {
                return when {
                    swipeLeftAction != null && swipeRightAction != null -> ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                    swipeLeftAction != null -> ItemTouchHelper.LEFT
                    swipeRightAction != null -> ItemTouchHelper.RIGHT
                    swipeLongRightAction != null -> SwipeDirctions.RIGHT_LONG
                    swipeLongLeftAction != null -> SwipeDirctions.LEFT_LONG
                    else -> 0
                }
            }

        }
    }

    /**
     * When swiped to left
     */
    fun left(setup: SwipeActionBuilder.() -> Unit) {
        this.swipeLeftBuilder = SwipeActionBuilder().apply(setup)
    }

    /**
     * When Swiped Right
     */
    fun right(setup: SwipeActionBuilder.() -> Unit) {
        this.swipeRightBuilder = SwipeActionBuilder().apply(setup)
    }

    /**
     * long Right Swipe Action for the RecyclerView
     */
    fun longRight(setup: SwipeActionBuilder.() -> Unit) {
        this.swipeLongRightBuilder = SwipeActionBuilder().apply(setup)
    }

    /**
     * long Left Swipe Action for the RecyclerView
     */
    fun longLeft(setup: SwipeActionBuilder.() -> Unit) {
        this.swipeLongLeftBuilder = SwipeActionBuilder().apply(setup)
    }
}


fun recyclerViewSwipe(recyclerView: RecyclerView, setup: SwipeBuilder.() -> Unit) {
    with(SwipeBuilder()) {
        setup()
        build()
    }?.let {
        ItemTouchHelper(it).attachToRecyclerView(recyclerView)
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
