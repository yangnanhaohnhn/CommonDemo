package com.sinfotek.lib.base.mvvm.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.sinfotek.lib.base.mvvm.bus.SingleLiveEvent
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

/**
 *
 * @author Y&N
 * date: 2021/10/8
 * desc:
 */
open class BaseViewModel(application: Application) : AndroidViewModel(application), IBaseViewModel {

    val uc: UiChangeLiveData<Any> by lazy {
        UiChangeLiveData()
    }

    inner class UiChangeLiveData<T> : SingleLiveEvent<T>() {
        val showLoadingEvent: SingleLiveEvent<String> by lazy {
            SingleLiveEvent()
        }
        val hideLoadingEvent: SingleLiveEvent<Void> by lazy {
            SingleLiveEvent()
        }
    }

    protected fun <T> createLiveData(liveData: SingleLiveEvent<T>?): SingleLiveEvent<T> {
        if (liveData == null) {
            return SingleLiveEvent()
        }
        return liveData
    }

    /**
     * 显示loading
     * setValue()只能在主线程中调用，postValue()可以在任何线程中调用。
     * @param msg
     */
    protected open fun showLoading(msg: String) {
        uc.showLoadingEvent.postValue(msg)
    }

    /**
     * 隐藏loading
     */
    open fun hideLoading() {
        uc.hideLoadingEvent.call()
    }

    /**
     * 在主线程中执行一个协程
     */
    protected fun launch(block: suspend CoroutineScope.() -> Unit): Job {
        return viewModelScope.launch {
            block()
        }
    }

    /**
     * 在IO线程中执行一个协程：其实并不太需要，VM大部分时间是与UI的操作绑定，不太需要新起线程
     */
    protected fun launchOnIO(block: suspend CoroutineScope.() -> Unit): Job {
        return viewModelScope.launch(Dispatchers.IO) { block() }
    }

    /**
     *
     */
    protected inline fun <reified T> dealDataFlow(crossinline block: suspend () -> T): Flow<T> {
        return flow {
            emit(block.invoke())
        }.flowOn(Dispatchers.IO)
    }


    override fun onCleared() {
        super.onCleared()
    }
}