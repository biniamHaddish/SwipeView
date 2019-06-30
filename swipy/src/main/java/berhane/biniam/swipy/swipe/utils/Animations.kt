package berhane.biniam.swipy.swipe.utils

import android.animation.Animator
import android.animation.TimeInterpolator
import android.transition.ChangeBounds
import android.transition.Fade
import android.transition.TransitionSet
import android.util.ArrayMap
import android.view.animation.Interpolator
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import androidx.interpolator.view.animation.FastOutSlowInInterpolator

import java.util.ArrayList

/**
 * borrowed from saket
 */
object Animations {

    val TRANSITION_ANIM_DURATION = 200
    val INTERPOLATOR: Interpolator = FastOutSlowInInterpolator()

    val FAST_OUT_SLOW_IN_INTERPOLATOR = INTERPOLATOR
    val FAST_OUT_LINEAR_IN_INTERPOLATOR: Interpolator = FastOutLinearInInterpolator()

    fun transitions(): TransitionSet {
        return TransitionSet()
            .addTransition(ChangeBounds().setInterpolator(Animations.INTERPOLATOR))
            .addTransition(Fade(Fade.IN).setInterpolator(Animations.INTERPOLATOR))
            .addTransition(Fade(Fade.OUT).setInterpolator(Animations.INTERPOLATOR))
            .setOrdering(TransitionSet.ORDERING_TOGETHER)
            .setDuration(TRANSITION_ANIM_DURATION.toLong())
    }

    /**
     * https://halfthought.wordpress.com/2014/11/07/reveal-transition/
     *
     *
     * Interrupting Activity transitions can yield an OperationNotSupportedException when the
     * transition tries to pause the animator. Yikes! We can fix this by wrapping the Animator:
     */
    class NoPauseAnimator(private val mAnimator: Animator) : Animator() {
        private val mListeners = ArrayMap<Animator.AnimatorListener, Animator.AnimatorListener>()

        override fun addListener(listener: Animator.AnimatorListener) {
            val wrapper = AnimatorListenerWrapper(this, listener)
            if (!mListeners.containsKey(listener)) {
                mListeners[listener] = wrapper
                mAnimator.addListener(wrapper)
            }
        }

        override fun cancel() {
            mAnimator.cancel()
        }

        override fun end() {
            mAnimator.end()
        }

        override fun getDuration(): Long {
            return mAnimator.duration
        }

        override fun getInterpolator(): TimeInterpolator {
            return mAnimator.interpolator
        }

        override fun setInterpolator(timeInterpolator: TimeInterpolator) {
            mAnimator.interpolator = timeInterpolator
        }

        override fun getListeners(): ArrayList<Animator.AnimatorListener> {
            return ArrayList(mListeners.keys)
        }

        override fun getStartDelay(): Long {
            return mAnimator.startDelay
        }

        override fun setStartDelay(delayMS: Long) {
            mAnimator.startDelay = delayMS
        }

        override fun isPaused(): Boolean {
            return mAnimator.isPaused
        }

        override fun isRunning(): Boolean {
            return mAnimator.isRunning
        }

        override fun isStarted(): Boolean {
            return mAnimator.isStarted
        }

        /* We don't want to override pause or resume methods because we don't want them
         * to affect mAnimator.
        public void pause();

        public void resume();

        public void addPauseListener(AnimatorPauseListener listener);

        public void removePauseListener(AnimatorPauseListener listener);
        */

        override fun removeAllListeners() {
            mListeners.clear()
            mAnimator.removeAllListeners()
        }

        override fun removeListener(listener: Animator.AnimatorListener) {
            val wrapper = mListeners[listener]
            if (wrapper != null) {
                mListeners.remove(listener)
                mAnimator.removeListener(wrapper)
            }
        }

        override fun setDuration(durationMS: Long): Animator {
            mAnimator.duration = durationMS
            return this
        }

        override fun setTarget(target: Any?) {
            mAnimator.setTarget(target)
        }

        override fun setupEndValues() {
            mAnimator.setupEndValues()
        }

        override fun setupStartValues() {
            mAnimator.setupStartValues()
        }

        override fun start() {
            mAnimator.start()
        }
    }

    private class AnimatorListenerWrapper internal constructor(
        private val mAnimator: Animator,
        private val mListener: Animator.AnimatorListener
    ) : Animator.AnimatorListener {

        override fun onAnimationStart(animator: Animator) {
            mListener.onAnimationStart(mAnimator)
        }

        override fun onAnimationEnd(animator: Animator) {
            mListener.onAnimationEnd(mAnimator)
        }

        override fun onAnimationCancel(animator: Animator) {
            mListener.onAnimationCancel(mAnimator)
        }

        override fun onAnimationRepeat(animator: Animator) {
            mListener.onAnimationRepeat(mAnimator)
        }
    }
}
