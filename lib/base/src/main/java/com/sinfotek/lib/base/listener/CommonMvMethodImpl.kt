package com.sinfotek.lib.base.listener

import androidx.lifecycle.ViewModelProvider

/**
 *
 * @author Y&N
 * date: 2021/10/9
 * desc:
 */
open class CommonMvMethodImpl: CommonMvMethodListener {
    /**
     * BR.viewmodel或者0
     * @return
     */
    override fun onBindVariableId(): Int {
        return 0
    }

    /**
     * 绑定工厂
     * @return
     */
    override fun onBindViewModelFactory(): ViewModelProvider.AndroidViewModelFactory? {
        return null
    }

    /**
     * //页面事件监听的方法，一般用于ViewModel层转到View层的事件注册
     */
    override fun initViewObservable() {
    }
}