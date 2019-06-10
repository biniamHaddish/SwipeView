package berhane.biniam.swipeview.swipe

import android.graphics.drawable.Drawable

class SwipeAction(
    val background: Drawable? = null,
    val icon: Drawable? = null,
    val margin: Int? = 0,
    val action: (Int) -> Unit
)