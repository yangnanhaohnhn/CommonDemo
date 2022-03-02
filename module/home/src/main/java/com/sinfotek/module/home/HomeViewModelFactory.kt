package com.sinfotek.module.home

import android.app.Application
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModel
import com.sinfotek.lib.base.mvvm.BaseAppVmFactory
import com.sinfotek.lib.base.mvvm.model.BaseModel

/**
 *
 * @author Y&N
 * date: 2021/10/9
 * desc:
 */
class HomeViewModelFactory(application: Application, private val activity: ComponentActivity) : BaseAppVmFactory(application) {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeVm::class.java)) {
            return HomeVm(activity, application, BaseModel()) as T
        }
        return super.create(modelClass)
    }

}