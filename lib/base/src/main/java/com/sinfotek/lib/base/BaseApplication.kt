package com.sinfotek.lib.base

import android.os.Build
import android.os.StrictMode
import androidx.multidex.MultiDexApplication
import com.alibaba.android.arouter.launcher.ARouter
import com.sinfotek.lib.common.const.BaseConst
import com.sinfotek.libbase.widget.ActivityManage
import com.sinfotek.lib.base.widget.exception.ExceptionHandler

/**
 *
 * @author Y&N
 * date: 2021/10/25
 * desc:
 */
open class BaseApplication : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        if (BaseConst.isOpenDebug) {
            //初始化 ARouter
            ARouter.openLog()
            //debug版本开启调试(线上版本需要关闭，否则有安全风险)
            ARouter.openDebug()
        }
        ARouter.init(this)
        //初始化日志
        val handler = ExceptionHandler.instance()
        handler.init(this)

        //解决7.0版本后调用相机报错的问题
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val builder = StrictMode.VmPolicy.Builder()
            StrictMode.setVmPolicy(builder.build())
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        //注销路由
        ARouter.getInstance().destroy()
        appExit()
    }

    /**
     * 退出应用程序
     */
    private fun appExit() {
        try {
            ActivityManage.instance.finishAllActivity()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}