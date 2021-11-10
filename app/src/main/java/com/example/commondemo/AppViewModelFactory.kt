package com.example.commondemo

import android.app.Application
import androidx.activity.ComponentActivity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import com.sinfotek.lib.base.mvvm.BaseAppVmFactory

/**
 *
 * @author Y&N
 * date: 2021/10/9
 * desc:
 */
class AppViewModelFactory(application: Application) : BaseAppVmFactory(application) {

    fun activity(activity: FragmentActivity): AppViewModelFactory {
        super.setActivity(activity)
        return this
    }

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainVm::class.java)) {
            return MainVm(activity, application, MainModel()) as T
        }
        return super.create(modelClass)
    }

}