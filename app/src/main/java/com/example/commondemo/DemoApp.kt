package com.example.commondemo

import com.sinfotek.component.bus.InitBus
import com.sinfotek.component.net.InitNet
import com.sinfotek.lib.base.BaseApplication

/**
 *
 * @author Y&N
 * date: 2021/10/25
 * desc:
 */
open class DemoApp : BaseApplication() {

    override fun onCreate() {
        super.onCreate()

        init()
    }

    private fun init() {
        //初始化Bus
        InitBus.init()
        //开启网络
        InitNet.init(this)
    }
}