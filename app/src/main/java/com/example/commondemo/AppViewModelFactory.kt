package com.example.commondemo

import android.app.Application
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModel
import com.sinfotek.lib.base.mvvm.BaseAppVmFactory

/**
 *
 * @author Y&N
 * date: 2021/10/9
 * desc:
 */
class AppViewModelFactory(application: Application, private val activity: ComponentActivity) :
    BaseAppVmFactory(application) {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainVm::class.java)) {
            return MainVm(activity, application, MainModel()) as T
        } else if (modelClass.isAssignableFrom(Paging3ViewModel::class.java)) {
            return Paging3ViewModel(activity, application) as T
        }
        return super.create(modelClass)
    }

}