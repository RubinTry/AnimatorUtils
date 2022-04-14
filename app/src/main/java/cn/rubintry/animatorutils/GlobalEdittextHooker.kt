package cn.rubintry.animatorutils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.view.allViews
import cn.rubintry.animatorutils.ext.getField


/**
 * 针对点击Edittext外部时要收起软键盘而设计的Hooker
 *
 */
class GlobalEdittextHooker {

    companion object{

        @SuppressLint("ClickableViewAccessibility")
        @JvmStatic
        fun registerHooker(app: Application){
            app.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks{
                override fun onActivityCreated(p0: Activity, p1: Bundle?) {

                }

                override fun onActivityStarted(activity: Activity) {
                    hookViews(activity)
                }

                override fun onActivityResumed(p0: Activity) {

                }

                override fun onActivityPaused(p0: Activity) {

                }

                override fun onActivityStopped(p0: Activity) {

                }

                override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {

                }

                override fun onActivityDestroyed(p0: Activity) {

                }

            })
        }


        /**
         * hook一下activity里view的点击事件
         *
         * @param activity
         */
        private fun hookViews(activity: Activity) {
            val decorView = (activity.window.decorView as ViewGroup)
            val viewIterator = decorView.allViews.iterator()
            while (viewIterator.hasNext()){
                val targetNode = viewIterator.next()
                if(targetNode !is EditText){
                    //已有OnClickListener???不慌，咱把它的mOnClickListener给hook出来，执行完它的代码再执行咱的
                    if(targetNode.hasOnClickListeners()){
                        val listener = hookOnClickListeners(targetNode)
                        targetNode.setOnClickListener {
                            listener?.onClick(it)
                            hideKeyboard(activity)
                        }
                    }else{
                        targetNode.setOnClickListener {
                            hideKeyboard(activity)
                        }
                    }
                }
            }
        }


        /**
         * 获取view中已有的onClickListener
         *
         * @param targetNode
         * @return
         */
        @SuppressLint("PrivateApi", "DiscouragedPrivateApi")
        private fun hookOnClickListeners(targetNode: View): View.OnClickListener? {
            return try {
                val mListenerInfo = targetNode.getField<Any>("mListenerInfo")
                return mListenerInfo?.getField<View.OnClickListener>("mOnClickListener")
            }catch (e: Exception){
                null
            }
        }


        /**
         * 隐藏键盘
         *
         * @param activity
         */
        private fun hideKeyboard(activity: Activity) {
            val window = activity.window
            var view: View? = window?.currentFocus
            if (view == null) {
                val decorView: View? = window?.decorView
                val focusView = decorView?.findViewWithTag<View>("keyboardTagView")
                if (focusView == null) {
                    view = EditText(window?.context)
                    view.setTag("keyboardTagView")
                    (decorView as ViewGroup).addView(view, 0, 0)
                } else {
                    view = focusView
                }
                view.requestFocus()
            }

            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                ?: return
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }



}