package com.sinfotek.component.net.bean

import com.google.gson.JsonSyntaxException
import com.sinfotek.component.net.R
import com.sinfotek.component.net.config.NetAppContext
import com.sinfotek.component.net.const.ApiConst
import com.sinfotek.component.net.utils.ResponseCodeException
import com.sinfotek.lib.common.RxGsonUtil
import com.sinfotek.lib.common.log.RxLogUtil
import com.sinfotek.lib.common.showWarnToast
import java.io.Serializable
import java.lang.Exception
import java.lang.RuntimeException
import java.net.ConnectException
import java.net.NoRouteToHostException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 *
 * @author Y&N
 * date: 2021/10/8
 * desc:
 */
class ResponseResult constructor(
    val msgId: String,
    val status: Int = STATUS_SUCCESS,
    val value: Any?
) : Serializable {
    var errorMsg: String = ""

    companion object {
        private fun <T> createSuccessResponse(res: T, msgId: String) =
            ResponseResult(msgId, STATUS_SUCCESS, res)

        private fun <T> createFailureResponse(res: T, msgId: String) =
            ResponseResult(msgId, STATUS_FAILURE, res)

        private fun createErrorResponse(errorMsg: String, msgId: String): ResponseResult {
            val responseResult = ResponseResult(msgId, STATUS_ERROR, null)
            responseResult.errorMsg = errorMsg
            return responseResult
        }

        const val STATUS_FAILURE = -1
        const val STATUS_SUCCESS = 0
        const val STATUS_ERROR = -2

        fun <T : BaseBean> success(res: T, msgId: String): ResponseResult {
            val baseBean = res as BaseBean
            return when (baseBean.code) {
                ApiConst.RESULT_OK -> {
                    RxLogUtil.logApi(
                        ApiConst.API_INFO,
                        "Success： " + msgId + "--->" + RxGsonUtil.toJsonHtml(res)
                    )
                    createSuccessResponse(res, msgId)
                }
                else -> {
                    RxLogUtil.logApi(
                        ApiConst.API_INFO,
                        "Failure： " + msgId + "--->" + RxGsonUtil.toJsonHtml(res)
                    )
                    createFailureResponse(res, msgId)
                }
            }
        }

        fun error(isShow: Boolean, exception: Exception, msgId: String): ResponseResult {
            val msg = getFailureTips(exception)
            if (isShow) {
                showWarnToast(NetAppContext.getContext(), msg)
            }
            RxLogUtil.logApi(
                ApiConst.API_INFO,
                "Error： " + msgId + "--->" + exception.message
            )
            return createErrorResponse(msg, msgId)
        }

        /**
         * 当网络请求没有正常响应的时候，根据异常类型进行相应的处理。
         * @param e 异常实体类
         */
        private fun getFailureTips(e: Throwable?): String {
            return when (e) {
                is ConnectException -> NetAppContext.getContext()
                    .getString(R.string.network_connect_error)
                is SocketTimeoutException -> NetAppContext.getContext()
                    .getString(R.string.network_connect_timeout)
                is ResponseCodeException -> NetAppContext.getContext()
                    .getString(R.string.network_response_code_error) + e.code
                is NoRouteToHostException -> NetAppContext.getContext()
                    .getString(R.string.no_route_to_host)
                is UnknownHostException -> NetAppContext.getContext()
                    .getString(R.string.network_error)
                is JsonSyntaxException -> NetAppContext.getContext()
                    .getString(R.string.json_data_error)
                is RuntimeException -> e.message!!
                else -> {
                    NetAppContext.getContext().getString(R.string.unknown_error)
                }
            }
        }
    }

    override fun toString(): String {
        return "ResponseResult(msgId='$msgId', status=$status, value=$value, errorMsg='$errorMsg')"
    }

}

