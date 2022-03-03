package com.sinfotek.module.home

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.sinfotek.chart.LineChartHelper
import com.sinfotek.lib.base.listener.CommonMvMethodImpl
import com.sinfotek.lib.base.listener.CommonMvMethodListener
import com.sinfotek.lib.base.mvvm.vm.BaseViewModel
import com.sinfotek.lib.base.ui.BaseMvActivity
import com.sinfotek.lib.common.RxDateUtil
import com.sinfotek.lib.common.log.RxLogUtil
import com.sinfotek.lib.common.showToast
import com.sinfotek.module.home.databinding.ActivityHomeBinding

/**
 *
 * @author Y&N
 * date: 2021/10/25
 * desc:
 */
class HomeActivity : BaseMvActivity<ActivityHomeBinding, HomeVm>() {
    override fun onBindLayoutId() = R.layout.activity_home

    override fun onBindViewModel() = HomeVm::class.java

    override fun getMvvmImpl() = object : CommonMvMethodImpl() {
        override fun onBindViewModelFactory(): ViewModelProvider.AndroidViewModelFactory {
            return HomeViewModelFactory(application, this@HomeActivity)
        }

        override fun onBindVariableId(): Int {
            return BR.homeVm
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)

        mBinding.btnStart.setOnClickListener {

            showToast(this, "点击返回")

            val intent = Intent()
            intent.putExtra("code", 1)
            setResult(Activity.RESULT_OK)
            finish()
        }

        val helper = LineChartHelper(mBinding.chartLine)
        val xValList = mutableListOf<String>()
        val yValList = mutableListOf<Entry>()
        val xValList2 = mutableListOf<String>()
        val yValList2 = mutableListOf<Entry>()
        yValList.add(Entry(0F, 1F))
        xValList.add(System.currentTimeMillis().toString())
        yValList.add(Entry(1F, 1.2F))
        xValList.add((System.currentTimeMillis() + 1000).toString())
        yValList.add(Entry(2F, 1.1F))
        xValList.add((System.currentTimeMillis() + 5000).toString())
        yValList.add(Entry(3F, 0.2F))
        xValList.add((System.currentTimeMillis() + 10000).toString())
        yValList.add(Entry(4F, 0.4F))
        xValList.add((System.currentTimeMillis() + 15000).toString())
        yValList.add(Entry(5F, 1.9F))
        xValList.add((System.currentTimeMillis() + 20000).toString())

        yValList2.add(Entry(0F, 1.3F))
        xValList2.add(System.currentTimeMillis().toString())
        yValList2.add(Entry(1F, 2.2F))
        xValList2.add((System.currentTimeMillis() + 1000).toString())
        yValList2.add(Entry(2F, 1.1F))
        xValList2.add((System.currentTimeMillis() + 5000).toString())
        yValList2.add(Entry(3F, 1.2F))
        xValList2.add((System.currentTimeMillis() + 10000).toString())
        yValList2.add(Entry(4F, 0.4F))
        xValList2.add((System.currentTimeMillis() + 15000).toString())
        yValList2.add(Entry(5F, 1.7F))
        xValList2.add((System.currentTimeMillis() + 20000).toString())
        helper.addSingleEntry("Line1", xValList = xValList, yValEntryList = yValList)
            .addSingleEntry(
                "Line2",
                color = Color.RED,
                xValList = xValList2,
                yValEntryList = yValList2
            ).xAxisValueFormatter()
            .initMarkerView(this) {
                if (0 == it.dataSetIndex) {

                } else {

                }
            }.notifyChart()
    }
}