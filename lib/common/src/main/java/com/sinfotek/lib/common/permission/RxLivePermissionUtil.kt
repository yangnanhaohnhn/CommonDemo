package com.sinfotek.lib.common.permission

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData

/**
 *
 * @author Y&N
 * date: 2021/10/27
 * desc:
 */
class RxLivePermissionUtil {
    companion object {
        const val TAG = "permission"
    }

    @Volatile
    private var liveFragment: LiveFragment? = null

    constructor(activity: AppCompatActivity) {
        liveFragment = getInstance(activity.supportFragmentManager)
    }

    constructor(fragment: Fragment) {
        liveFragment = getInstance(fragment.childFragmentManager)
    }

    private fun getInstance(fragmentManager: FragmentManager) = liveFragment ?: synchronized(this) {
        liveFragment ?: if (fragmentManager.findFragmentByTag(TAG) == null) LiveFragment().run {
            fragmentManager.beginTransaction().add(this, TAG).commitNow()
            this
        } else fragmentManager.findFragmentByTag(TAG) as LiveFragment
    }

    fun request(vararg permissions: String): MutableLiveData<PermissionResult> {
        return this.requestArray(permissions)
    }

    fun requestArray(permissions: Array<out String>): MutableLiveData<PermissionResult> {
        liveFragment!!.requestPermissions(permissions)
        return liveFragment!!.liveData
    }
}