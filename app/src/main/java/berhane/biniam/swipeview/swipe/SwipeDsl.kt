package berhane.biniam.swipeview.swipe

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

enum class SwipeDirections {

    LEFT,
    RIGHT,
    LONG_RIGHT,
    LONG_LEFT
}
@DslMarker
annotation class SwipeDsl

@SwipeDsl
class SwipeActionBuilder {

    var color: Int? = null
    var background: Drawable? = null
    var icon: Drawable? = null
    var iconMargin: Int = 0

    lateinit var action: (Int) -> Unit

    fun build(): SwipeAction = SwipeAction(
        background ?: color?.let { ColorDrawable(it) }, icon, iconMargin, action
    )
}

@SwipeDsl
class SwipeBuilder {

    var swipeLeftBuilder: SwipeActionBuilder? = null
    var swipeRightBuilder: SwipeActionBuilder? = null

    fun build(): ItemTouchHelper.Callback? {

        val swipeLeftAction = swipeLeftBuilder?.build()
        val swipeRightAction = swipeRightBuilder?.build()

        return object : GenericSwipeCallback(swipeLeftAction, swipeRightAction) {
            override fun getAvailableDirections(): Int {
                return when {
                    swipeLeftAction != null && swipeRightAction != null -> ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                    swipeLeftAction != null -> ItemTouchHelper.LEFT
                    swipeRightAction != null -> ItemTouchHelper.RIGHT
                    else -> 0
                }
            }

        }
    }


    fun left(setup: SwipeActionBuilder.() -> Unit) {
        this.swipeLeftBuilder = SwipeActionBuilder().apply(setup)
    }


    fun right(setup: SwipeActionBuilder.() -> Unit) {
        this.swipeRightBuilder = SwipeActionBuilder().apply(setup)
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

fun RecyclerView.swipe(setup: SwipeBuilder.() -> Unit) {
    with(SwipeBuilder()) {
        setup()
        build()
    }?.let {
        ItemTouchHelper(it).attachToRecyclerView(this)
    }
}

fun RecyclerView.leftSwipe(setup: SwipeActionBuilder.() -> Unit) {
    with(SwipeBuilder()) {
        swipeLeftBuilder = SwipeActionBuilder().apply(setup)
        build()
    }?.let {
        ItemTouchHelper(it).attachToRecyclerView(this)
    }
}

fun RecyclerView.rightSwipe(setup: SwipeActionBuilder.() -> Unit) {
    with(SwipeBuilder()) {
        swipeRightBuilder = SwipeActionBuilder().apply(setup)
        build()
    }?.let {
        ItemTouchHelper(it).attachToRecyclerView(this)
    }
}