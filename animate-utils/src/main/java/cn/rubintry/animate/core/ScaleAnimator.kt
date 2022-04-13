package cn.rubintry.animate.core

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Build
import android.view.View
import android.view.animation.Interpolator
import androidx.annotation.RequiresApi

import cn.rubintry.animate.core.listener.OnAnimationListener


/**
 * 作   者:  卢孙仲
 * 创建日期:  2022年3月22日
 * 描   述: 缩放动画
 * 页面中文名:
 */
final class ScaleAnimator(view: View? , from: Float , to: Float , reverse: Boolean) : BaseAnimator(view) ,
    IAnimatorInterface {


    private var animatorX: ObjectAnimator = ObjectAnimator.ofFloat(view, "scaleX", from, to)
    private var animatorY: ObjectAnimator = ObjectAnimator.ofFloat(view, "scaleY", from, to)
    private var isDestroy = false


    companion object{
        /**
         * 获得一个缩放动画
         *
         * @param view 要操作的视图
         * @param from 原始倍率（一般为1.0f）
         * @param to 缩放倍数
         * @param reverse 缩放完成后是否反向恢复到原始大小
         * @return
         */
        @JvmStatic
        fun obtain(view: View?, from: Float, to: Float, reverse: Boolean): ScaleAnimator {
            return ScaleAnimator(view , from , to , reverse)
        }
    }

    init {
        set.playTogether(animatorX , animatorY)
        if(reverse){
            animatorX.repeatMode = ValueAnimator.REVERSE
            animatorY.repeatMode = ValueAnimator.REVERSE
        }
    }

    override fun onActivityDestroyed() {
        isDestroy = true
        cancel()
    }

    override fun onActivityStop() {
        cancel()
    }

    override fun onActivityResume() {
        set.resume()
    }

    override fun onActivityPause() {
        set.pause()
    }

    override fun enableLoop(loop: Boolean): IAnimatorInterface {
        this.loop = loop
        if(loop){
            allRepeatInfinity()
        }
        return this
    }

    override fun isLoop(): Boolean {
        return loop
    }


    override fun setInterpolator(interpolator: Interpolator): IAnimatorInterface {
        set.interpolator = interpolator
        return this
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


    @RequiresApi(Build.VERSION_CODES.O)
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

    override fun setDuration(duration: Long): IAnimatorInterface {
        set.duration = duration
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

    override fun addListener(listener: OnAnimationListener) : IAnimatorInterface {
        this.listeners?.add(listener)
        return this
    }

    override fun removeListener(listener: OnAnimationListener): IAnimatorInterface {
        this.listeners.remove(listener)
        return this
    }


}