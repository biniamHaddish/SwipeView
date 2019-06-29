@file:Suppress("unused")

package berhane.biniam.swipy.swipe

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

typealias callback = (Int) -> Unit

class SwipeDecorator<IT : Any>(private val context: Context) {

    var iconDrawable: Drawable? = null
    var backgroundDrawable: ColorDrawable? = null
    var text: String? = null
    var callback: callback? = null
    var textPaint: TextPaint? = null
    private var textBounds: Rect? = null

    /** Sets an optional icon that is shown in the swipe gutter. */
    fun setIcon(
        @DrawableRes res: Int? = null,
        literal: Drawable? = null
    ): SwipeDecorator<IT> {
        require(res != null || literal != null) {
            "Must provide a res or literal value to icon()"
        }
        iconDrawable = literal ?: ContextCompat.getDrawable(context, res!!)
        return this
    }

    /** Sets the color of the swipe gutter. */
    fun setColor(
        @ColorRes res: Int? = null,
        @ColorInt literal: Int? = null
    ): SwipeDecorator<IT> {
        require(res != null || literal != null) {
            "Must provide a res or literal value to color()"
        }
        val colorValue = literal ?: ContextCompat.getColor(context, res!!)
        backgroundDrawable = ColorDrawable(colorValue)
        return this
    }

    /**
     * Callback for the swipe actions on the Items
     */
    fun setCallback(block: callback): SwipeDecorator<IT> {
        this.callback = block
        return this
    }


    internal fun invokeCallback(adapterPos: Int) {
        callback?.invoke(adapterPos)
    }

    /** Sets optional text that is shown in the swipe gutter. */
    fun setText(
        @StringRes res: Int? = null,
        literal: String? = null,
        @ColorRes color: Int = android.R.color.white,
        @DimenRes size: Int = R.dimen.swipe_default_text_size,
        typeface: Typeface? = null,
        @FontRes typefaceRes: Int? = null
    ): SwipeDecorator<IT> {
        require(res != null || literal != null) {
            "Must provide a res or literal value to text()"
        }
        text = literal ?: context.getString(res!!)

        val actualTypeface = when {
            typefaceRes != null -> ResourcesCompat.getFont(context, typefaceRes)
            typeface != null -> typeface
            else -> Typeface.SANS_SERIF
        }
        textPaint = TextPaint().apply {
            this.isAntiAlias = true
            this.color = ContextCompat.getColor(context, color)
            this.typeface = actualTypeface
            this.textSize = context.resources.getDimension(size)
        }

        return this
    }


    fun getTextWidth(): Int = getTextBounds().width()

    internal fun getTextHeight(): Int = getTextBounds().height()

    private fun getTextBounds(): Rect {
        require(text != null) { "text is null" }
        if (textBounds == null) {
            textBounds = Rect()
            textPaint!!.getTextBounds(text, 0, text!!.length, textBounds!!)
        }
        return textBounds!!
    }
}