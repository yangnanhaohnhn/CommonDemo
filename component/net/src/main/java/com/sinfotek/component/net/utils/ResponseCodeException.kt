package com.sinfotek.component.net.utils

import java.lang.RuntimeException

/**
 *
 * @author Y&N
 * date: 2021/11/9
 * desc:
 */
class ResponseCodeException(val code: String) : RuntimeException("Http request failed with response code $code")
