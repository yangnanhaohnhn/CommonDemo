package com.sinfotek.lib.common

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import java.lang.Exception
import java.lang.ref.WeakReference
import java.util.*
import kotlin.system.exitProcess

/**
 *
 * @author Y&N
 * date: 2021/10/9
 * desc: Activity 管理
 */
class ActivityManage private constructor() {
    private var activityStack = Stack<WeakReference<Activity>>()

    companion object {
        //通过@JvmStatic注解，使得在Java中调用instance直接是像调用静态函数一样
        //类似KLazilyDCLSingleton.getInstance(),如果不加注解，在Java中必须这样调用: KLazilyDCLSingleton.Companion.getInstance().
        @JvmStatic
        //使用lazy属性代理，并指定LazyThreadSafetyMode为SYNCHRONIZED模式保证线程安全
        val instance: ActivityManage by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            ActivityManage()
        }
    }

    /**
     * 添加Activity到堆栈
     */
    fun addActivity(activity: WeakReference<Activity>?) {
        activityStack.push(activity)
    }

//    /**
//     * 获取当前Activity（堆栈中最后一个压入的）
//     */
//    fun currentActivity(): Activity? {
//        return try {
//            activityStack.lastElement().get()
//        } catch (e: Exception) {
//            null
//        }
//    }
//
//    /**
//     * 获取当前Activity的前一个Activity
//     */
//    fun preActivity(): Activity? {
//        val index: Int = activityStack.size - 2
//        return if (index < 0) {
//            null
//        } else activityStack[index].get()
//    }
//
//    /**
//     * 结束当前Activity（堆栈中最后一个压入的）
//     */
//    fun finishActivity() {
//        val weakReference = activityStack.lastElement()
//        finishActivity(weakReference.get())
//    }

//    /**
//     * 结束指定的Activity
//     */
//    fun finishActivity(activity: Activity?) {
//        if (activity != null) {
//            removeActivity(activity)
//            activity.finish()
//        }
//    }

//    /**
//     * 移除指定的Activity
//     */
//    fun removeActivity(activity: Activity?) {
//        if (activity != null) {
//            if (!isActivityStackEmpty()){
//                for (stack in activityStack){
//                    val activity = stack.get()
//                    if (activity)
//                }
//            }
//            activityStack.remove(activity)
//        }
//    }

//    /**
//     * 结束指定类名的Activity
//     */
//    fun finishActivity(cls: Class<*>) {
//        try {
//            for (activity in activityStack) {
//                if (activity.javaClass.name == cls.name) {
//                    finishActivity(activity)
//                }
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }

    /**
     * 结束所有Activity
     */
    fun finishAllActivity() {
       if (!isActivityStackEmpty()){
           for (stack in activityStack){
               val mActivity = stack.get()
               if (null != mActivity && !mActivity.isFinishing) {
                   mActivity.finish()
               }
           }
           activityStack.clear()
       }
    }

//    /**
//     * 返回到指定的activity
//     *
//     * @param cls
//     */
//    fun returnToActivity(cls: Class<*>) {
//        while (activityStack!!.size != 0) {
//            if (activityStack!!.peek().javaClass == cls) {
//                break
//            } else {
//                finishActivity(activityStack!!.peek())
//            }
//        }
//    }

//    /**
//     * 是否已经打开指定的activity
//     * @param cls
//     * @return
//     */
//    fun isOpenActivity(cls: Class<*>): Boolean {
//        for (stack in activityStack) {
//            val activity: Activity? = stack.get()
//            if (activity != null && cls == activity.javaClass) {
//                return true
//            }
//        }
//        return false
//    }

    /**
     * 退出应用程序
     *
     * @param context      上下文
     * @param isBackground 是否开开启后台运行
     */
    fun appExit(context: Context, isBackground: Boolean?) {
        try {
            finishAllActivity()
            val activityMgr = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            activityMgr.killBackgroundProcesses(context.packageName)
        } catch (e: Exception) {
        } finally {
            // 注意，如果您有后台程序运行，请不要支持此句子
            if (!isBackground!!) {
                exitProcess(0)
            }
        }
    }

    /**
     * 是否为空
     */
    fun isActivityStackEmpty(): Boolean {
        return activityStack.empty()
    }
}