package com.simple.mycloudmusic.module.utils

import android.util.Log

/**
 * Description bean复制类
 * Created by Admin
 * On 2021/5/14
 */
fun Class<*>.beanCopy() {
    this.declaredFields.forEach {
        Log.i("BeanCopy======>", it.name)
    }
}