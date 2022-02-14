package com.sinfotek.lib.common.router

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.alibaba.android.arouter.launcher.ARouter
import com.sinfotek.lib.common.const.BaseConst

/**
 *
 * @author Y&N
 * date: 2021/12/8
 * desc:
 */
fun startToActivity(path: String) {
    ARouter.getInstance()
        .build(path)
        .withTransition(0, 0)
        .navigation()
}

/**
 * 跳转
 *
 * @param path
 * @param flag
 */
fun startToActivity(path: String, flag: Int) {
    ARouter.getInstance().build(path)
        .withTransition(0, 0)
        .addFlags(flag)
        .navigation()
}

fun startToActivity(path: String, bundle: Bundle) {
    ARouter.getInstance().build(path)
        .withBundle(BaseConst.INTENT_DATA, bundle)
        .withTransition(0, 0)
        .navigation()
}

/**
 * 跳转带参数
 *
 * @param path
 * @param bundle
 * @param flag
 */
fun startToActivity(path: String, bundle: Bundle, flag: Int) {
    ARouter.getInstance().build(path)
        .withBundle(BaseConst.INTENT_DATA, bundle)
        .withTransition(0, 0)
        .addFlags(flag)
        .navigation()
}

/**
 * 跳转页面
 *
 * @param activity
 * @param path
 * @param requestCode
 */
fun startForResult(activity: FragmentActivity, path: String, requestCode: Int) {
    ARouter.getInstance().build(path)
        .withTransition(0, 0)
        .navigation(activity, requestCode)
}

/**
 * 跳转页面
 *
 * @param activity
 * @param path
 * @param requestCode
 */
fun startForResult(activity: FragmentActivity, path: String, bundle: Bundle, requestCode: Int) {
    ARouter.getInstance().build(path)
        .withBundle(BaseConst.INTENT_DATA, bundle)
        .withTransition(0, 0)
        .navigation(activity, requestCode)
}


