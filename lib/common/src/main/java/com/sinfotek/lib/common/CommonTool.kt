package com.sinfotek.lib.common

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.text.Editable
import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import java.lang.NullPointerException

/**
 *
 * @author Y&N
 * date: 2021/12/8
 * desc: 公共工具
 */
fun isEmptyList(data: List<*>?) = (data == null || data.isEmpty())

fun isEmpty(data: String?) = (data == null || data.isEmpty() || "null" == data)

/**
 * 对Map进行判空
 */
fun isEmptyMap(data: Map<*, *>?) = (data == null || data.isEmpty())

fun checkNull(any: Any?, msg: String = "must not be null") {
    if (null == any) {
        throw NullPointerException("$msg must not be null")
    }
}

fun View.showText(msg: String?, defaultText: String = "无") {
    when (this) {
        is Button -> this.text = if (isEmpty(msg)) defaultText else msg
        is TextView -> this.text = if (isEmpty(msg)) defaultText else msg
    }
}

/**
 * 处理数据
 * 去掉最后的一个 type
 *
 * @param type
 */
fun String.dealWithSymbol(type: String = ","): String {
    if (TextUtils.isEmpty(this)) {
        return this
    }
    if (!this.contains(type)) {
        return this
    }
    //如果最后一个没有不是这个 则认为当前数据是正确的
    return if (this.endsWith(type))
        this.substring(0, this.lastIndexOf(type))
    else
        this
}

fun getAppVersionName(context: Context): String? {
    val manager = context.packageManager
    val info = manager.getPackageInfo(context.packageName, PackageManager.GET_ACTIVITIES)
    return info.versionName
}

fun getAppVersionCode(context: Context): Int {
    val manager = context.packageManager
    val info = manager.getPackageInfo(context.packageName, PackageManager.GET_ACTIVITIES)
    return info.versionCode
}

/**
 * 获取屏幕长度
 */
fun getScreenWidth(activity: Activity): Int {
    val localDisplayMetrics = DisplayMetrics()
    activity.windowManager.defaultDisplay.getMetrics(localDisplayMetrics)
    return localDisplayMetrics.widthPixels
}

/**
 * 获取屏幕高度
 */
fun getScreenHeight(activity: Activity): Int {
    val localDisplayMetrics = DisplayMetrics()
    activity.windowManager.defaultDisplay.getMetrics(localDisplayMetrics)
    return localDisplayMetrics.heightPixels
}

/**
 * dp转px
 */
fun Float.dp2px(context: Context): Int {
    val scale = context.applicationContext.resources.displayMetrics.density
    return (this * scale + 0.5f).toInt()
}

/**
 * 得到状态栏高度
 */
fun getStatusBarHeight(context: Context): Int {
    var result = 0
    val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
        result = context.resources.getDimensionPixelSize(resourceId)
    }
    return if (result == 0) 25f.dp2px(context) else result
}


///**
// * 请求权限
// */
//inline fun AppCompatActivity.registerPermission(crossinline block: (Boolean) -> Unit) =
//    this.registerForActivityResult(ActivityResultContracts.RequestPermission()) { res -> block(res) }
//
///**
// * 请求多个权限
// */
//inline fun AppCompatActivity.registerMultiPermission(crossinline block: (Map<String, Boolean>) -> Unit) =
//    this.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { res ->
//        block(
//            res
//        )
//    }


