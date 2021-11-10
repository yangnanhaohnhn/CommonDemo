package com.sinfotek.lib.base.mvvm.binding.command

/**
 * Create 2021/5/11
 *
 * @author N
 * desc:执行的命令回调, 用于ViewModel与xml之间的数据绑定
 */
class BindingCommand<T> {
    private var execute: BindingAction? = null
    private var consumer: BindingConsumer<T>? = null

    constructor(execute: BindingAction?) {
        this.execute = execute
    }

    /**
     * @param execute 带泛型参数的命令绑定
     */
    constructor(execute: BindingConsumer<T>?) {
        consumer = execute
    }

    /**
     * 执行BindingAction命令
     */
    fun execute() {
        if (execute != null) {
            execute!!.call()
        }
    }

    /**
     * 执行带泛型参数的命令
     *
     * @param parameter 泛型参数
     */
    fun execute(parameter: T) {
        if (consumer != null) {
            consumer!!.call(parameter)
        }
    }
}