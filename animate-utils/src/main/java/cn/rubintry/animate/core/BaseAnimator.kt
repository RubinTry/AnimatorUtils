package cn.rubintry.animate.core

import android.animation.AnimatorSet
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import cn.rubintry.animate.core.listener.OnAnimationListener


/**
 * 作   者:  卢孙仲
 * 创建日期:  2022年3月16日
 * 描   述: 动画基类，绑定了生命周期
 * 页面中文名:
 */
abstract class BaseAnimator(val view : View?) {
    protected var listeners: ArrayList<OnAnimationListener> = arrayListOf()
    protected var finished = false
    protected var set: AnimatorSet = AnimatorSet()
    protected var loop = false

    init {
        val context = view?.context
        //自动注册生命周期
        if(context is AppCompatActivity){
            context.lifecycle.addObserver(object : LifecycleEventObserver{
                override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                    when(event){
                        Lifecycle.Event.ON_PAUSE -> {
                            onActivityPause()
                        }

                        Lifecycle.Event.ON_DESTROY -> {
                            onActivityDestroyed()
                            AnimatorCache.getInstance().remove(view)
                        }

                        Lifecycle.Event.ON_RESUME -> {
                            onActivityResume()
                        }


                        else -> {

                        }
                    }
                }
            })
        }
    }


    /**
     * 生命周期方法，Activity在onDestroy的时候自动调用
     *
     */
    abstract fun onActivityDestroyed()




    /**
     * 生命周期方法，Activity在onResume的时候自动调用
     *
     */
    abstract fun onActivityResume()


    /**
     * 生命周期方法，Activity在onPause的时候自动调用
     *
     */
    abstract fun onActivityPause()


}