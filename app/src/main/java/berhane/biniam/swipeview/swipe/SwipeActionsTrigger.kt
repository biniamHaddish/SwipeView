package berhane.biniam.swipeview.swipe


/**
 *  for getting  the swipe action and callback perform
 */
 interface SwipeActionsTrigger {
     fun swipeAction(actionState:Int,isCurrentlyActive: Boolean,index:Int,swipeDir:SwipeDirections)
}