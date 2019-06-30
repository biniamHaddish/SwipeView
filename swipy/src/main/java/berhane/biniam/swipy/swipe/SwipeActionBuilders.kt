@file:Suppress("unused")

package berhane.biniam.swipy.swipe

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.text.TextPaint
import androidx.annotation.*
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import berhane.biniam.swipy.R


/** @author Biniam Berhane **/
@DslMarker
annotation class SwipyDsl

class SwipeActionBuilders {

    var color: Int? = null
    var background: Drawable? = null
    var icon: Drawable? = null
    var text: String? = null
    var textPaint: TextPaint? = null
    private var textBounds: Rect? = null
    var iconMargin: Int = 35
    lateinit var callback: (Int) -> Unit
    /** Apply Icon  **/
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
     * Applying color to the  sliding gutter
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



    /**callback when the swiped **/
    fun callback(block: (Int) -> Unit): SwipeActionBuilders {
        this.callback = block
        return this
    }

    fun build(): SwipeAction {
        return SwipeAction(
            text, background ?: color?.let { ColorDrawable(it) },
            icon,
            iconMargin,
            callback
        )
    }
}