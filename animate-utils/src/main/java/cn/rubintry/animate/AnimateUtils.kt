package cn.rubintry.animate

import android.content.Context
import android.view.View
import androidx.annotation.FloatRange
import cn.rubintry.animate.core.*
import cn.rubintry.animate.core.AnimatorCache
import cn.rubintry.animate.core.listener.watcher.AnimatorWatcher
import java.lang.ref.WeakReference



/**
 * 作   者:  卢孙仲
 * 创建日期:  2022年3月16日
 * 描   述: view动画工具类，内部实现了生命周期绑定  ， 可自行使用[IAnimatorInterface]接口与[BaseAnimator]基类进行自定义
 * 页面中文名:
*/
class AnimateUtils private constructor(view: View?) {
    /**
     * 进行视图弱引用，防止内存泄漏
     */
    private val weakView = WeakReference(view)


    companion object{

        /**
         * 绑定到视图
         *
         * @param view
         * @return
         */
        @JvmStatic
        fun with(view: View?): AnimateUtils {
            return AnimateUtils(view)
        }


        /**
         * 监听当前activity所有view的动画是否播放完成
         * @param animators
         * @return
         */
        @JvmStatic
        fun watch(context: Context): AnimatorWatcher {
            return AnimatorWatcher(context)
        }

    }


    /**
     * 移除动画
     *
     */
    fun cancel(){
        weakView.get()?.let {
            AnimatorCache.getInstance().get(it)?.cancel()
            it.clearAnimation()
        }
    }


    /**
     * 重置，将当前视图恢复到初始位置、大小、角度、透明度
     *
     */
    fun reset(){
        weakView.get()?.let {
            AnimatorCache.getInstance().get(it)?.reset()
            AnimatorCache.getInstance().remove(it)
        }
    }


    /**
     * 判断当前view是否有动画正在播放
     *
     * @return
     */
    fun isAnimateRunning() : Boolean{
        return getAnimator()?.isRunning() ?: false
    }


    /**
     * 当前视图是否含有[IAnimatorInterface]动画
     *
     * @return
     */
    fun hasAnimator() : Boolean{
        return null != getAnimator()
    }

    /**
     * 获取当前view的动画
     *
     * @return
     */
    internal fun getAnimator() : IAnimatorInterface? {
        return AnimatorCache.getInstance().get(weakView.get())
    }


    /**
     * 自定义动画接口
     *
     * @param T 动画具体实现类
     * @param customAnimator 实现类的class
     * 案例：用[AnimateUtils.with]获取到view所绑定的AnimateUtils，再用[AnimateUtils.asCustom]方法进行自定义动画
     * @return
     */
    fun <T : IAnimatorInterface> asCustom(customAnimator: Class<T>) : T{
        if(customAnimator.isInterface){
            throw IllegalArgumentException("请传入IAnimatorInterface的实现类以保证动画能正常执行")
        }
        val animate = customAnimator.newInstance()
        weakView.get()?.let {
            AnimatorCache.getInstance().put(it , animate)
        }
        return animate
    }




    /**
     * 创建移动动画
     *
     * @param x x偏移值
     * @param y y偏移值
     * @param reverse 是否需要原路返回 ，如果为false，则较为生硬，直接将view闪现到初始位置
     */
    fun translation(x: Float, y: Float, reverse: Boolean): IAnimatorInterface {
        val animate = TranslationAnimator.obtain(weakView.get(), x, y, reverse)
        weakView.get()?.let {
            AnimatorCache.getInstance().put(it , animate)
        }
        return animate
    }


    /**
     * 创建一个旋转动画
     *
     * @param angle 旋转角度
     * @param reverse 是否需要原路返回
     * @return
     */
    fun rotate(angle: Float, reverse: Boolean): IAnimatorInterface {
        val animate = RotateAnimator.obtain(weakView.get(), angle, reverse)
        weakView.get()?.let {
            AnimatorCache.getInstance().put(it , animate)
        }
        return animate
    }


    /**
     * 创建一个缩放动画
     *
     * @param from 初始大小
     * @param to 最终大小
     * @param reverse 是否需要原路返回
     * @return
     */
    fun scale( from: Float,  to: Float, reverse: Boolean): IAnimatorInterface {
        val animate = ScaleAnimator.obtain(weakView.get(), from, to, reverse)
        weakView.get()?.let {
            AnimatorCache.getInstance().put(it , animate)
        }
        return animate
    }


    /**
     * 创建一个回弹动画
     *
     * @param from 初始大小
     * @param to  最终大小
     * @param factor  弹性因子，取值范围为 0..1，值越小效果越明显
     * @return
     */
    fun sprintScale(
        from: Float,
        to: Float,
        @FloatRange(from = 0.0, to = 1.0) factor: Float
    ): SpringScaleAnimator {
        val animate = SpringScaleAnimator.obtain(weakView.get(), from, to, factor)
        weakView.get()?.let {
            AnimatorCache.getInstance().put(it , animate)
        }
        return animate
    }


    /**
     * 创建一个不透明度动画
     *
     * @param from 初始不透明度
     * @param to 最终不透明度
     * @param reverse  是否需要原路返回
     * @return
     */
    fun alpha(from : Float , to : Float , reverse: Boolean): AlphaAnimator {
        val animate = AlphaAnimator.obtain(weakView.get() , from , to , reverse)
        weakView.get()?.let {
            AnimatorCache.getInstance().put(it , animate)
        }
        return animate
    }





}