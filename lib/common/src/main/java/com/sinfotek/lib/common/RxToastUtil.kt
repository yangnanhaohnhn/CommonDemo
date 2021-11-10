package com.sinfotek.lib.common

import android.content.Context
import android.text.TextUtils
import android.widget.Toast
import es.dmoral.toasty.Toasty

/**
 *
 * @author Y&N
 * date: 2021/10/8
 * desc:
 */
object RxToastUtil {
    /**
     * Toast 替代方法 ：立即显示无需等待
     *
     * @param mContext
     * @param resId    String资源ID
     */
    fun showToast(mContext: Context, resId: Int) {
        showToast(mContext, mContext.getString(resId))
    }

    /**
     * Toast
     *
     * @param mContext
     * @param msg
     */
    fun showToast(mContext: Context, msg: String) {
        if (TextUtils.isEmpty(msg)) {
            return
        }
        Toasty.normal(mContext, msg, Toast.LENGTH_SHORT).show()
    }


    /**
     * Toast 替代方法 ：立即显示无需等待
     *
     * @param mContext
     * @param resId    String资源ID
     */
    fun showWarnToast(mContext: Context, resId: Int) {
        showWarnToast(mContext, mContext.getString(resId))
    }

    /**
     * Toast
     *
     * @param mContext
     * @param msg
     */
    fun showWarnToast(mContext: Context, msg: String) {
        if (TextUtils.isEmpty(msg)) {
            return
        }
        Toasty.warning(mContext, msg, Toast.LENGTH_LONG).show()
    }

    /**
     * Toast 替代方法 ：立即显示无需等待
     *
     * @param mContext
     * @param resId    String资源ID
     */
    fun showErrorToast(mContext: Context, resId: Int) {
        showErrorToast(mContext, mContext.getString(resId))
    }

    /**
     * Toast
     *
     * @param mContext
     * @param msg
     */
    fun showErrorToast(mContext: Context, msg: String) {
        if (TextUtils.isEmpty(msg)) {
            return
        }
        Toasty.error(mContext, msg, Toast.LENGTH_LONG).show()
    }

    /**
     * Toast 替代方法 ：立即显示无需等待
     *
     * @param mContext
     * @param resId    String资源ID
     */
    fun showSuccessToast(mContext: Context, resId: Int) {
        showSuccessToast(mContext, mContext.getString(resId))
    }

    /**
     * Toast
     *
     * @param mContext
     * @param msg
     */
    fun showSuccessToast(mContext: Context, msg: String) {
        if (TextUtils.isEmpty(msg)) {
            return
        }
        Toasty.success(mContext, msg, Toast.LENGTH_LONG).show()
    }
}