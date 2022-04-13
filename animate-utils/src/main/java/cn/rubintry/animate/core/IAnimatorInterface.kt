package cn.rubintry.animate.core

import android.view.animation.Interpolator
import cn.rubintry.animate.core.listener.OnAnimationListener


/**
 * 作   者:  卢孙仲
 * 创建日期:  2022年3月16日
 * 描   述: 动画封装接口
 * 页面中文名:
*/
interface IAnimatorInterface {


    /**
     * 允许无限循环
     *
     * @param loop
     * @return
     */
    fun enableLoop(loop: Boolean) : IAnimatorInterface

    /**
     * 是否开启无限循环
     *
     * @return
     */
    fun isLoop() : Boolean


    /**
     * 开始播放
     *
     */
    fun start()



    /**
     * 单次播放时长
     *
     * @param duration
     * @return
     */
    fun setDuration(duration: Long) : IAnimatorInterface


    /**
     * 添加插值器
     *
     * @param interpolator
     * @return
     */
    fun setInterpolator(interpolator: Interpolator) : IAnimatorInterface


    /**
     * 是否正在执行动画
     *
     * @return
     */
    fun isRunning() : Boolean


    /**
     * 是否已完成动画
     *
     * @return
     */
    fun isFinish() : Boolean


    /**
     * 取消动画
     *
     */
    fun cancel()


    /**
     * 添加动画执行回调
     *
     * @param listener
     */
    fun addListener(listener: OnAnimationListener) : IAnimatorInterface

    /**
     * 移除监听
     *
     * @param listener
     * @return
     */
    fun removeListener(listener: OnAnimationListener) : IAnimatorInterface

}