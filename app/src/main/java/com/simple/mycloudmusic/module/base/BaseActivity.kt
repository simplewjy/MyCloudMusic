package com.simple.mycloudmusic.module.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.BarUtils

/**
 * Created by Admin
 * On 2021/5/8
 */
abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        initView()
        initData()
        initListeners()
    }

    //获取布局id
    abstract fun getLayoutId(): Int

    //初始化view
    protected open fun initView() {
        BarUtils.setStatusBarLightMode(this, false)
    }

    //初始化数据
    protected open fun initData() {}

    //初始化监听器
    protected open fun initListeners() {}
}