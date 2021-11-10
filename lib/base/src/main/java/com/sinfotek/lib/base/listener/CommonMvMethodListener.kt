package com.sinfotek.lib.base.listener

import androidx.lifecycle.ViewModelProvider

/**
 *
 * @author Y&N
 * date: 2021/10/9
 * desc:
 */
interface CommonMvMethodListener {

    /**
     * BR.viewmodel或者0
     * @return
     */
    fun onBindVariableId(): Int

    /**
     * 绑定工厂
     * @return
     */
    fun onBindViewModelFactory(): ViewModelProvider.AndroidViewModelFactory?

    /**
     * //页面事件监听的方法，一般用于ViewModel层转到View层的事件注册
     */
    fun initViewObservable()
}