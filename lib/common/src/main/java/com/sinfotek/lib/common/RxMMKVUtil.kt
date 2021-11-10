package com.sinfotek.lib.common

import android.content.Context
import com.tencent.mmkv.MMKV

/**
 *
 * @author Y&N
 * date: 2021/10/15
 * desc:
 */
object RxMMKVUtil {
    private const val mvName = "mmkv_name"

    private val mv: MMKV
        ///data/user/0/packageName/files/mmkv
        get() = MMKV.mmkvWithID(mvName)

    fun init(context: Context) {
        MMKV.initialize(context)
    }

    /** 查询某个key是否已经存在
     *
     * @param key
     * @return
     */
    fun contains(key: String) = mv.contains(key)

    /**
     * 删除全部数据(传了参数就是按key删除)
     */
    fun deleteKeyOrAll(key: String? = null) {
        if (key == null) mv.clearAll()
        else mv.removeValueForKey(key)
    }

    fun putBoolean(key: String?, defaultValue: Boolean): Boolean {
        return mv.encode(key, defaultValue)
    }

    fun putInt(key: String?, defaultValue: Int): Boolean {
        return mv.encode(key, defaultValue)
    }

    fun putLong(key: String?, defaultValue: Long): Boolean {
        return mv.encode(key, defaultValue)
    }

    fun putString(key: String?, defaultValue: String?): Boolean {
        return mv.encode(key, defaultValue)
    }

    fun getBoolean(key: String?): Boolean {
        return getBoolean(key, false)
    }

    fun getBoolean(key: String?, defaultValue: Boolean): Boolean {
        return mv.decodeBool(key, defaultValue)
    }

    fun getInt(key: String?): Int {
        return getInt(key, -1)
    }

    fun getInt(key: String?, defaultValue: Int): Int {
        return mv.decodeInt(key, defaultValue)
    }

    fun getLong(key: String?): Long {
        return getLong(key, -1L)
    }

    fun getLong(key: String?, defaultValue: Long): Long {
        return mv.decodeLong(key, defaultValue)
    }

    fun getString(key: String?): String? {
        return getString(key, "")
    }

    fun getString(key: String?, defaultValue: String?): String? {
        return mv.decodeString(key, defaultValue)
    }
}