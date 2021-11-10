package com.sinfotek.component.net.config

import android.content.Context
import com.sinfotek.component.net.const.ApiConst
import okhttp3.Interceptor

/**
 *
 * @author Y&N
 * date: 2021/10/26
 * desc:
 */
class NetConfig private constructor(builder: Builder) {

    init {
        baseUrl = builder.baseUrl
        defaultTimeout = builder.defaultTimeout
        heads = builder.heads
        interceptors = builder.interceptors
        networkInterceptors = builder.networkInterceptors
        enableHttps = builder.enableHttps
    }

    companion object {
        /**
         * baseUrl
         */
        private var baseUrl: String = "";

        fun getBaseUrl(): String = baseUrl

        /**
         * 连接超时
         */
        private var defaultTimeout: Long = 0;

        fun getDefaultTimeout(): Long = defaultTimeout

        /**
         * token
         */
        private var mToken: String = "";

        fun getToken(): String = mToken

        /**
         * 设置拦截器
         */
        private var interceptors = ArrayList<Interceptor>()

        fun getInterceptors(): List<Interceptor> = interceptors

        /**
         * 设置拦截器
         */
        private var networkInterceptors = ArrayList<Interceptor>()

        fun getNetworkInterceptors(): List<Interceptor> = networkInterceptors

        /**
         * 添加头消息，也可以在自己的拦截器中添加，就不用设置这个了
         */
        private var heads = HashMap<String, Any>()

        fun getHeads(): Map<String, Any> = heads

        /**
         * 启用https
         */
        private var enableHttps: Boolean = false

        fun isEnableHttps(): Boolean = enableHttps
    }

    /**
     * 初始化上下文
     */
    fun initContext(context: Context) {
        NetAppContext.init(context)
    }

    open class Builder {
        internal var baseUrl: String = ""
        internal var defaultTimeout: Long = ApiConst.CONNECT_TIME_OUT
        internal var mToken: String = "";
        internal var interceptors = arrayListOf<Interceptor>()
        internal var networkInterceptors = arrayListOf<Interceptor>()
        internal var heads = HashMap<String, Any>()
        internal var enableHttps = false

        open fun setBaseUrl(baseUrl: String): Builder = apply {
            this.baseUrl = baseUrl
        }

        open fun setDefaultTimeout(defaultTimeout: Long): Builder = apply {
            this.defaultTimeout = defaultTimeout
        }

        open fun setToken(token: String): Builder = apply {
            this.mToken = token
        }

        open fun addInterceptor(interceptor: Interceptor): Builder = apply {
            this.interceptors.add(interceptor)
        }

        open fun addNetworkInterceptors(interceptor: Interceptor): Builder = apply {
            this.networkInterceptors.add(interceptor)
        }

        open fun setHeads(headers: Map<String, Any>): Builder = apply {
            this.heads.putAll(headers)
        }

        open fun enableHttps(enableHttps: Boolean): Builder = apply {
            this.enableHttps = enableHttps
        }

        open fun build(): NetConfig = NetConfig(this)
    }
}