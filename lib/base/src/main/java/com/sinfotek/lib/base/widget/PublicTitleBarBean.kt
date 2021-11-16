package com.sinfotek.lib.base.widget

import android.view.View
import androidx.databinding.BaseObservable
import com.sinfotek.lib.base.mvvm.binding.command.BindingCommand
import java.io.Serializable

/**
 *
 * @author Y&N
 * date: 2021/11/15
 * desc: 公共的标题
 */
class PublicTitleBarBean : BaseObservable(), Serializable {
    var title: String? = null
        set(value) {
            field = value
            notifyChange()
        }

    var leftImgVisibility = View.VISIBLE
        set(value) {
            field = value
            notifyChange()
        }
    var leftListener: BindingCommand<*>? = null
        set(value) {
            field = value
            notifyChange()
        }
    var midListener: BindingCommand<*>? = null
        set(value) {
            field = value
            notifyChange()
        }
    var midImgVisibility = View.GONE
        set(value) {
            field = value
            notifyChange()
        }
    var midImgResource: Int = 0
        set(value) {
            field = value
            notifyChange()
        }

    /**
     * 默认不显示
     */
    var rightImgVisibility = View.GONE
        set(value) {
            field = value
            notifyChange()
        }
    var rightImgResource: Int = 0
        set(value) {
            field = value
            notifyChange()
        }
    var rightListener: BindingCommand<*>? = null
        set(value) {
            field = value
            notifyChange()
        }

    var rightTextVisibility = View.GONE
        set(value) {
            field = value
            notifyChange()
        }
    var rightTextListener: BindingCommand<*>? = null
        set(value) {
            field = value
            notifyChange()
        }

    var rightText: String? = null
        set(value) {
            field = value
            notifyChange()
        }
}