package com.sinfotek.component.net.const

/**
 *
 * @author Y&N
 * date: 2021/10/22
 * desc:
 */
object ApiConst {
    const val BASE_URL = "https://news-at.zhihu.com/api/4/"
    const val DEFAULT_HEADERS = "Content-Type:application/json"
    const val DEFAULT_HEADERS2 = "Accept: application/json"
    const val Encoding = "UTF-8"

    /**
     * 网络连接超时
     */
    const val CONNECT_TIME_OUT = 60L
    const val API_INFO = "ApiUrl"

    /**
     * //成功的标志
     */
    const val RESULT_OK = "200"
}