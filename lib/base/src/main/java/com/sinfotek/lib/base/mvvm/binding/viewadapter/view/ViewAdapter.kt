package com.sinfotek.lib.base.mvvm.binding.viewadapter.view

import android.view.View
import androidx.databinding.BindingAdapter
import com.sinfotek.lib.base.mvvm.binding.command.BindingCommand
import com.sinfotek.lib.common.RxFastClickUtil.isFastDoubleClick


/**
 * requireAll 是意思是是否需要绑定全部参数, false为否
 * View的onClick事件绑定
 * onClickCommand 绑定的命令,
 * isThrottleFirst 是否开启防止过快点击
 * 伴随对象的成员很像其它语言中的静态成员，但在运行时它们任然是真正对象的成员实例
 */
@BindingAdapter(value = ["onClickCommand", "isThrottleFirst"], requireAll = false)
fun onClickCommand(view: View?, clickCommand: BindingCommand<*>, isThrottleFirst: Boolean) {
    if (isThrottleFirst) {
        view!!.setOnClickListener { clickCommand.execute() }
    } else {
        view!!.setOnClickListener {
            if (!isFastDoubleClick()) {
                clickCommand.execute()
            }
        }
    }
}