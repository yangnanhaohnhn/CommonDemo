package com.sinfotek.lib.base.mvvm.binding.viewadapter.view

import android.view.View
import android.widget.ImageView
import android.widget.RadioGroup
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.sinfotek.lib.base.mvvm.binding.command.BindingActionCommand
import com.sinfotek.lib.base.mvvm.binding.command.BindingConsumerCommand
import com.sinfotek.lib.common.RxFastClickUtil.isFastDoubleClick

/**
 * requireAll 是意思是是否需要绑定全部参数, false为否
 * View的onClick事件绑定
 * onClickCommand 绑定的命令,
 * isThrottleFirst 是否开启防止过快点击
 * 伴随对象的成员很像其它语言中的静态成员，但在运行时它们任然是真正对象的成员实例
 */
@BindingAdapter(value = ["onClickCommand", "isThrottleFirst"], requireAll = false)
fun onClickCommand(view: View?, clickCommand: BindingActionCommand, isThrottleFirst: Boolean) {
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

/**
 * 设置图片
 */
@BindingAdapter("imageRes")
fun setImageResource(imageView: ImageView, resId: Int) {
    imageView.setImageResource(resId)
}

/**
 * 显示或隐藏
 */
@BindingAdapter("showOrHide")
fun showOrHide(view: View, isShow: Boolean) {
    view.isVisible = isShow
}

/**
 * 显示或隐藏
 */
@BindingAdapter("showOrInvisible")
fun showOrInvisible(view: View, isShow: Boolean) {
    view.isInvisible = !isShow
}

/**
 * 注册 RadioGroup
 */
@BindingAdapter("registerRadioGroup")
fun registerRadioGroup(radioGroup: RadioGroup, clickCommand: BindingConsumerCommand<Any>) {
    radioGroup.setOnCheckedChangeListener { _, checkedId ->
        clickCommand.execute(checkedId)
    }
}



