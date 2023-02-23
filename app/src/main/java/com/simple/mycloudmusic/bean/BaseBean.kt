package com.simple.mycloudmusic.bean

/**
 * @author simple.wu
 * @description
 * @date 2023/2/23
 */
data class BaseBean<T>(
    val code: String? = null,
    val message: String? = null,
    val data: T? = null,
    val success: Boolean = false
)
