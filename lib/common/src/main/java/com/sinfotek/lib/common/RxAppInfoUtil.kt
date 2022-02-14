package com.sinfotek.lib.common

import android.app.ActivityManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Environment
import android.os.Process
import android.os.StatFs
import java.text.DecimalFormat

/**
 *
 * @author Y&N
 * date: 2021/10/8
 * desc:  手机信息
 */
object RxAppInfoUtil {
    /**
     * 获取手机ram内存，即应用运行所用内存
     *
     * @param context
     * @return [0] 手机ram总内存， [1] 当前ram可用内存 , [2] 系统是否处于低内存运行模式
     */
    fun getRamMemory(context: Context): Array<Any> {
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val mi = ActivityManager.MemoryInfo()
        am.getMemoryInfo(mi)
        return arrayOf(reviseFileSize(mi.totalMem), reviseFileSize(mi.availMem), mi.lowMemory)
    }

    private const val FILE_SIZE = 1024

    /**
     * 获取大小
     *
     * @param size
     * @return
     */
    private fun reviseFileSize(size: Long): String {
        var str = "KB"
        var reviseSize = 0f
        if (size > FILE_SIZE) {
            reviseSize = (size / FILE_SIZE).toFloat()
            if (reviseSize > FILE_SIZE) {
                str = "M"
                reviseSize /= FILE_SIZE
                if (reviseSize > FILE_SIZE) {
                    str = "G"
                    reviseSize /= FILE_SIZE
                }
            }
        }
        val formatter = DecimalFormat()
        formatter.groupingSize = 3
        return formatter.format(reviseSize.toDouble()) + str
    }


//    /**
//     * 获取软件版本信息
//     *
//     * @param context
//     * @return
//     */
//    fun getAppVersion(context: Context): MutableList<Any> {
//        val msg = arrayListOf<Any>(2)
//        val manager = context.packageManager
//        try {
//            val info = manager.getPackageInfo(context.packageName, PackageManager.GET_ACTIVITIES)
//            msg.add(if (info.versionName == null) "" else info.versionName)
//            msg.add(info.versionCode)
//        } catch (e: PackageManager.NameNotFoundException) {
//            e.printStackTrace()
//        }
//        return msg
//    }

    /**
     * 获取应用虚拟机能获取使用的最大内存
     *
     * @return
     */
    fun getAppMaxMemory(): String {
        return reviseFileSize(Runtime.getRuntime().maxMemory())
    }

    /**
     * 获取应用虚拟机已开辟内存
     *
     * @return
     */
    fun getAppAllocatedMemory(): String {
        return reviseFileSize(Runtime.getRuntime().totalMemory())
    }

    /**
     * 获取应用虚拟机已释放的内存
     * 调用gc()会使该值增大
     *
     * @return
     */
    fun getAppFreeMemory(): String {
        return reviseFileSize(Runtime.getRuntime().freeMemory())
    }

    /**
     * 获取SD总大小
     *
     * @return
     */
    fun getSdTotalSize(): String? {
        if (!RxSdUtil.isExistSD()) {
            return null
        }
        val file = Environment.getExternalStorageDirectory()
        val statFs = StatFs(file.path)
        val blockSize = statFs.blockSizeLong
        val totalBlocks = statFs.blockCountLong
        return reviseFileSize(totalBlocks * blockSize)
    }

    /**
     * 获取sd卡可用大小
     *
     * @return
     */
    fun getSdAvailableSize(): String? {
        if (!RxSdUtil.isExistSD()) {
            return null
        }
        val file = Environment.getExternalStorageDirectory()
        val statFs = StatFs(file.path)
        val blockSize = statFs.blockSizeLong
        val availableBlocks = statFs.freeBlocksLong
        return reviseFileSize(availableBlocks * blockSize)
    }

    /**
     * 获取当前的进程名
     *
     * @param cxt
     * @return
     */
    fun getCurProcessName(cxt: Context): String? {
        val pid = Process.myPid()
        val am = cxt.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningApps = am.runningAppProcesses ?: return null
        for (procInfo in runningApps) {
            if (procInfo.pid == pid) {
                return procInfo.processName
            }
        }
        return null
    }


}