package com.simple.mycloudmusic

import android.app.Activity
import android.os.Bundle
import androidx.multidex.MultiDexApplication
import com.simple.mycloudmusic.module.base.ActivityCollector.add
import com.simple.mycloudmusic.module.base.ActivityCollector.remove
import me.jessyan.autosize.AutoSizeConfig
import me.jessyan.autosize.unit.Subunits

/**
 * Created by simple
 * On 2021/5/8
 */
class App : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        //初始化屏幕适配
        initAutoSize()
        //注册activity生命周期监听
        registerActivityLifecycleCallbacks(ActivityLifecycleListener())
    }


    /**
     * 初始化autoSize
     */
    private fun initAutoSize() {
        AutoSizeConfig.getInstance()
            .setCustomFragment(true)
            .setExcludeFontScale(true)
            .unitsManager
            .setSupportDP(false)
            .setSupportSP(false)
            .setSupportSubunits(Subunits.MM)
    }

    internal class ActivityLifecycleListener : ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            add(activity)
        }

        override fun onActivityStarted(activity: Activity) {}

        override fun onActivityResumed(activity: Activity) {}

        override fun onActivityPaused(activity: Activity) {}

        override fun onActivityStopped(activity: Activity) {}

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

        override fun onActivityDestroyed(activity: Activity) {
            remove(activity)
        }

    }
}