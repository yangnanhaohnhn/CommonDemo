package com.sinfotek.lib.common.permission

/**
 *
 * @author Y&N
 * date: 2021/10/27
 * desc:
 */
sealed class PermissionResult {
    /**
     * 权限允许
     */
    object Grant:PermissionResult()

    /**
     * 权限拒绝，且勾选了不再询问
     */
    class Deny(val permissions: Array<String>) : PermissionResult()

    /**
     * 权限拒绝
     */
    class Rationale(val permissions: Array<String>) : PermissionResult()
}
