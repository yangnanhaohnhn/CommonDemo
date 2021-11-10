package com.sinfotek.lib.base.ui

import android.app.Activity
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog
import com.sinfotek.lib.base.mvvm.view.IBaseView
import com.sinfotek.lib.common.ActivityManage
import com.sinfotek.lib.common.RxThreadPoolUtil
import java.lang.ref.WeakReference

/**
 *
 * @author Y&N
 * date: 2021/10/25
 * desc:
 */
abstract class BaseActivity : AppCompatActivity(), IBaseView {
    private var dialog: QMUITipDialog? = null
    private lateinit var proxy: RxThreadPoolUtil
    /** 当前Activity的弱引用，防止内存泄露  */
    private var activityWeakReference: WeakReference<Activity>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initCreate(savedInstanceState)
        activityWeakReference = WeakReference(this)
        proxy = RxThreadPoolUtil.instance()
        setContentView(getLayoutId())
        initView(savedInstanceState)
        initData(savedInstanceState)
        initListener()
        ActivityManage.instance.addActivity(activityWeakReference)
    }

    protected open fun initListener() {
    }

    protected open fun initData(savedInstanceState: Bundle?) {
    }

    /**
     * 初始化LayoutId
     */
    abstract fun getLayoutId(): Int

    /**
     *   初始化View
     */
    abstract fun initView(savedInstanceState: Bundle?)

    protected open fun initCreate(savedInstanceState: Bundle?) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    override fun showLoading(msg: String) {
        showLoading(msg, true)
    }

    /**
     * 关闭loading
     */
    override fun hideLoading() {
        if (dialog != null && dialog!!.isShowing) {
            dialog!!.dismiss()
            dialog = null
        }
    }

    /**
     * 显示loading
     *
     * @param msg dialog.setCancelable(false);
     * dialog弹出后会点击屏幕或物理返回键，dialog不消失
     *
     *
     * dialog.setCanceledOnTouchOutside(false);
     * dialog弹出后会点击屏幕，dialog不消失；点击物理返回键dialog消失
     */
    open fun showLoading(msg: String?, isCancelable: Boolean) {
        if (dialog == null) {
            val builder = QMUITipDialog.Builder(this)
            builder.setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
            builder.setTipWord(msg)
            dialog = builder.create(isCancelable)
        }
        dialog!!.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityManage.instance.finishAllActivity()
    }
}