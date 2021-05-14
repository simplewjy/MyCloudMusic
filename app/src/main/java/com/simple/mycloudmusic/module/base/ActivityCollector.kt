package com.simple.mycloudmusic.module.base

import android.app.Activity

/**
 * Created by Admin
 * On 2021/5/13
 */
object ActivityCollector {
    /**
     * 初始化懒加载Activity列表
     */
    private val mActivities by lazy { ArrayList<Activity>() }

    /**
     * 添加相关activity到栈中
     */
    fun add(activity: Activity) {
        if (mActivities.contains(activity).not())
            mActivities.add(activity)
    }

    /**
     * 移除相关activity
     */
    fun remove(activity: Activity) {
        mActivities.remove(activity)
    }

    /**
     * 获取当前activity
     */
    fun getCurrentActivity(): Activity? {
        return mActivities.lastOrNull { it.isFinishing.not() }
    }

    /**
     * 结束所有的页面
     */
    fun finishAllActivity() {
        mActivities.forEach {
            if (it.isDestroyed.not()) {
                it.finish()
            }
        }
    }


}