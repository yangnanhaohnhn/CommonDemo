package com.sinfotek.lib.common

import android.content.Context
import android.widget.Button
import es.dmoral.toasty.Toasty

/**
 *
 * @author Y&N
 * date: 2021/12/8
 * desc:
 */
fun showToast(context: Context, msgId: Int) {
    if (0 == msgId) {
        return
    }
    showToast(context, context.getString(msgId))
}

fun showToast(context: Context, msg: String = "无数据") {
    Toasty.normal(context, msg).show()
}

fun showWarnToast(context: Context, msgId: Int) {
    if (0 == msgId) {
        return
    }
    showWarnToast(context, context.getString(msgId))
}

fun showWarnToast(context: Context, msg: String = "警告！") {
    Toasty.normal(context, msg).show()
}

fun showErrorToast(context: Context, msgId: Int) {
    if (0 == msgId) {
        return
    }
    showErrorToast(context, context.getString(msgId))
}

fun showErrorToast(context: Context, msg: String = "异常！") {
    Toasty.normal(context, msg).show()
}

fun showSuccessToast(context: Context, msgId: Int) {
    if (0 == msgId) {
        return
    }
    showSuccessToast(context, context.getString(msgId))
}

fun showSuccessToast(context: Context, msg: String = "成功！") {
    Toasty.normal(context, msg).show()
}

