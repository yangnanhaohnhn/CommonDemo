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
}