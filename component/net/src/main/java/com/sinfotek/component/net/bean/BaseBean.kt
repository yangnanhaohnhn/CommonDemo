package com.sinfotek.component.net.bean

import java.io.Serializable

/**
 *
 * @author Y&N
 * date: 2021/10/28
 * desc:
 */
open class BaseBean : Serializable {
    var code: String = ""
    var msg: String = ""
    override fun toString(): String {
        return "BaseBean(code='$code', msg='$msg')"
    }


}
