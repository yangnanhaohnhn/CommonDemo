package com.sinfotek.module.home

import android.app.Activity
import android.app.Application
import android.widget.Toast
import androidx.databinding.ObservableField
import com.sinfotek.lib.base.mvvm.binding.command.BindingConsumer
import com.sinfotek.lib.base.mvvm.binding.command.BindingConsumerCommand
import com.sinfotek.lib.base.mvvm.model.BaseModel
import com.sinfotek.lib.base.mvvm.vm.BaseViewModel
import com.sinfotek.lib.common.showToast

/**
 *
 * @author Y&N
 * date: 2022/3/2
 * desc:
 */
class HomeVm(activity: Activity, application: Application, model: BaseModel) : BaseViewModel(application) {
    val registerInfo = ObservableField(0)
    val registerOp = BindingConsumerCommand(object : BindingConsumer<Any> {
        /**
         * 带参数的调用
         * @param t
         */
        override fun call(t: Any) {
            if (R.id.rb_nan == t) {
                registerInfo.set(0)
            } else {
                registerInfo.set(1)
            }
            showToast(activity, registerInfo.get().toString())
        }
    })

}