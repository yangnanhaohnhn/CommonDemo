package com.sinfotek.component.net

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.sinfotek.component.net.config.NetConfig
import com.sinfotek.component.net.const.ApiConst
import com.sinfotek.component.net.utils.SSLContextUtil
import com.sinfotek.lib.common.RxGsonUtil
import com.sinfotek.lib.common.const.BaseConst
import com.sinfotek.lib.common.log.RxLogUtil
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 *
 * @author Y&N
 * date: 2021/10/8
 * desc:
 */
class ApiRetrofit {
    private val retrofit: Retrofit

    companion object {
        @JvmStatic
        fun instance(): ApiRetrofit {
            return SingletonHolder.mInstance
        }
    }

    private object SingletonHolder {
        val mInstance = ApiRetrofit()
    }

    init {
        //添加请求log拦截器
        val builder = OkHttpClient.Builder()
        builder.addInterceptor(logInterceptor)
        builder.addInterceptor(RequestInterceptor())
        builder.addInterceptor(ResponseInterceptor())
        // 添加配置增加拦截器
        val interceptors = NetConfig.getInterceptors()
        if (interceptors.isNotEmpty()) {
            interceptors.forEach { builder.addInterceptor(it) }
        }
        if (BaseConst.isOpenDebug) {
            builder.addNetworkInterceptor(StethoInterceptor())
        }
        // 添加网络拦截器
        val networkInterceptors = NetConfig.getNetworkInterceptors()
        if (networkInterceptors.isNotEmpty()) {
            networkInterceptors.forEach { builder.addInterceptor(it) }
        }
        builder.connectTimeout(NetConfig.getDefaultTimeout(), TimeUnit.SECONDS)
        builder.readTimeout(NetConfig.getDefaultTimeout(), TimeUnit.SECONDS)
        builder.writeTimeout(NetConfig.getDefaultTimeout(), TimeUnit.SECONDS)
        builder.retryOnConnectionFailure(true)
        // 判断是否启用 https
        if (NetConfig.isEnableHttps()) {
            //给client的builder添加了增加可以忽略SSL
            val sslParams = SSLContextUtil.getSslSocketFactory()
            builder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
            builder.hostnameVerifier(SSLContextUtil.UnSafeHostnameVerifier)
        }

        builder.connectionPool(ConnectionPool(8, 10, TimeUnit.SECONDS))
            .build()
        retrofit = Retrofit.Builder() //支持返回String字符串
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .baseUrl(NetConfig.getBaseUrl())
            .client(builder.build())
            .build()
    }


    /**
     * create you ApiService
     * Create an implementation of the API endpoints defined by the `service` interface.
     */
//    fun <T> create(service: Class<T>?): T {
//        if (service == null) {
//            throw RuntimeException("Api service is null!")
//        }
//        return retrofit.create(service)
//    }

    fun <S> create(serviceClass: Class<S>): S = retrofit.create(serviceClass)

//    inline fun <reified T> create(): T = create(T::class.java)

    /**
     * GET请求
     *
     * @param name
     * @param paramMap
     * @return
     */
    fun createQueryMap(name: String, paramMap: Map<String, Any>): Map<String, Any> {
        val json = RxGsonUtil.toJson(paramMap)
        RxLogUtil.logApi(ApiConst.API_INFO, "QueryMap: $name|$json")
        return paramMap
    }

    private val logInterceptor: HttpLoggingInterceptor
        get() {
            // 日志显示级别
            val level: HttpLoggingInterceptor.Level = HttpLoggingInterceptor.Level.BASIC
            // 新建日志拦截器
            val loggingInterceptor = HttpLoggingInterceptor { message: String ->
                RxLogUtil.logApi(ApiConst.API_INFO, "response->$message")
            }
            loggingInterceptor.setLevel(level)
            return loggingInterceptor
        }


    private inner class RequestInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
//            val uuid: String = UserInfo.instance().getUuid()

            val request: Request
//            if (TextUtils.isEmpty(uuid)) {
            request = chain.request()
//            } else {
//                val originalRequest = chain.request()
//                request =
//                    originalRequest.newBuilder().addHeader(RequestInterceptor.Authorization, uuid)
//                        .build()
//                RxLogUtil.logApi(ApiConstants.API_INFO, "header:$uuid")
//            }
            return chain.proceed(request)
        }
    }

    /**
     * 网络请求之后返回的 response 拦截器
     */
    private inner class ResponseInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            val startTime = System.currentTimeMillis()
            val response = chain.proceed(chain.request())
            val endTime = System.currentTimeMillis()
            val duration = endTime - startTime
            val mediaType = response.body!!.contentType()
            val content = response.body!!.string()
            RxLogUtil.i(
                "response",
                """
                $request
                RequestHead:${request.headers}
                ResponseResult:$content
                ----------End:${duration}毫秒----------
                """.trimIndent()
            )
            return response.newBuilder()
                .body(ResponseBody.create(mediaType, content))
                .build()
        }
    }
}