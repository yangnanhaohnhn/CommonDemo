package com.sinfotek.lib.common

/**
 *
 * @author Y&N
 * date: 2021/10/9
 * desc: 快速点击
 */
object RxFastClickUtil {
    private var lastClickTime: Long = 0
    private const val TIME: Long = 800

    /**
     * 快速点击
     *  true:已经快速点击了
     */
    fun isFastDoubleClick(): Boolean {
        val time = System.currentTimeMillis()
        if (time - lastClickTime < TIME) {
            return true
        }
        lastClickTime = time
        return false
    }
}