package com.sinfotek.lib.common

import android.app.Activity
import android.content.Context
import android.os.IBinder
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import java.lang.NullPointerException

/**
 *
 * @author Y&N
 * date: 2021/10/8
 * desc:
 */
object RxUiUtil {

    /**
     * 处理数据
     * 去掉最后的一个 type
     *
     * @param data
     * @param type
     */
    fun dealWithData(data: String, type: String): String {
        if (TextUtils.isEmpty(data)) {
            return data
        }
        if (!data.contains(type)) {
            return data
        }
        //如果最后一个没有不是这个 则认为当前数据是正确的
        return if (!data.endsWith(type)) {
            data
        } else data.substring(0, data.lastIndexOf(type))
    }

    /**
     * 处理数据
     * 去掉最后的一个 type
     *
     * @param data
     */
    fun dealWithData(data: String): String {
        return dealWithData(data, ",")
    }

    fun hide(view: View, hide: Boolean) {
        view.isVisible = !hide
//        if (hide) {
//            view.visibility = View.GONE
//        } else {
//            view.visibility = View.VISIBLE
//        }
    }

    fun isVisible(view: View, hide: Boolean) {
//        if (hide) {
        view.isInvisible = hide
//            view.visibility = View.INVISIBLE
//        } else {
//            view.visibility = View.VISIBLE
//        }
    }


    /**
     * 关闭键盘
     *
     * @param activity
     */
    fun hideSoftBoard(activity: Activity) {
        val focus = activity.currentFocus
        if (focus != null) {
            hideSoftInput(activity, focus.windowToken)
        }
    }

    /**
     * 隐藏软键盘
     */
    private fun hideSoftInput(context: Context, windowToken: IBinder?) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (windowToken != null) {
            // 强制隐藏软键盘
            imm.hideSoftInputFromWindow(windowToken, 0)
        }
    }

    /**
     * 对集合进行判断
     *
     * @param data
     * @return
     */
    fun isEmptyList(data: List<*>?): Boolean {
        return data == null || data.isEmpty()
    }

    fun isEmpty(data: String?): Boolean {
        return data == null || data.isEmpty() || "null" == data
    }

    fun isEmptyMap(data: Map<Any, Any>?): Boolean {
        return data == null || data.isEmpty()
    }

    /**
     * 检查是否为空
     *
     * @param any
     * @param name
     */
    fun checkNull(any: Any?, name: String) {
        if (any == null) {
            throw NullPointerException("$name must not be null")
        }
    }

    /**
     * 显示文字
     *
     * @param view
     * @param msg
     * @param defaultText
     */
    fun showText(view: View?, msg: String, defaultText: String) {
        val s: String = dealWithShowText(msg, defaultText)
        if (view is Button) {
            view.text = s
        } else if (view is TextView) {
            view.text = s
        }
    }

    /**
     * 处理显示数据
     *
     * @param msg
     */
    fun dealWithShowText(msg: String, defaultMsg: String): String {
        if (!isEmpty(msg)) {
            return msg
        }
        return if (!isEmpty(defaultMsg)) {
            defaultMsg
        } else "无"
    }

}