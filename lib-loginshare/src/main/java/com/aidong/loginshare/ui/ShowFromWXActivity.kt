package com.aidong.loginshare.ui

import android.app.Activity
import android.app.AlertDialog
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageView
import com.aidong.loginshare.Constants
import com.aidong.loginshare.R

/**
 * @description
 * @author simple.wu
 * @date 2023/1/5
 */

class ShowFromWXActivity : Activity() {
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_from_wx)
        initView()
    }

    private fun initView() {
        val title = intent.getStringExtra(Constants.ShowMsgActivity.STitle)
        val message = intent.getStringExtra(Constants.ShowMsgActivity.SMessage)
        val thumbData = intent.getByteArrayExtra(Constants.ShowMsgActivity.BAThumbData)
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
        if (thumbData != null && thumbData.isNotEmpty()) {
            val thumbIv = ImageView(this)
            thumbIv.setImageBitmap(BitmapFactory.decodeByteArray(thumbData, 0, thumbData.size))
            builder.setView(thumbIv)
        }
        builder.show()
    }
}