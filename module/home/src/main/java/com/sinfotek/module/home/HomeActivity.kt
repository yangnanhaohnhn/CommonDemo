package com.sinfotek.module.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.databinding.ViewDataBinding
import com.sinfotek.lib.base.mvvm.vm.BaseViewModel
import com.sinfotek.lib.base.ui.BaseMvActivity
import com.sinfotek.lib.common.showToast
import com.sinfotek.module.home.databinding.ActivityHomeBinding

/**
 *
 * @author Y&N
 * date: 2021/10/25
 * desc:
 */
class HomeActivity : BaseMvActivity<ActivityHomeBinding, BaseViewModel<*>>() {
    override fun onBindLayoutId(): Int {
        return R.layout.activity_home
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)

        mBinding.btnStart.setOnClickListener {

            showToast(this, "点击返回")

            val intent = Intent()
            intent.putExtra("code",1)
            setResult(Activity.RESULT_OK)
            finish()
        }
    }
}