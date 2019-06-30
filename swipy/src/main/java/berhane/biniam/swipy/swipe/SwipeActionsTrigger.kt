package berhane.biniam.swipy.swipe

/**  @author Biniam Berhane **/
/** Getting the swipe action and callback perform the action **/
interface SwipeActionsTrigger {
    fun swipeAction(isCurrentlyActive: Boolean, index: Int, swipeDir: SwipeDirections)
}