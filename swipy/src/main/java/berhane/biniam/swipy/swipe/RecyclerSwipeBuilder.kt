@file:Suppress("unused")

package berhane.biniam.swipy.swipe

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView


@DslMarker
annotation class SwipeDsl

@SwipeDsl
class SwipeActionBuilder {

    var color: Int? = null
    var background: Drawable? = null
    var icon: Drawable? = null
    var iconMargin: Int = 35
    var text: String? = null
    lateinit var callback: (Int) -> Unit

    fun Context.setIcon(
        @DrawableRes drawableRes: Int? = null,
        drawable: Drawable? = null
    ) {
        require(drawableRes != null || drawable != null) {
            " Provide a drawableRes or drawable value to setIcon()"
        }
        icon = drawable ?: ContextCompat.getDrawable(this, drawableRes!!)
    }

    /**
     * We can be able to set the Color of the Slide gutter
     */
    fun Context.setColor(
        @ColorRes colorRes: Int? = null,
        @ColorInt colorInt: Int? = null
    ) {
        require(colorRes != null || colorInt != null) {
            " Provide a ColorRes or colorInt value to setColor()"
        }
        val colorValue = colorInt ?: ContextCompat.getColor(this, colorRes!!)
        background = ColorDrawable(colorValue)

    }


    fun callback(block: (Int) -> Unit): SwipeActionBuilder {
        this.callback = block
        return this
    }

    fun build(): SwipeAction {

        return SwipeAction(
            text,
            background ?: color?.let { ColorDrawable(it) },
            icon,
            iconMargin,
            callback
        )
    }

}

@SwipeDsl
class SwipeBuilder {

    companion object {
        const val LEFT_LONG = 4
        const val RIGHT_LONG = 3
    }

    private var swipeLeftBuilder: SwipeActionBuilder? = null
    private var swipeRightBuilder: SwipeActionBuilder? = null
    private var swipeLongRightBuilder: SwipeActionBuilder? = null
    private var swipeLongLeftBuilder: SwipeActionBuilder? = null


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

fun RecyclerView.whenSwipedTo(
    setup: SwipeBuilder.() -> Unit
) {
    with(berhane.biniam.swipy.swipe.SwipeBuilder()) {
        setup()
        build()
    }?.let {
        ItemTouchHelper(it).attachToRecyclerView(this)
    }
}

