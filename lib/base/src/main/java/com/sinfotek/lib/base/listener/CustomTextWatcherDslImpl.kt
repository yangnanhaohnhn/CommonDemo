package com.sinfotek.lib.base.listener

import android.text.Editable
import android.text.TextWatcher

/**
 *
 * @author Y&N
 * date: 2022-3-24
 * desc: 自定义TextWatcher
 */
class CustomTextWatcherDslImpl : TextWatcher {
    private var afterTextChanged: ((Editable?) -> Unit)? = null
    private var beforeTextChanged: ((CharSequence?, Int, Int, Int) -> Unit)? = null
    private var onTextChanged: ((CharSequence?, Int, Int, Int) -> Unit)? = null

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        beforeTextChanged?.invoke(s, start, count, after)
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        onTextChanged?.invoke(s, start, before, count)
    }

    override fun afterTextChanged(s: Editable?) {
        afterTextChanged?.invoke(s)
    }

    fun beforeTextChanged(method: (CharSequence?, Int, Int, Int) -> Unit) {
        beforeTextChanged = method
    }

    fun afterTextChanged(method: (Editable?) -> Unit) {
        afterTextChanged = method
    }

    fun onTextChanged(method: (CharSequence?, Int, Int, Int) -> Unit) {
        onTextChanged = method
    }
}