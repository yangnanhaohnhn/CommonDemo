package com.sinfotek.lib.base.widget.exception

import android.app.Application
import android.os.Build
import android.os.Looper
import android.os.Process
import com.sinfotek.lib.common.RxAppInfoUtil
import java.io.File
import java.io.PrintWriter
import java.io.StringWriter
import java.io.Writer
import kotlin.system.exitProcess

/**
 *
 * @author Y&N
 * date: 2021/10/9
 * desc:崩溃日志信息上报，收集
 * 等到用户在启动的时候 把信息上传到服务器中
 */
class ExceptionHandler : Thread.UncaughtExceptionHandler {

    private lateinit var application: Application
    private lateinit var poolUtil: com.sinfotek.lib.common.RxThreadPoolUtil
    private var mDefaultHandler: Thread.UncaughtExceptionHandler? = null
    private var crashFilePath: File? = null
    private val sbInfo = StringBuffer()

    companion object {
        @JvmStatic
        fun instance(): ExceptionHandler {
            return SingletonHolder.mInstance
        }
    }

    /**
     * 静态内部类
     */
    private object SingletonHolder {
        val mInstance: ExceptionHandler = ExceptionHandler()
    }

    fun init(application: Application) {
        this.application = application
        crashFilePath = com.sinfotek.lib.common.RxSdUtil.getFileCacheDir(application)!!
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler(this)
        poolUtil = com.sinfotek.lib.common.RxThreadPoolUtil.instance()
    }


    /**
     * Method invoked when the given thread terminates due to the
     * given uncaught exception.
     *
     * Any exception thrown by this method will be ignored by the
     * Java Virtual Machine.
     * @param t the thread
     * @param e the exception
     */
    override fun uncaughtException(t: Thread, e: Throwable) {
        //如果没有处理就交给系统处理
        if (mDefaultHandler != null && !handlerException(t, e)) {
            mDefaultHandler!!.uncaughtException(t, e)
        } else {
            //用作自杀操作
            Process.killProcess(Process.myPid())
            //终止当前正在运行的Java虚拟机，导致程序终止，0表示正常终止，其它表示异常结束
            //在部分机型中，当退出应用后弹出应用程序崩溃的对话框，有时退出后还会再次启动，少部分的用户体验不太好
            exitProcess(0)
        }
    }

    /**
     * 获取信息
     *
     * @param t
     * @param e
     * @return
     */
    private fun handlerException(t: Thread?, e: Throwable?): Boolean {
        if (t == null || e == null) {
            return false
        }
        //收集手机信息
        collectDeviceInfo()
        //收集运行信息
        collectRunningMsg()
        collectExceptionInfo(t, e)
        //保存到文件中
        saveInfoToFile(e)
        return true
    }

    /**
     * 保存异常信息
     *
     * @param t
     * @param e
     */
    private fun collectExceptionInfo(t: Thread, e: Throwable) {
        sbInfo.append(
            """
            ====应用崩溃信息====
            
            """.trimIndent()
        )
        sbInfo.append("ThreadGroup = ")
            .append(if (t.threadGroup == null) "" else t.threadGroup.name).append("\n")
        sbInfo.append("Thread = ").append(t.name).append("\n")
        sbInfo.append("Priority = ").append(t.priority).append("\n")
        sbInfo.append("activeCount = ").append(Thread.activeCount()).append("\n")
        sbInfo.append("Throwable = ").append(e.message).append("\n")
        poolUtil.execute(object : Runnable {
            override fun run() {
                Looper.prepare()
                com.sinfotek.lib.common.RxToastUtil.showToast(application, "很抱歉,程序出现异常,即将退出.")
                //上传数据
                Looper.loop()
                poolUtil.remove(this)
            }
        })
    }

