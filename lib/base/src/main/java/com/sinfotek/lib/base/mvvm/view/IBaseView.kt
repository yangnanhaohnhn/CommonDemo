package com.sinfotek.lib.base.mvvm.view

/**
 *
 * @author Y&N
 * date: 2021/10/25
 * desc:
 */
interface IBaseView {
    /**
     * 显示loading
     * @param msg
     */
    fun showLoading(msg: String)

    /**
     * 隐藏
     */
    fun hideLoading()
}