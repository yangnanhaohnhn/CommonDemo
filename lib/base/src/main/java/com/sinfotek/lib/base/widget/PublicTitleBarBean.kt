package com.sinfotek.lib.base.widget

import android.view.View
import androidx.databinding.BaseObservable
import com.sinfotek.lib.base.mvvm.binding.command.BindingActionCommand
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

    var leftImgVisibility = true
        set(value) {
            field = value
            notifyChange()
        }

    var leftListener: BindingActionCommand? = null
        set(value) {
            field = value
            notifyChange()
        }

    var midListener: BindingActionCommand? = null
        set(value) {
            field = value
            notifyChange()
        }
    var midImgVisibility = false
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
    var rightImgVisibility = false
        set(value) {
            field = value
            notifyChange()
        }
    var rightImgResource: Int = 0
        set(value) {
            field = value
            notifyChange()
        }
    var rightListener: BindingActionCommand? = null
        set(value) {
            field = value
            notifyChange()
        }

    var rightTextVisibility = false
        set(value) {
            field = value
            notifyChange()
        }
    var rightTextListener: BindingActionCommand? = null
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