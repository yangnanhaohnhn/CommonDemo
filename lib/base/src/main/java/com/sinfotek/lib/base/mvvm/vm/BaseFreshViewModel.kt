package com.sinfotek.lib.base.mvvm.vm

import android.app.Application
import com.sinfotek.lib.base.mvvm.bus.SingleLiveEvent

/**
 *
 * @author Y&N
 * date: 2021/11/26
 * desc:
 */
open class BaseFreshViewModel<M>(application: Application) : BaseViewModel<M>(application) {

    var mUiChangeFreshLiveData = UiChangeFreshLiveData()

    inner class UiChangeFreshLiveData : SingleLiveEvent<Any>() {

    }

}