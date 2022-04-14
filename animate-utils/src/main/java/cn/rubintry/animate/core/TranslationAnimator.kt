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
 * 描   述: 平移动画
 * 页面中文名:
 */
final class TranslationAnimator(view : View?,  x: Float,  y: Float, val reverse: Boolean) : BaseAnimator(view) ,
    IAnimatorInterface {

    private var animatorX : ObjectAnimator = ObjectAnimator.ofFloat(view , "translationX" , 0f , x)
    private var animatorY : ObjectAnimator = ObjectAnimator.ofFloat(view , "translationY" , 0f , y)
    private var isDestroy = false
    

    companion object{
        /**
         * 获取移动动画
         *
         * @param view 要操作的视图
         * @param x x轴上的偏移量
         * @param y y轴上的偏移量
         * @param reverse 播放完一次后是否需要反向播放一次
         * @return
         */
        @JvmStatic
        fun obtain(view: View? , x: Float , y: Float , reverse: Boolean): TranslationAnimator {
            return TranslationAnimator(view , x , y ,  reverse)
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
     * 动画是否正在播放
     *
     * @return
     */
    override fun isRunning(): Boolean {
        return set.isRunning
    }

    /**
     * 是否执行完毕
     *
     * @return
     */
    override fun isFinish(): Boolean {
        return finished
    }


    /**
     * 无限循环
     *
     * @param loop
     * @return
     */
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
        view?.translationX = 0.0f
        view?.translationY = 0.0f
    }


    /**
     * 取消播放
     *
     */
    override fun cancel() {
        set.cancel()
    }


    /**
     * 添加动画播放监听
     *
     * @param listener
     * @return
     */
    override fun addListener(listener: OnAnimationListener): IAnimatorInterface {
        this.listeners.add(listener)
        return this
    }


    /**
     * 移除动画播放监听
     *
     * @param listener
     * @return
     */
    override fun removeListener(listener: OnAnimationListener): IAnimatorInterface {
        this.listeners.remove(listener)
        return this
    }


}