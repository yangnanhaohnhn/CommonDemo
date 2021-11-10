package com.sinfotek.lib.common

import android.content.Context
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Create 2021/6/1
 *
 * @author N
 * desc:
 */
object RxDateUtil {
    const val TYPE_YMD = "yyyy-MM-dd"
    const val TYPE_YMD_HMS = "yyyy-MM-dd HH:mm:ss"
    const val TYPE_1 = "HH:mm"
    const val TYPE_2 = "yyyy-MM-dd HH:mm"
    private fun getCommonDate(type: String): SimpleDateFormat {
        return SimpleDateFormat(type, Locale.CHINA)
    }

    fun getDateByLong(time: Long, type: String): String {
        return getCommonDate(type).format(Date(time))
    }

    fun getDateByDate(date: Date, type: String): String {
        return getCommonDate(type).format(date)
    }

    /**
     * 2017-1-1 1:1 - 时间戳
     *
     * @param data
     * @return
     */
    fun getLongByStr(data: String, type: String): Long {
        val simpleDateFormat = SimpleDateFormat(type, Locale.CHINESE)
        try {
            return simpleDateFormat.parse(data)!!.time
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return System.currentTimeMillis()
    }

    fun getDayByLong(time: Long): String {
        val date = Date(time)
        val calendar = Calendar.getInstance()
        calendar.time = date
        return when (calendar[Calendar.DAY_OF_WEEK]) {
            1 -> "星期天"
            2 -> "星期一"
            3 -> "星期二"
            4 -> "星期三"
            5 -> "星期四"
            6 -> "星期五"
            else -> "星期六"
        }
    }

    fun getSectionForPosition(time: Long): Int {
        return getTime(time)
    }

    /**
     * 根据毫秒时间戳来格式化字符串
     * 今天显示今天、昨天显示昨天、前天显示前天.
     * 早于前天的显示具体年-月-日，如2017-06-12；
     *
     * @param timeStamp 毫秒值
     * @return 今天 昨天 前天 或者 yyyy-MM-dd HH:mm:ss类型字符串
     */
    private fun getTime(timeStamp: Long): Int {
        val calendar = Calendar.getInstance();
        calendar.timeInMillis = System.currentTimeMillis()
        val todayHoursSeconds = calendar[Calendar.HOUR] * 60 * 60
        val todayMinutesSeconds = calendar[Calendar.MINUTE] * 60
        val todaySeconds = calendar[Calendar.SECOND]
        val todayMillis = (todayHoursSeconds + todayMinutesSeconds + todaySeconds) * 1000
        val todayStartMillis = calendar.timeInMillis - todayMillis
        if (timeStamp >= todayStartMillis) {
            return 0
        }
        val oneDayMillis = 24 * 60 * 60 * 1000
        val yesterdayStartMillis = todayStartMillis - oneDayMillis
        if (timeStamp >= yesterdayStartMillis) {
            return 1
        }
        val yesterdayBeforeStartMillis = yesterdayStartMillis - oneDayMillis
        return if (timeStamp >= yesterdayBeforeStartMillis) {
            2
        } else 3
    }

    /**
     * 选择开始时间是否通过
     *
     * @param startTime
     * @param endTime
     * @return true : 通过
     */
    fun isPassStartTime(context: Context?, startTime: Long, endTime: Long): Boolean {
        //开始时间只需要小于结束时间， 开始时间不能大于当前时间
        if (endTime != 0L) {
            if (startTime > endTime) {
                RxToastUtil.showWarnToast(context!!, "开始时间不能大于结束时间！")
                return false
            }
        }
        if (startTime > System.currentTimeMillis()) {
            RxToastUtil.showWarnToast(context!!, "开始时间不能大于当前时间！")
            return false
        }
        return true
    }

    /**
     * 选择结束时间是否通过
     *
     * @param startTime
     * @param endTime
     * @return true : 通过
     */
    fun isPassEndTime(context: Context?, startTime: Long, endTime: Long): Boolean {
        //结束时间不能大于开始时间
        if (startTime != 0L) {
            if (startTime > endTime) {
                RxToastUtil.showWarnToast(context!!, "结束时间不能小于开始时间！")
                return false
            }
        }
        return true
    }

    /**
     * 获取昨天的时间
     *
     * @return
     */
    val oneDayTime: Long
        get() = getOneDayTime(System.currentTimeMillis())

    /**
     * 获取昨天的时间
     *
     * @return
     */
    fun getOneDayTime(time: Long): Long {
        val cal = GregorianCalendar.getInstance()
        cal.time = Date(time)
        cal.add(Calendar.DAY_OF_YEAR, -1)
        return cal.time.time
    }

    /**
     * 获取三天前的时间
     *
     * @return
     */
    fun getThreeDayTime(time: Long): Long {
        val cal = GregorianCalendar.getInstance()
        cal.time = Date(time)
        cal.add(Calendar.DAY_OF_YEAR, -3)
        return cal.time.time
    }

    /**
     * 获取三天前的时间
     *
     * @return
     */
    val threeDayTime: Long
        get() = getThreeDayTime(System.currentTimeMillis())

    /**
     * 获取七天前的时间
     *
     * @return
     */
    fun getSevenDayTime(time: Long): Long {
        val cal = GregorianCalendar.getInstance()
        cal.time = Date(time)
        cal.add(Calendar.DAY_OF_YEAR, -7)
        return cal.time.time
    }

    /**
     * 获取七天前的时间
     *
     * @return
     */
    val sevenDayTime: Long
        get() = getSevenDayTime(System.currentTimeMillis())

    @JvmStatic
    fun main(args: Array<String>) {
        println(getDateByLong(oneDayTime, TYPE_YMD_HMS))
    }
}