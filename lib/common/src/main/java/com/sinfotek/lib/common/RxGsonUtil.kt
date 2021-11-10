package com.sinfotek.lib.common

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.lang.Exception
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * Create 2021/5/7
 *
 * @author N
 * desc:
 */
object RxGsonUtil {
    /**
     * 转成json
     */
    fun toJson(obj: Any?): String? {
        var res: String? = null
        try {
            res = Gson().toJson(obj)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return res
    }

    /**
     * 转成json
     * 解决一下转义的问题
     */
    fun toJson2(obj: Any?): String? {
        var res: String? = null
        try {
            val gson = GsonBuilder().disableHtmlEscaping().create()
            res = gson.toJson(obj)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return res
    }

    fun <T> parseJson(json: String?, clz: Class<T>?): T? {
        var t: T? = null
        try {
            t = Gson().fromJson(json, clz)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return t
    }

    /**
     * 将Map转化为Json
     *
     * @param map
     * @return String
     */
    fun <T> mapToJson(map: Map<String?, T>?): String {
        val gson = Gson()
        return gson.toJson(map)
    }

    /**
     * 将Map转化为Json
     *
     * @return String
     */
    fun <T> parseJsonToMap(json: String?): Map<String, T> {
        val amountCurrencyType = object : TypeToken<Map<String?, T>?>() {}.type
        return Gson().fromJson(json, amountCurrencyType)
    }

    fun <T> parseJsonToList(
        json: String?,
        clz: Class<*>
    ): List<T> {
        val type: Type = ParameterizedTypeImpl(clz)
        return Gson().fromJson(json, type)
    }

    private class ParameterizedTypeImpl(var clazz: Class<*>) : ParameterizedType {
        override fun getActualTypeArguments(): Array<Type> {
            return arrayOf(clazz)
        }

        override fun getRawType(): Type {
            return MutableList::class.java
        }

        override fun getOwnerType(): Type? {
            return null
        }
    }
}