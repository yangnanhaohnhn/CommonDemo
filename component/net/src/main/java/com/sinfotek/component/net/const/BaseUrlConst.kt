package com.sinfotek.component.net.const

/**
 *
 * @author Y&N
 * date: 2021/10/8
 * desc:
 */
object BaseUrlConst {
    private const val URL_HTTP_FLAG = "http"

    private const val URL_IP_ADDRESS = "118.26.64.162"
    private const val URL_PORT_ADDRESS = "9917"

    /**
     * 线上
     */
    var BASE_URL = "$URL_HTTP_FLAG://$URL_IP_ADDRESS:$URL_PORT_ADDRESS"

}