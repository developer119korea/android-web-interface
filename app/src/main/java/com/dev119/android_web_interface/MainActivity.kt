package com.dev119.android_web_interface

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.*
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        val webView = findViewById<WebView>(R.id.webview);
        initializeWebView(webView);
        var url = "file:///android_asset/sample.html";
        webView.loadUrl(url);
    }

    fun initializeWebView(webView: WebView) {
        webView.apply {
            webViewClient = WebViewClient();
            webChromeClient = WebChromeClient();
            settings.javaScriptEnabled = true;
            settings.javaScriptCanOpenWindowsAutomatically = false;
            settings.setSupportMultipleWindows(false);
            settings.loadsImagesAutomatically = true;
            settings.useWideViewPort = true;
            settings.loadWithOverviewMode = true;
            settings.setSupportZoom(true);
            settings.builtInZoomControls = false;
            settings.displayZoomControls = false;
            settings.cacheMode = WebSettings.LOAD_NO_CACHE;
            settings.domStorageEnabled = true;
            settings.allowContentAccess = true;
            settings.userAgentString = "app";
            settings.defaultTextEncodingName = "UTF-8";
            settings.databaseEnabled = true;
        }
        webView.addJavascriptInterface(this, "AndroidInterface");
    }

    @JavascriptInterface
    fun requestFromWeb(callbackID: String, action: String, actionArgs: String) {
        Log.d("PostAction", "callbackID : " + callbackID + ", action : " + action + "actionArgs : " + actionArgs);
        reseponseToWeb(callbackID, true);
    }

    fun reseponseToWeb(callbackID: String, isSuccess: Boolean) {
        val webView = findViewById<WebView>(R.id.webview);
        webView.post { webView.loadUrl("javascript:onResponseFromNative(" + callbackID + "," + isSuccess + ")"); }
    }
}
