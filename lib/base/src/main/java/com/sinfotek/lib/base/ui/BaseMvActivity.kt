package com.sinfotek.lib.base.ui

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.launcher.ARouter
import com.sinfotek.lib.base.listener.CommonMvMethodImpl
import com.sinfotek.lib.base.listener.CommonMvMethodListener
import com.sinfotek.lib.base.mvvm.vm.BaseViewModel

/**
 *
 * @author Y&N
 * date: 2021/10/8
 * desc:
 */
abstract class BaseMvActivity<B : ViewDataBinding, VM : BaseViewModel<*>> : BaseActivity(){
    private lateinit var mvImpl: CommonMvMethodListener
    protected open lateinit var mBinding: B
    protected open lateinit var mViewModel: VM

    override fun initView(savedInstanceState: Bundle?) {
        //注入ARouter
        ARouter.getInstance().inject(this)
        mvImpl = getMvvmImpl()
        //私有的初始化Databinding和ViewModel方法
        initViewDataBinding()
        //私有的ViewModel与View的契约事件回调逻辑
        registerUiChangeLiveDataCallBack()
    }

    private fun registerUiChangeLiveDataCallBack() {
        //showLoading
        mViewModel.uc.showLoadingEvent.observe(this) { t -> showLoading(t!!) }
        //隐藏Loading
        mViewModel.uc.hideLoadingEvent.observe(this) { hideLoading() }
    }

    /**
     * 初始化ViewDataBinding
     */
    private fun initViewDataBinding() {
        mBinding = DataBindingUtil.setContentView(this, getLayoutId())
        val viewModelId = mvImpl.onBindVariableId()
        mViewModel = createViewModel()
        //DataBinding绑定生命周期
//        mBinding.lifecycleOwner = this
        mBinding.setVariable(viewModelId, mViewModel)
        lifecycle.addObserver(mViewModel)

        //初始化Observable
        mvImpl.initViewObservable()
    }

    /**
     * 创建ViewModel
     */
    private fun createViewModel(): VM {
        //ViewModelProvider 默认创建无参的构造方法
        var factory = ViewModelProvider.AndroidViewModelFactory(application)
        val androidViewModelFactory = mvImpl.onBindViewModelFactory()
        if (androidViewModelFactory != null) {
            factory = androidViewModelFactory
        }
        return ViewModelProvider(this, factory)[onBindViewModel()]
    }

    /**
     * ViewModel的类
     * 默认的是BaseView.class
     *
     * @return
     */
    protected open fun onBindViewModel(): Class<VM> = BaseViewModel::class.java as Class<VM>

    override fun getLayoutId(): Int {
        return onBindLayoutId()
    }

    protected open fun getMvvmImpl(): CommonMvMethodListener {
        return CommonMvMethodImpl()
    }

    abstract fun onBindLayoutId(): Int

    override fun onDestroy() {
        mBinding.unbind()
        hideLoading()
        super.onDestroy()
    }
}
