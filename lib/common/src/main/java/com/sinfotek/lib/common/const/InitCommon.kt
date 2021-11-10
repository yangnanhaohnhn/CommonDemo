package com.sinfotek.lib.common.const

import android.content.Context
import com.sinfotek.lib.common.RxMMKVUtil

/**
 *
 * @author Y&N
 * date: 2021/11/4
 * desc:
 */
object InitCommon {
    fun init(context: Context) {
        //初始化
        RxMMKVUtil.init(context)
    }
}