package com.sinfotek.component.choose

import android.app.Application
import com.sinfotek.lib.common.log.RxLogUtil
import com.tencent.smtt.sdk.QbSdk

/**
 *
 * @author Y&N
 * date: 2021/11/15
 * desc:
 */
object InitChoose {
    fun initTbs(application: Application) {
        QbSdk.initX5Environment(application, object : QbSdk.PreInitCallback {
            override fun onCoreInitFinished() {
                RxLogUtil.i("onCoreInitFinished")
            }

            override fun onViewInitFinished(b: Boolean) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。

                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                RxLogUtil.d("QB", " onViewInitFinished is $b")
            }
        })
    }

}