    /**
     * 保存到文件中
     *
     * @param e
     */
    private fun saveInfoToFile(e: Throwable) {
        val writer: Writer = StringWriter()
        val printWriter = PrintWriter(writer)
        e.printStackTrace(printWriter)
        sbInfo.append(writer.toString())
        printWriter.close()
        if (crashFilePath == null) {
            return
        }
        if (!crashFilePath!!.exists()) {
            return
        }
        //
        val l = System.currentTimeMillis()
        val ymd: String = com.sinfotek.lib.common.RxDateUtil.getDateByLong(l, com.sinfotek.lib.common.RxDateUtil.TYPE_YMD)
        val file = File(crashFilePath, ymd)
        if (!file.exists()) {
            file.mkdirs()
        }
        val date: String = com.sinfotek.lib.common.RxDateUtil.getDateByLong(l, com.sinfotek.lib.common.RxDateUtil.TYPE_YMD_HMS)
        //文件名还可以加上用户名等信息
        val fileName = "crash-$date.txt"
        //写入到文件中
        com.sinfotek.lib.common.RxFileUtil.writeDataToFile(sbInfo.toString(), file, fileName)
    }

    /**
     * 收集运行信息
     */
    private fun collectRunningMsg() {
        sbInfo.append(
            """
            ====设备运行时信息=====
            """.trimIndent()
        )
        val ramMemory: Array<Any> = RxAppInfoUtil.getRamMemory(application)
        val appMaxMemory: String = RxAppInfoUtil.getAppMaxMemory()
        val appAllocatedMemory: String = RxAppInfoUtil.getAppAllocatedMemory()
        val appFreeMemory: String = RxAppInfoUtil.getAppFreeMemory()
        sbInfo.append("totalMem = ").append(ramMemory[0]).append("\n")
            .append("availMem = ").append(ramMemory[1]).append(" \n")
            .append("lowMemory = ").append(ramMemory[2]).append("\n")
            .append("maxMemory = ").append(appMaxMemory).append(" \n")
            .append("allocated = ").append(appAllocatedMemory).append("\n")
            .append("freeMemory = ").append(appFreeMemory).append("\n")
    }

    /**
     * 采集手机信息
     */
    private fun collectDeviceInfo() {
        sbInfo.append(
            """
            ======设备参数信息======
            
            """.trimIndent()
        )
        //版本信息
        val appVersion = RxAppInfoUtil.getAppVersion(application)
        sbInfo.append("versionName = ").append(appVersion[0]).append("\n")
            .append("versionCode = ").append(appVersion[1]).append("\n") //获取设备信息
            //sdk版本
            .append("SDK_INT = ").append(Build.VERSION.SDK_INT).append("\n") //Android版本
            .append("RELEASE = ").append(Build.VERSION.RELEASE).append("\n") //手机制造商
            .append("PRODUCT = ").append(Build.PRODUCT).append("\n") //版本
            .append("MODEL = ").append(Build.MODEL).append("\n") //设备参数
            .append("DEVICE = ").append(Build.DEVICE).append("\n") //显示屏参数
            .append("DISPLAY = ").append(Build.DISPLAY).append("\n") //Android 系统定制商
            .append("BRAND = ").append(Build.BRAND).append("\n") //主板
            .append("BOARD = ").append(Build.BOARD).append("\n") //硬件名称
            .append("FINGERPRINT = ").append(Build.FINGERPRINT).append("\n") //修订版本列表
            .append("ID = ").append(Build.ID).append("\n") //硬件制造商
            .append("MANUFACTURER = ").append(Build.MANUFACTURER).append("\n")
            .append("USER = ").append(Build.USER).append("\n")
            .append("TIME = ").append(Build.TIME).append("\n")

        //CPU指令集
        val cpu = Build.SUPPORTED_ABIS
        val size = cpu?.size ?: 0
        for (i in 0 until size) {
            sbInfo.append("SUPPORTED_ABIS ").append(i).append(" = ").append(cpu!![i]).append("\n")
        }
    }
}