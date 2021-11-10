package com.example.commondemo

import com.sinfotek.component.net.RequestParam
import com.sinfotek.component.net.config.BaseNetModel
import com.sinfotek.component.net.const.UrlConst
import com.sinfotek.component.net.service.AppService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext

/**
 *
 * @author Y&N
 * date: 2021/10/26
 * desc:
 */
class MainModel : BaseNetModel() {

//    /**
//     * 登录用户
//     */
//    suspend fun login(
//        username: String,
//        password: String,
//        channelId: String,
//        callBack: AbstractResponseCallBack<LoginBean>?
//    ) {
//        val paramMap: MutableMap<String, Any> = HashMap(4)
//        paramMap[RequestParam.USERNAME] = username
//        paramMap[RequestParam.USER_PASS] = password
//        paramMap[RequestParam.PHONE_TYPE] = "Android"
//        paramMap[RequestParam.CHANNEL_ID] = channelId
//
//
////        try {
////            val login = create<AppService>()
////                .login(UrlConst.APP_LOGIN_URL, createQueryMap("login", paramMap))
////
////            callBack!!.onResponseResultCallBack(login)
////        } catch (e: Exception) {
////            e.printStackTrace()
////        }
//    }

    /**
     * 登录用户
     */
    suspend fun login(
        username: String,
        password: String,
        channelId: String
    ) = withContext(Dispatchers.IO) {
        coroutineScope {
            val paramMap: MutableMap<String, Any> = HashMap(4)
            paramMap[RequestParam.USERNAME] = username
            paramMap[RequestParam.USER_PASS] = password
            paramMap[RequestParam.PHONE_TYPE] = "Android"
            paramMap[RequestParam.CHANNEL_ID] = channelId
            //启动一个协程
            val login = async {
                create<AppService>().login(
                    UrlConst.APP_LOGIN_URL,
                    createQueryMap("login", paramMap)
                ).await()
            }
            login.await()
        }
    }
}