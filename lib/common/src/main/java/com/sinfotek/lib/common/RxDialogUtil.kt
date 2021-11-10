package com.sinfotek.lib.common

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.text.TextUtils
import com.qmuiteam.qmui.widget.dialog.QMUIDialog
import com.qmuiteam.qmui.widget.dialog.QMUIDialog.CheckableDialogBuilder
import com.qmuiteam.qmui.widget.dialog.QMUIDialog.MessageDialogBuilder
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction

/**
 *
 * @author Y&N
 * date: 2021/11/1
 * desc:
 */
object RxDialogUtil {

    /**
     * 显示提示框
     *
     * @param context
     * @param msg
     */
    fun showTipDialog(context: Context, msg: String) {
        showTipDialog(context, "温馨提示", msg, null)
    }

    /**
     * 显示提示框
     *
     * @param context
     * @param msg
     */
    fun showTipDialog(context: Context, title: String, msg: String) {
        showTipDialog(context, title, msg, null)
    }

    /**
     * 显示提示框
     *
     * @param context
     * @param msg
     * @param listener
     */
    fun showTipDialog(
        context: Context,
        msg: String,
        listener: QMUIDialogAction.ActionListener?
    ) {
        showTipDialog(context, "温馨提示", msg, listener)
    }

    /**
     * 显示提示框
     *
     * @param context
     * @param msg
     * @param listener
     */
    fun showTipDialog(
        context: Context,
        title: String,
        msg: String,
        listener: QMUIDialogAction.ActionListener?
    ) {
        val builder = MessageDialogBuilder(context)
        builder.setTitle(title).setMessage(msg)
        builder.addAction("确定") { dialog: QMUIDialog, index: Int ->
            dialog.dismiss()
            listener?.onClick(dialog, index)
        }
        builder.show()
    }

    /**
     * 显示对话框
     *
     * @param context
     * @param msg
     * @param okListener
     */
    fun showConfirmDialog(
        context: Context, title: String, msg: String, okStr: String, cancelStr: String,
        okListener: QMUIDialogAction.ActionListener?
    ) {
        showConfirmDialog(context, title, msg, okStr, cancelStr, okListener, null)
    }

    /**
     * 显示对话框
     *
     * @param context
     * @param msg
     * @param okListener
     */
    fun showConfirmDialog(
        context: Context, title: String, msg: String, okStr: String,
        okListener: QMUIDialogAction.ActionListener?
    ) {
        showConfirmDialog(context, title, msg, okStr, "取消", okListener, null)
    }

    /**
     * 显示对话框
     *
     * @param context
     * @param msg
     * @param okListener
     */
    fun showConfirmDialog(
        context: Context, title: String, msg: String,
        okListener: QMUIDialogAction.ActionListener?
    ) {
        showConfirmDialog(context, title, msg, "确定", "取消", okListener, null)
    }

    /**
     * 显示对话框
     *
     * @param context
     * @param msg
     * @param okListener
     */
    fun showConfirmDialog(
        context: Context, msg: String,
        okListener: QMUIDialogAction.ActionListener?
    ) {
        showConfirmDialog(context, "温馨提示", msg, "确定", "取消", okListener, null)
    }

    /**
     * 显示对话框
     *
     * @param context
     * @param msg
     */
    fun showConfirmDialog(context: Context, title: String, msg: String) {
        showConfirmDialog(context, title, msg, null)
    }

    /**
     * 显示对话框
     *
     * @param context
     * @param msg
     */
    fun showConfirmDialog(context: Context, msg: String) {
        showConfirmDialog(context, "温馨提示", msg)
    }

    /**
     * 显示对话框
     *
     * @param context
     * @param msg
     * @param okListener
     * @param cancelListener
     */
    fun showConfirmDialog(
        context: Context,
        title: String,
        msg: String,
        okStr: String,
        cancelStr: String,
        okListener: QMUIDialogAction.ActionListener?,
        cancelListener: QMUIDialogAction.ActionListener?
    ) {
        val builder = MessageDialogBuilder(context)
        builder.setTitle(title).setMessage(msg)
        builder.addAction(
            if (TextUtils.isEmpty(cancelStr)) "取消" else cancelStr
        ) { dialog: QMUIDialog, index: Int ->
            dialog.dismiss()
            cancelListener?.onClick(dialog, index)
        }
        builder.addAction(
            if (TextUtils.isEmpty(okStr)) "确定" else okStr
        ) { dialog: QMUIDialog, index: Int ->
            dialog.dismiss()
            okListener?.onClick(dialog, index)
        }
        builder.show()
    }

    /**
     * * 显示单选
     *
     * @param activity
     * @param name
     */
    fun showSingleChooseDialog(
        activity: Activity,
        name: String,
        defaultContent: String,
        items: Array<String>,
        listener: OnClickSingleListener?
    ) {
        val builder = CheckableDialogBuilder(activity)
        builder.setTitle(name)
        var checkedIndex = -1
        if (!TextUtils.isEmpty(defaultContent)) {
            for (i in items.indices) {
                val item = items[i]
                if (defaultContent == item) {
                    checkedIndex = i
                    break
                }
            }
        }
        builder.checkedIndex = checkedIndex
        builder.addItems(items) { dialog: DialogInterface, which: Int ->
            listener?.onClickSingle(which)
            dialog.dismiss()
        }
        builder.addAction("取消") { dialog: QMUIDialog, _: Int -> dialog.dismiss() }
        builder.show()
    }



    interface OnClickSingleListener {
        /**
         * 点击事件
         *
         * @param which 没有的话 就是-1
         */
        fun onClickSingle(which: Int)
    }
}