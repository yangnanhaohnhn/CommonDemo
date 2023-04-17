package com.sinfotek.lib.common

import android.content.Context
import android.os.Parcelable
import com.tencent.mmkv.MMKV

/**
 *
 * @author Y&N
 * date: 2021/10/15
 * desc:
 */
object RxMMKVUtil {
    private const val mvName = "mmkv_name"

    ///data/user/0/packageName/files/mmkv
    private val mv = MMKV.mmkvWithID(mvName)

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

    fun putValue(key: String, defaultValue: Any? = null) {
        when (defaultValue) {
            is String -> mv.encode(key, defaultValue)
            is Boolean -> mv.encode(key, defaultValue)
            is Int -> mv.encode(key, defaultValue)
            is Long -> mv.encode(key, defaultValue)
            is Float -> mv.encode(key, defaultValue)
            is Double -> mv.encode(key, defaultValue)
            is Parcelable -> mv.encode(key, defaultValue)
            is ByteArray -> mv.encode(key, defaultValue)
        }
    }

    fun getValue(key: String, defaultValue: Any?): Any? {
        return when (defaultValue) {
            is String -> mv.decodeString(key, defaultValue)
            is Boolean -> mv.decodeBool(key, defaultValue)
            is Int -> mv.decodeInt(key, defaultValue)
            is Long -> mv.decodeLong(key, defaultValue)
            is Float -> mv.decodeFloat(key, defaultValue)
            is Double -> mv.decodeDouble(key, defaultValue)
            is ByteArray -> mv.decodeBytes(key, defaultValue)
            else -> {}
        }
    }
}