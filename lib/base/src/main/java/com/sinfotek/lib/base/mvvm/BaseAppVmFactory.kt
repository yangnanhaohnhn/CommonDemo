package com.sinfotek.lib.base.mvvm

import android.app.Application
import androidx.activity.ComponentActivity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 *
 * @author Y&N
 * date: 2021/10/9
 * desc:
 */
open class BaseAppVmFactory(val application: Application) :
    ViewModelProvider.AndroidViewModelFactory(application) {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

}