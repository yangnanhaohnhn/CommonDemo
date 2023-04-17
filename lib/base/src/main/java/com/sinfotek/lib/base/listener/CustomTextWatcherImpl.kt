package com.sinfotek.lib.base.listener

import android.text.Editable
import android.text.TextWatcher

/**
 *
 * @author Y&N
 * date: 2022-3-24
 * desc: 自定义TextWatcher
 */
private typealias AfterTextChangedCallback = (Editable?) -> Unit
private typealias BeforeTextChangedCallback = (CharSequence?, Int, Int, Int) -> Unit
private typealias OnTextChangedCallback = (CharSequence?, Int, Int, Int) -> Unit

class CustomTextWatcherImpl : TextWatcher {
    private var afterTextChangedCallback: AfterTextChangedCallback? = null
    private var beforeTextChangedCallback: BeforeTextChangedCallback? = null
    private var onTextChangedCallback: OnTextChangedCallback? = null

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        beforeTextChangedCallback?.invoke(s, start, count, after)
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        onTextChangedCallback?.invoke(s, start, before, count)
    }

    override fun afterTextChanged(s: Editable?) {
        afterTextChangedCallback?.invoke(s)
    }

    fun beforeTextChanged(callback: BeforeTextChangedCallback) {
        beforeTextChangedCallback = callback
    }

    fun afterTextChanged(callback: AfterTextChangedCallback) {
        afterTextChangedCallback = callback
    }

    fun onTextChanged(callback: OnTextChangedCallback) {
        onTextChangedCallback = callback
    }
}