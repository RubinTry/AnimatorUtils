package cn.rubintry.animate.core

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.View
import android.view.animation.Interpolator

import cn.rubintry.animate.core.interpolator.SpringScalingInterpolator
import cn.rubintry.animate.core.listener.OnAnimationListener


/**
 * 作   者:  卢孙仲
 * 创建日期:  2022年3月22日
 * 描   述: dialog抖动动画
 * 页面中文名:
 */
final class SpringScaleAnimator(view: View? , from: Float , to: Float , factor: Float) : BaseAnimator(view) ,
    IAnimatorInterface {
    private var animatorX: ObjectAnimator = ObjectAnimator.ofFloat(view, "scaleX", from, to)
    private var animatorY: ObjectAnimator = ObjectAnimator.ofFloat(view, "scaleY", from, to)
    
    private var isDestroy = false
    private val interpolator = SpringScalingInterpolator(factor)

    init {
        set.playTogether(animatorX , animatorY)
        set.interpolator = interpolator
    }

    companion object{

        /**
         * 获取一个带有弹性feel的缩放动画
         *
         * @param view 要操作的视图
         * @param from 原始大小倍率
         * @param to 最终大小倍率
         * @param factor 弹性系数：取值范围为 0..1，值越小效果越明显
         * @return
         */
        @JvmStatic
        fun obtain(view: View? , from: Float , to: Float , factor: Float) : SpringScaleAnimator {
            return SpringScaleAnimator(view, from , to , factor)
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


    /**
     * 无限循环
     *
     * @param loop
     * @return
     */
    override fun enableLoop(loop: Boolean): IAnimatorInterface {
        if(loop){
            allRepeatInfinity()
        }
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


    /**
     * 设置插值器（必要时）
     *
     * @param interpolator
     * @return
     */
    override fun setInterpolator(interpolator: Interpolator): IAnimatorInterface {
        set.interpolator = interpolator
        return this
    }


    /**
     * 开始播放
     *
     * @return
     */
    override fun start() : IAnimatorInterface {
        if(isDestroy){
            return this
        }
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
        return this
    }


    /**
     * 播放时长
     *
     * @param duration
     * @return
     */
    override fun setDuration(duration: Long): IAnimatorInterface {
        set.duration = duration
        return this
    }

    /**
     * 是否正在播放
     *
     * @return
     */
    override fun isRunning(): Boolean {
        return set.isRunning
    }


    /**
     * 是否播放结束
     *
     * @return
     */
    override fun isFinish(): Boolean {
        return finished
    }


    /**
     * 取消播放
     *
     */
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