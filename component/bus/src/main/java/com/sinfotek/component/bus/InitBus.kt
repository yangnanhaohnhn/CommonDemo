package com.sinfotek.component.bus

import com.jeremyliao.liveeventbus.LiveEventBus
import com.sinfotek.lib.common.const.BaseConst

/**
 *
 * @author Y&N
 * date: 2021/10/28
 * desc:
 */
object InitBus {
    /**
     * 在子线程中初始化
     */
    fun init() {
        Thread {
            //lifecycleObserverAlwaysActive
//        //配置LifecycleObserver（如Activity）接收消息的模式（默认值true）：
//        //true：整个生命周期（从onCreate到onDestroy）都可以实时收到消息
//        //false：激活状态（Started）可以实时收到消息，非激活状态（Stoped）无法实时收到消息，需等到Activity重新变成激活状态，方可收到消息
//        //setContext
//        //如果广播模式有问题，请手动传入Context，需要在application onCreate中配置
            LiveEventBus.config()
                //配置在没有Observer关联的时候是否自动清除LiveEvent以释放内存（默认值false）
                .autoClear(true)
                .lifecycleObserverAlwaysActive(true)
                .enableLogger(BaseConst.isOpenDebug)
        }.start()

    }
}