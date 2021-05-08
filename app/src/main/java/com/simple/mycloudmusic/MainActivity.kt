package com.simple.mycloudmusic

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import com.simple.mycloudmusic.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            wb_test.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    return false
                }
            }
            wb_test.webChromeClient = WebChromeClient()
        }
        val webSettings = wb_test.settings
        webSettings.javaScriptEnabled = true
        wb_test.loadUrl("https://dioa.gitee.io/mobile-personal-page/#/introduce")
    }


    private fun test() {

    }
}