package com.sinfotek.lib.base.listener

import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView

/**
 *
 * @author Y&N
 * date: 2022-3-24
 * desc: DSL语句
 */
//创建与原函数同名的扩展函数，函数参数为实现类扩展函数
fun TextView.addTextChangedListenerDsl(init: CustomTextWatcherDslImpl.() -> Unit) {
    val listener = CustomTextWatcherDslImpl()
    listener.init()
    this.addTextChangedListener(listener)
}
//使用新的 typealias
fun TextView.addTextChangedListener(init: CustomTextWatcherImpl.() -> Unit){
    val listener = CustomTextWatcherImpl()
    listener.init()
    this.addTextChangedListener(listener)
}

//高阶函数
//inline 关键字通常用于修饰高阶函数，用于提升性能
//crossinline 声明的 lambda 不允许局部返回，用于避免调用者错误的使用 return 导致函数中断。
//crossinline关键字用于保证在Lambda表达式中一定不使用return关键字，这样冲突就不存在了
inline fun TextView.addTextChangedListenerClosure(
    crossinline afterTextChanged: (Editable?) -> Unit = {},
    crossinline beforeTextChanged: (CharSequence?, Int, Int, Int) -> Unit = { _, _, _, _ -> },
    crossinline onTextChanged: (CharSequence?, Int, Int, Int) -> Unit = { _, _, _, _ -> }
) {
    val listener = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            beforeTextChanged.invoke(s, start, count, after)
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            onTextChanged.invoke(s, start, before, count)
        }

        override fun afterTextChanged(s: Editable?) {
            afterTextChanged.invoke(s)
        }
    }
    this.addTextChangedListener(listener)
}