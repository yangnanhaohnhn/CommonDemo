package com.example.commondemo

import com.sinfotek.component.bus.InitBus
import com.sinfotek.component.choose.InitChoose
import com.sinfotek.component.db.DbContext
import com.sinfotek.component.db.DbHelper
import com.sinfotek.component.net.InitNet
import com.sinfotek.lib.base.BaseApplication
import com.sinfotek.lib.common.const.InitCommon
import kotlin.concurrent.thread

/**
 *
 * @author Y&N
 * date: 2021/10/25
 * desc:
 */
open class DemoApp : BaseApplication() {

    //初始化Db
    private lateinit var dbHelper: DbHelper

    override fun onCreate() {
        super.onCreate()
        mApp = this
        init()
    }

    companion object {
        private lateinit var mApp: DemoApp
        fun getApp(): DemoApp {
            return mApp
        }
    }

    private fun init() {
        //初始化Bus
        InitBus.init()
        //开启网络
        InitNet.init(this)
        DbContext.init(this)
        dbHelper = DbHelper.instance
        InitCommon.init(this)
        InitChoose.initTbs(this)
    }

    fun getDbHelper() = dbHelper

}