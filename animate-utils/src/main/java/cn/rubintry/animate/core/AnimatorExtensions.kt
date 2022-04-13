package cn.rubintry.animate.core

import android.animation.Animator

import cn.rubintry.animate.core.listener.OnAnimationListener


/**
 * 对[OnAnimationListener]进行简单的扩展
 *
 * @param action
 * @return
 */
inline fun IAnimatorInterface.doOnEnd(crossinline action: () -> Unit) = addListeners(onEnd = action)

inline fun IAnimatorInterface.addListeners(crossinline onEnd: () -> Unit = {}) : OnAnimationListener {
    val listener = object : OnAnimationListener {
        override fun onEnd() = onEnd()
    }
    addListener(listener)
    return listener
}


/**
 * 对[Animator.AnimatorListener]进行简单的扩展
 *
 * @param action
 * @return
 */
inline fun Animator.doOnEnd(crossinline action: (animator: Animator) -> Unit) = addListener(onEnd = action)

inline fun Animator.addListener(
    crossinline onEnd: (animator: Animator) -> Unit = {},
    crossinline onStart: (animator: Animator) -> Unit = {},
    crossinline onCancel: (animator: Animator) -> Unit = {},
    crossinline onRepeat: (animator: Animator) -> Unit = {}
): Animator.AnimatorListener {
    val listener = object : Animator.AnimatorListener {
        override fun onAnimationRepeat(animator: Animator) = onRepeat(animator)
        override fun onAnimationEnd(animator: Animator) = onEnd(animator)
        override fun onAnimationCancel(animator: Animator) = onCancel(animator)
        override fun onAnimationStart(animator: Animator) = onStart(animator)
    }
    addListener(listener)
    return listener
}