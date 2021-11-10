package com.sinfotek.component.net.config

import com.sinfotek.component.net.ApiRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 *
 * @author Y&N
 * date: 2021/11/8
 * desc:
 */
open class BaseNetModel {
    val apiRetrofit = ApiRetrofit.instance()

    inline fun <reified T> create(): T = apiRetrofit.create(T::class.java)

    fun createQueryMap(name: String, paramMap: Map<String, Any>) =
        apiRetrofit.createQueryMap(name, paramMap)

    suspend fun <T> Call<T>.await(): T {
        return suspendCoroutine { continuation ->
            enqueue(object : Callback<T> {
                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }

                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if (body != null) continuation.resume(body)
                    else continuation.resumeWithException(RuntimeException("response body is null"))
                }
            })
        }
    }
}