
@file:Suppress("unused")
package berhane.biniam.swipy.swipe

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat



@DslMarker
annotation class SwipyDsl
class SwipeActionBuilders {

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


        fun callback(block: (Int) -> Unit): SwipeActionBuilders {
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