package com.sinfotek.lib.base.ui

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import com.sinfotek.lib.base.mvvm.vm.BaseFreshViewModel

/**
 *
 * @author Y&N
 * date: 2021/11/26
 * desc:
 */
abstract class BaseFreshMvActivity<M, B : ViewDataBinding, VM: BaseFreshViewModel<M>> : BaseMvActivity<B, VM>(){
    override fun onBindLayoutId(): Int = onFreshBindLayoutId()

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        initFreshView()
    }

    /**
     * 初始化Fresh
     */
    private fun initFreshView() {
    }

    protected abstract fun onFreshBindLayoutId(): Int

//    protected abstract fun get

}