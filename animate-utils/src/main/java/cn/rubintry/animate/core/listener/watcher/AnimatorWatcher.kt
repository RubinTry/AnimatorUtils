package cn.rubintry.animate.core.listener.watcher

import cn.rubintry.animate.core.IAnimatorInterface
import cn.rubintry.animate.core.doOnEnd
import cn.rubintry.animate.core.listener.OnAllCompleteListener
import java.util.concurrent.*




/**
 * 作   者:  卢孙仲
 * 创建日期:  2022年3月23日
 * 描   述:  动画完成度批量监听
 * 页面中文名:
 */
class AnimatorWatcher {

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
        threadPoolExecutor = ThreadPoolExecutor(5, 10, 15, TimeUnit.SECONDS, ArrayBlockingQueue<Runnable>(5)
        )
        threadPoolExecutor?.execute(coreRunnable)
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
    fun put(vararg animators: IAnimatorInterface) {
        this.animatorSize = animators.size
        for (animator in animators) {
            try {
                animatorQueue.put(AnimatorWatcherImpl(animator))
            }catch (ex: Exception){
                ex.printStackTrace()
            }
        }
    }



    inner class AnimatorWatcherImpl(private val animator: IAnimatorInterface) : Runnable{
        override fun run() {
            animator.doOnEnd {
                endCount ++
                //队列被清空并且已完成数量等于动画总数，则认定为全部执行完毕
                if(animatorQueue.isEmpty() && endCount == animatorSize){
                    //所有动画都执行完了
                    listener?.onAllComplete()
                }
            }
        }
    }
}