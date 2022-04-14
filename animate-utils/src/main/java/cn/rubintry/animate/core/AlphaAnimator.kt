package cn.rubintry.animate.core

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Build
import android.view.View
import android.view.animation.Interpolator
import androidx.annotation.RequiresApi
import cn.rubintry.animate.core.ext.doOnEnd

import cn.rubintry.animate.core.listener.OnAnimationListener


/**
 * 作   者:  卢孙仲
 * 创建日期:  2022年3月23日
 * 描   述: 不透明度动画
 * 页面中文名:
 */

class AlphaAnimator(view: View? , from: Float , to: Float , reverse: Boolean) : BaseAnimator(view) ,
    IAnimatorInterface {

    private var animatorX: ObjectAnimator = ObjectAnimator.ofFloat(view, "alpha", from, to)
    private var animatorY: ObjectAnimator = ObjectAnimator.ofFloat(view, "alpha", from, to)

    private var isDestroy = false


    companion object{

        /**
         * 获得一个不透明度动画
         *
         * @param view 要操作的视图
         * @param from 初始不透明度
         * @param to 最终不透明度
         * @param reverse 播放完成后是否需要恢复
         * @return
         */
        @JvmStatic
        fun obtain(view: View?, from: Float, to: Float, reverse: Boolean): AlphaAnimator {
            return AlphaAnimator(view , from , to , reverse)
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

    override fun reset() {
        if(isRunning()){
            throw IllegalStateException("请先停止动画")
        }
        view?.alpha = 1.0f
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