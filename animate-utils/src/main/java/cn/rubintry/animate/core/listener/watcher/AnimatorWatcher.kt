package cn.rubintry.animate.core.listener.watcher

import android.app.Activity
import android.content.Context
import android.view.View
import cn.rubintry.animate.AnimateUtils
import cn.rubintry.animate.core.IAnimatorInterface
import cn.rubintry.animate.core.ext.doOnEnd
import cn.rubintry.animate.core.listener.OnAllCompleteListener
import java.lang.ref.WeakReference
import java.util.concurrent.*




/**
 * 作   者:  卢孙仲
 * 创建日期:  2022年3月23日
 * 描   述:  动画完成度批量监听
 * 页面中文名:
 */
class AnimatorWatcher(context: Context) {

    private var weakContext: WeakReference<Context> ?= null
    private var animatorSize: Int = 0
    private var endCount = 0
    private var listener: OnAllCompleteListener?= null
    //动画队列
    private val animatorQueue: LinkedBlockingDeque<Runnable> = LinkedBlockingDeque()
    private val coreRunnable = Runnable {
        while (true){
            try {
                val runnable: Runnable = animatorQueue.take()
                threadPoolExecutor?.execute(runnable)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }
    private var threadPoolExecutor : ThreadPoolExecutor ?= null

    init {
        this.weakContext = WeakReference(context)
        threadPoolExecutor = ThreadPoolExecutor(5, 10, 15, TimeUnit.SECONDS, ArrayBlockingQueue<Runnable>(5))
        threadPoolExecutor?.execute(FinderRunnable(weakContext?.get()))
        threadPoolExecutor?.execute(coreRunnable)
    }


    /**
     * 筛选出实现了[IAnimatorInterface]接口的View，开启循环的除外
     *
     * @param context 当前activity或activity对应的上下文
     * @return
     */
    private fun findTargetFromContext(context : Context?) : List<IAnimatorInterface> {
        val targetList = mutableListOf<IAnimatorInterface>()
        try {
            if(context is Activity){
                val memberIterator = context.javaClass.declaredFields.iterator()
                //遍历成员变量
                while (memberIterator.hasNext()){
                    val target = memberIterator.next()
                    target?.isAccessible =true
                    val targetAnimator = AnimateUtils.with(target.get(context) as? View).getAnimator()
                    if(!View::class.java.isAssignableFrom(target.type) || targetAnimator?.isLoop() == true){
                        continue
                    }

                    if(targetAnimator != null){
                        targetList.add(targetAnimator)
                    }
                }
            }
        }catch (ignore : Exception){
        }
        return targetList
    }


    /**
     * 添加动画完成回调
     *
     * @param listener
     */
    fun waitAllComplete(listener : OnAllCompleteListener){
        this.listener = listener
    }

    /**
     * 批量添加watcher
     *
     * @param animators
     */
    private fun put(vararg animators: IAnimatorInterface) {
        this.animatorSize = animators.size
        for (animator in animators) {
            try {
                animatorQueue.put(AnimatorWatcherImpl(animator))
            }catch (ex: Exception){
                ex.printStackTrace()
            }
        }
    }

    /**
     * 监听器线程
     *
     * @property animator
     */
    inner class AnimatorWatcherImpl(private val animator: IAnimatorInterface) : Runnable{
        override fun run() {
            animator.doOnEnd {
                endCount ++
                //队列被清空并且已完成数量等于动画总数，则认定为全部执行完毕
                if(animatorQueue.isEmpty() && endCount == animatorSize){
                    //所有动画都执行完了
                    println("All animation play complete!")
                    listener?.onAllComplete()
                }
            }
        }
    }


    /**
     * 视图查找线程
     *
     * @property context activity所对应的context
     */
    inner class FinderRunnable(private val context: Context?) : Runnable{
        override fun run() {
            val targetList = findTargetFromContext(context)
            put(*targetList.toTypedArray())
        }
    }
}