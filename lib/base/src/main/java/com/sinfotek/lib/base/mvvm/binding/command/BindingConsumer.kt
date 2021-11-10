package com.sinfotek.lib.base.mvvm.binding.command

/**
 * A one-argument action.
 *
 * @author EDZ
 * @param <T> the first argument type
</T> */
interface BindingConsumer<T> {
    /**
     * 带参数的调用
     * @param t
     */
    fun call(t: T)
}