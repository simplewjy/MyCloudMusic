package com.simple.mycloudmusic

import androidx.multidex.MultiDexApplication
import me.jessyan.autosize.AutoSizeConfig
import me.jessyan.autosize.unit.Subunits

/**
 * Created by Admin
 * On 2021/5/8
 */
class App : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        //初始化屏幕适配
        initAutoSize()
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
}