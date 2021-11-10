package com.sinfotek.lib.common

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

/**
 *
 * @author Y&N
 * date: 2021/10/22
 * desc:
 */
object RxNetUtil {

    /**
     * 判断是否有网
     * @return true:有网
     */
    fun isNetworkAvailable(context: Context): Boolean {
        val cm = getConnectivityManager(context) as ConnectivityManager?
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        if (cm == null) {
            return false
        }
        if (Build.VERSION.SDK_INT >= 23) {
            //获取网络属性
            val networkCapabilities = cm.getNetworkCapabilities(cm.activeNetwork);
            if (networkCapabilities != null) {
                return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
            }
        } else {
            // 获取NetworkInfo对象
            val networkInfo = cm.activeNetworkInfo
            if (networkInfo != null) {
                return networkInfo.isConnected
            }
        }
        return false
    }

    private fun getConnectivityManager(context: Context): ConnectivityManager {
        return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }
}