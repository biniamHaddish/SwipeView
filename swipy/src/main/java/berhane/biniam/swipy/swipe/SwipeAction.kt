
package berhane.biniam.swipy.swipe

import android.graphics.drawable.Drawable

/** @author Biniam Berhane **/
class SwipeAction(
    val text:String?=null,
    val background: Drawable? = null,
    val icon: Drawable? = null,
    val margin: Int? = 35,
    val action: (Int) -> Unit

)