package com.sinfotek.lib.common.log

import android.util.Log

/**
 *
 * @author Y&N
 * date: 2021/9/30
 * desc:
 */
object RxLogUtil {
    private const val START_DOUBLE_DIVIDER =
        "─────────────────────────────────────start──────────────────────────────────────────────"
    private const val END_DOUBLE_DIVIDER =
        "──────────────────────────────────────end───────────────────────────────────────────────"
    private const val HORIZONTAL_LINE = '│'
    private const val TAG_DEFAULT = "RxLog"
    private const val V = 0x1
    private const val D = 0x2
    private const val I = 0x3
    private const val W = 0x4
    private const val E = 0x5
    private const val XML = 0x6

    private var IS_SHOW_LOG = true
    private var IS_SHOW_HEAD = true
    fun init(isShowLog: Boolean) {
        IS_SHOW_LOG = isShowLog
    }

    fun setIsShowHead(isShowHead: Boolean) {
        IS_SHOW_HEAD = isShowHead
    }

    /**
     * Verbose就是冗长啰嗦的：通常表达开发调试过程中的一些详细信息，不过滤地输出所有调试信息。
     */
    fun v(tag: String?, msg: String?) {
        printLog(V, tag!!, msg!!)
    }

    fun v(msg: String?) {
        printLog(V, null, msg)
    }

    /**
     * Info来表达一些信息：用Log.i()能输出Info、Warning、Error级别的Log信息。
     */
    fun i(tag: String?, msg: String?) {
        printLog(I, tag, msg)
    }

    fun i(msg: String?) {
        printLog(I, msg)
    }


    /**
     * Debug来表达调试信息：用Log.d()能输出Debug、Info、Warning、Error级别的Log信息。
     */
    fun d(tag: String?, msg: String?) {
        printLog(D, tag, msg)
    }

    fun d(msg: String?) {
        printLog(D, msg)
    }

    /**
     * Warning表示警告：但不一定会马上出现错误，开发时有时用来表示特别注意的地方。用Log.w()能输出Warning、Error级别的Log信息。
     */
    fun w(tag: String?, msg: String?) {
        printLog(W, tag, msg)
    }

    fun w(msg: String?) {
        printLog(W, msg)
    }

    /**
     * Error表示出现错误：是最需要关注解决的。用Log.e()输出，能输出Error级别的Log信息。
     */
    fun e(tag: String?, msg: String?) {
        printLog(E, tag, msg)
    }

    fun e(msg: String?) {
        printLog(E, msg)
    }

    /**
     * 打印Api
     *
     * @param tag
     * @param message
     */
    fun logApi(tag: String?, message: String?) {
        d(tag, message)
    }

    /**
     * 打印定位信息
     *
     * @param message
     */
    fun logLocationInfo(message: String?) {
        if (!IS_SHOW_LOG) {
            Log.w(TAG_DEFAULT, "Log 已关闭,请先开启日志")
            return
        }
        Log.v("locationInfo", message!!)
    }

    //    /**
    //     * 打印Xml
    //     *
    //     * @param message
    //     */
    //    public static void logXml(String message) {
    //        if (!IS_SHOW_LOG) {
    //            Log.w(TAG_DEFAULT, "Log 已关闭,请先开启日志");
    //            return;
    //        }
    //        Logger.xml(message);
    //    }
    //    /**
    //     * 打印Xml
    //     *
    //     * @param message
    //     */
    //    public static void logJson(String message) {
    //        if (!IS_SHOW_LOG) {
    //            Log.w(TAG_DEFAULT, "Log 已关闭,请先开启日志");
    //            return;
    //        }
    //        Logger.json(message);
    //    }
    private fun printLog(type: Int, msg: String?) {
        printLog(type, TAG_DEFAULT, msg)
    }

    private fun printLog(type: Int, tagStr: String?, msg: String?) {
        if (!IS_SHOW_LOG) {
            Log.w(TAG_DEFAULT, "Log 已关闭,请先开启日志")
            return
        }
        printSub(type, tagStr, START_DOUBLE_DIVIDER)
        //打印头信息
        logHeaderContent(type, tagStr)
        //打印哪里打印的
        printSub(type, tagStr, "$HORIZONTAL_LINE $msg")
        printSub(type, tagStr, END_DOUBLE_DIVIDER)
    }

    private fun logHeaderContent(logType: Int, tag: String?) {
        val trace =
            Thread.currentThread().stackTrace
        if (IS_SHOW_HEAD) {
            printSub(
                logType,
                tag!!,
                "$HORIZONTAL_LINE Thread: " + Thread.currentThread().name
            )
        }
        var level = ""
        val stackOffset: Int = getStackOffset(trace)

        //corresponding method count with the current stack may exceeds the stack trace. Trims the count
        var methodCount = 2
        if (methodCount + stackOffset > trace.size) {
            methodCount = trace.size - stackOffset - 1
        }

        for (i in methodCount downTo 1) {
            val stackIndex: Int = i + stackOffset
            if (stackIndex >= trace.size) {
                continue
            }
            val builder = StringBuilder()
            builder.append(HORIZONTAL_LINE)
                .append(' ')
                .append(level)
                .append(trace[stackIndex].className)
                .append(".")
                .append(trace[stackIndex].methodName)
                .append(" ")
                .append(" (")
                .append(trace[stackIndex].fileName)
                .append(":")
                .append(trace[stackIndex].lineNumber)
                .append(")")
            level += "   "
            printSub(logType, tag!!, builder.toString())
        }
    }


    /**
     * Determines the starting index of the stack trace, after method calls made by this class.
     *
     * @param trace the stack trace
     * @return the stack offset
     */
    private fun getStackOffset(trace: Array<StackTraceElement>): Int {
        checkNotNull(trace)
        var i = 5
        while (i < trace.size) {
            val e = trace[i]
            val name = e.className
            if (name != RxLogUtil::class.simpleName) {
                return --i
            }
            i++
        }
        return -1
    }

    private fun <T> checkNotNull(obj: T?): T {
        if (obj == null) {
            throw NullPointerException()
        }
        return obj
    }

    private fun printSub(type: Int, tag: String?, objects: String?) {
        when (type) {
            V ->
                Log.v(tag, objects!!)
            D ->
                Log.d(tag, objects!!)
            I ->
                Log.i(tag, objects!!)
            W ->
                Log.w(tag, objects!!)
            E ->
                Log.e(tag, objects!!)
        }
    }
}