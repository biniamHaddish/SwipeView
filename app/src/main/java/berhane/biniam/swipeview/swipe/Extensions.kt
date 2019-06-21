package berhane.biniam.swipeview.swipe

import android.content.Context
import android.content.res.Resources
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat

fun Context.getColorCompat(@ColorRes resId: Int) =
    ContextCompat.getColor(this, resId)

fun View.getColorCompat(@ColorRes resId: Int) =
    context.getColorCompat(resId)


fun Context.getDrawableCompat(@DrawableRes resId: Int) =
    ContextCompat.getDrawable(this, resId)

fun View.getDrawableCompat(@DrawableRes resId: Int) =
    context.getDrawableCompat(resId)


fun Context.getStringCompat(@StringRes resId: Int) : String =
    resources.getString(resId)

fun View.getStringCompat(@StringRes resId: Int) : String =
    context.getStringCompat(resId)

fun Int.toDp() = (this / Resources.getSystem().displayMetrics.density).toInt()

fun Int.toPx() = (this * Resources.getSystem().displayMetrics.density).toInt()
