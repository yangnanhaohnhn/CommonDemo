package com.sinfotek.component.db

import android.content.Context
import java.lang.NullPointerException
import java.lang.ref.WeakReference

/**
 *
 * @author Y&N
 * date: 2021/10/26
 * desc:
 */
object DbContext {
    private var mContext: WeakReference<Context>? = null

    fun init(context: Context) {
        this.mContext = WeakReference(context.applicationContext)
    }

    fun getContext(): Context = mContext?.get() ?: throw NullPointerException("Db not init")
}