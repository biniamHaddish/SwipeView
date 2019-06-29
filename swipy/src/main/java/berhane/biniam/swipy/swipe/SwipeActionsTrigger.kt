package berhane.biniam.swipy.swipe


/**
 *  for getting  the swipe action and callback perform the action
 */
interface SwipeActionsTrigger {
    fun swipeAction(isCurrentlyActive: Boolean, index: Int, swipeDir: SwipeDirections)

}