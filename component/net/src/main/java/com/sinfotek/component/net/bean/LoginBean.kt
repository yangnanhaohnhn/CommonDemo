package com.sinfotek.component.net.bean

import java.io.Serializable

/**
 *
 * @author Y&N
 * date: 2021/8/12
 * desc:
 */
data class LoginBean(
    val data: DataBean?
) : BaseBean() {
    data class DataBean(
        val deptName: String,
        val email: String,
        val id: String,
        val isPushMsg: String,
        val jobType: String,
        val jobTypeName: String,
        val mobile: String,
        val name: String,
        val nick: String,
        val phone: String,
        val photo: String,
        val userId: String,
        //登录名
        val userName: String,
        val userType: String,
        val userTypeName: String
    ) : Serializable
}