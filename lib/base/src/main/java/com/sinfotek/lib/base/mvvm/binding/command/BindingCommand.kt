package com.sinfotek.lib.base.mvvm.binding.command

/**
 * Create 2021/5/11
 *
 * @author N
 * desc:执行的命令回调, 用于ViewModel与xml之间的数据绑定
 */
class BindingActionCommand(private val execute: BindingAction) {
    /**
     * 执行BindingAction命令
     */
    fun execute() {
        execute.call()
    }
}

class BindingConsumerCommand<T>(private val consumer: BindingConsumer<T>) {
    /**
     * 执行带泛型参数的命令
     *
     * @param parameter 泛型参数
     */
    fun execute(parameter: T) {
        consumer.call(parameter)
    }
}
