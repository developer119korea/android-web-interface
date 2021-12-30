package com.dev119.android_web_interface

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val webView = findViewById<WebView>(R.id.webview);
        initializeWebView(webView);
        var url = "https://google.com";
        webView.loadUrl(url);
    }

    fun initializeWebView(webView: WebView) {
        webView.apply {
            webViewClient = WebViewClient();
            webChromeClient = WebChromeClient()
            settings.javaScriptEnabled = true
            settings.javaScriptCanOpenWindowsAutomatically = false
            settings.setSupportMultipleWindows(false)
            settings.loadsImagesAutomatically = true
            settings.useWideViewPort = true
            settings.loadWithOverviewMode = true
            settings.setSupportZoom(true)
            settings.builtInZoomControls = false
            settings.displayZoomControls = false
            settings.cacheMode = WebSettings.LOAD_NO_CACHE
            settings.domStorageEnabled = true
            settings.allowContentAccess
            settings.userAgentString = "app"
            settings.defaultTextEncodingName = "UTF-8"
            settings.databaseEnabled = true
        }
    }
}
