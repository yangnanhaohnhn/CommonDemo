package com.sinfotek.component.net

import android.content.Context
import com.facebook.stetho.Stetho
import com.sinfotek.component.net.config.NetConfig
import com.sinfotek.component.net.const.BaseUrlConst
import com.sinfotek.lib.common.const.BaseConst
import com.sinfotek.lib.common.log.RxLogUtil

/**
 *
 * @author Y&N
 * date: 2021/10/25
 * desc:
 */
object InitNet {
    fun init(context: Context) {
        RxLogUtil.init(BaseConst.isOpenDebug)
        if (BaseConst.isOpenDebug) {
            //DeviceTool chrome://inspect
            Stetho.initializeWithDefaults(context)
        }

        val config = NetConfig.Builder()
            .setBaseUrl(BaseUrlConst.BASE_URL)
            .build()
        config.initContext(context)
    }
}