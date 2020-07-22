package com.syject.eqally.utils

import android.animation.Animator
import android.view.View

class AnimationUtils {

    companion object {
        fun alphaAnimation(view: View, onEndListener: OnAnimationEndListener?, alpha: Float) {
            view.animate()
                .alpha(alpha)
                .setListener(getAnimationListener(onEndListener))
                .duration = 200
        }

        fun scaleAnimation(view: View, onEndListener: OnAnimationEndListener?, scale: Float) {
            view.animate()
                .scaleX(scale)
                .scaleY(scale)
                .setListener(getAnimationListener(onEndListener))
                .duration = 600
        }

        private fun getAnimationListener(onEndListener: OnAnimationEndListener?) =
            object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {

                }

                override fun onAnimationEnd(animation: Animator?) {
                    onEndListener?.onAnimationEnd()
                }

                override fun onAnimationCancel(animation: Animator?) {

                }

                override fun onAnimationStart(animation: Animator?) {

                }
            }
    }

    interface OnAnimationEndListener {
        fun onAnimationEnd()
    }
}