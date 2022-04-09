package cn.rubintry.animate.core

import android.view.View

/**
 * 作   者:  卢孙仲
 * 创建日期:  2022年4月9日
 * 描   述: [IAnimatorInterface]的缓存类，与[IAnimatorInterface]的[生命周期]相呼应，也可以用来[手动取消]某个view的动画
 * 页面中文名:
 */
internal class AnimatorCache {


    private val animateMap = linkedMapOf<View, IAnimatorInterface>()

    companion object{
        @Volatile
        private var instance : AnimatorCache?= null

        /**
         * 获取单例，有且只有一个
         *
         * @return
         */
        @JvmStatic
        fun getInstance() : AnimatorCache {
            if(null == instance){
                synchronized(AnimatorCache::class.java){
                    if(null == instance){
                        instance = AnimatorCache()
                    }
                }
            }
            return instance!!
        }
    }


    /**
     * 存入动画
     *
     * @param view
     * @param iAnimatorInterface
     */
    fun put(view: View, iAnimatorInterface: IAnimatorInterface){
        animateMap[view] = iAnimatorInterface
    }


    /**
     * 获取动画
     *
     * @param view
     * @return
     */
    fun get(view: View?) : IAnimatorInterface?{
        if(null == view){
            return null
        }
        return animateMap[view]
    }


    /**
     * 移除动画
     *
     * @param view
     */
    fun remove(view: View?){
        if(null == view){
            return
        }
        animateMap.remove(view)
    }
}