package com.example.commondemo

import com.alibaba.android.arouter.facade.annotation.Route
import com.example.commondemo.databinding.ActivityPagingBinding
import com.sinfotek.lib.base.listener.CommonMvMethodImpl
import com.sinfotek.lib.base.ui.BaseMvActivity

/**
 *
 * @author Y&N
 * date: 2022-3-25
 * desc:
 */
@Route(path = RouteResConst.PAGING_PATH)
class Paging3Activity : BaseMvActivity<ActivityPagingBinding, Paging3ViewModel>() {
    override fun onBindLayoutId() = R.layout.activity_paging

    override fun onBindViewModel() = Paging3ViewModel::class.java

    override fun getMvvmImpl() = object : CommonMvMethodImpl() {
        override fun onBindViewModelFactory() =
            AppViewModelFactory(application, this@Paging3Activity)

        override fun initViewObservable() {
            super.initViewObservable()

        }
    }

}