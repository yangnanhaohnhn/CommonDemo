package com.sinfotek.component.net.service

import com.sinfotek.component.net.bean.LoginBean
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap
import retrofit2.http.Url

/**
 *
 * @author Y&N
 * date: 2021/10/22
 * desc:
 */
interface AppService {

    /**
     * 登录
     *
     * @param url：请求地址
     * @param body：封装参数
     * @return ：返回信息
     */
    @JvmSuppressWildcards
    @GET
    fun login(@Url url: String, @QueryMap body: Map<String, Any>): Call<LoginBean>
}