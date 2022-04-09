package cn.rubintry.animate.core

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle



/**
 * 作   者:  卢孙仲
 * 创建日期:  2022年3月16日
 * 描   述: 动画生命周期监听器
 * 页面中文名:
 */
open class AnimationLifecycleListener(context: Context?) : Application.ActivityLifecycleCallbacks{
    private var activityName = ""

    init {
        if(context is Activity){
            activityName = context.javaClass.canonicalName ?: ""
        }
    }

    override fun onActivityDestroyed(activity: Activity) {
        if(activityName == activity.javaClass.canonicalName){
            onActivityDestroy()
        }
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {

    }

    override fun onActivityStarted(activity: Activity) {

    }

    override fun onActivityResumed(activity: Activity) {
        if(activityName == activity.javaClass.canonicalName){
            onActivityResume()
        }
    }

    override fun onActivityPaused(activity: Activity) {
        if(activityName == activity.javaClass.canonicalName){
            onActivityPause()
        }
    }

    override fun onActivityStopped(activity: Activity) {

    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

    }

    open fun onActivityDestroy(){

    }

    open fun onActivityPause(){

    }

    open fun onActivityResume(){

    }
}