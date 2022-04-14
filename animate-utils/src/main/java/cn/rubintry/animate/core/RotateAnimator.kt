package cn.rubintry.animate.core

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.View
import android.view.animation.Interpolator
import cn.rubintry.animate.core.ext.doOnEnd

import cn.rubintry.animate.core.listener.OnAnimationListener


/**
 * 作   者:  卢孙仲
 * 创建日期:  2022年3月16日
 * 描   述: 转动动画
 * 页面中文名:
 */
final class RotateAnimator(view : View?, angle: Float, val reverse: Boolean) : BaseAnimator(view) ,
    IAnimatorInterface {
    private var isDestroy = false
    
    private var angleAnimator : ObjectAnimator = ObjectAnimator.ofFloat(view , "rotation" , -angle , angle)


    companion object{

        /**
         * 获取旋转动画
         *
         * @param view 要操作的视图
         * @param angle 旋转角度
         * @param reverse 旋转完一次后是否需要反向转回来
         * @return
         */
        @JvmStatic
        fun obtain(view: View?, angle: Float, reverse: Boolean): IAnimatorInterface {
            return RotateAnimator(view , angle , reverse)
        }
    }


    init {
        set.playTogether(angleAnimator)
        if(reverse){
            angleAnimator.repeatMode = ValueAnimator.REVERSE
        }
    }


    override fun onActivityDestroyed() {
        isDestroy = true
        set.cancel()
    }


    override fun onActivityResume() {
        set.resume()
    }

    override fun onActivityPause() {
        set.pause()
    }

    override fun enableLoop(loop: Boolean): IAnimatorInterface {
        if(loop){
            this.loop = true
            allRepeatInfinity()
        }
        return this
    }

    override fun isLoop(): Boolean {
        return loop
    }


    /**
     * 对当前所有动画开启无限循环
     *
     */
    private fun allRepeatInfinity(){
        try {
            for (animation in set.childAnimations) {
                (animation as ValueAnimator).repeatCount = ValueAnimator.INFINITE
            }
        }catch (ex: Exception){
            ex.printStackTrace()
        }
    }


    override fun start() {
        set.start()
        set.doOnEnd {
            if(isDestroy){
                cancel()
                finished = true
                listeners.forEach { listener ->
                    listener.onEnd()
                }
                return@doOnEnd
            }
            finished = true
            listeners.forEach { listener ->
                listener.onEnd()
            }
        }
    }

    override fun reset() {
        if(isRunning()){
            throw IllegalStateException("请先停止动画")
        }
        view?.rotation = 0f
    }

    override fun setDuration(duration: Long): IAnimatorInterface {
        set.duration = duration
        return this
    }

    override fun setInterpolator(interpolator: Interpolator): IAnimatorInterface {
        set.interpolator = interpolator
        return this
    }

    override fun isRunning(): Boolean {
        return set.isRunning
    }

    override fun isFinish(): Boolean {
        return finished
    }

    override fun cancel() {
        set.cancel()
    }

    override fun addListener(listener: OnAnimationListener): IAnimatorInterface {
        this.listeners.add(listener)
        return this
    }

    override fun removeListener(listener: OnAnimationListener): IAnimatorInterface {
        this.listeners.remove(listener)
        return this
    }
}