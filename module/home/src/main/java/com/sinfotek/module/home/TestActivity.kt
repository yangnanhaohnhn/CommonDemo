package com.sinfotek.module.home

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.sinfotek.lib.base.mvvm.vm.BaseViewModel
import com.sinfotek.lib.base.ui.BaseMvActivity
import com.sinfotek.module.home.databinding.ActivityTestBinding


/**
 *
 * @author Y&N
 * date: 2022-4-26
 * desc:
 */
class TestActivity : BaseMvActivity<ActivityTestBinding, BaseViewModel>() {
    override fun onBindLayoutId() = R.layout.activity_test

    override fun initView(savedInstanceState: Bundle?) {

    }

    class TestAdapter(data: MutableList<String>) :
        BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_test, data) {

        /**
         * Implement this method and use the helper to adapt the view to the given item.
         *
         * @param helper A fully initialized helper.
         * @param item   The item that needs to be displayed.
         */
        override fun convert(helper: BaseViewHolder?, item: String?) {
            TODO("Not yet implemented")
        }

    }

}