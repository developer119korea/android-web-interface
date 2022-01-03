package com.dev119.android_web_interface

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.*
import org.json.JSONObject
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    val TAG = "MainActivity";

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
        Log.d(TAG, "callbackID : $callbackID, action : $action, actionArgs : $actionArgs");

        when(action) {
            "showMenu" -> {
                showMenu(actionArgs);
                reseponseToWeb(callbackID, true);
            };
            "scanQRcode" -> {
                val qrcodeJson = scanQRcode(actionArgs);
                reseponseToWeb(callbackID, true, qrcodeJson.toString());
            };
        }
    }

    fun reseponseToWeb(callbackID: String, isSuccess: Boolean, callbackArgs: String = "", keppCallback: Boolean = false) {
        val webView = findViewById<WebView>(R.id.webview);
        webView.post { webView.loadUrl("javascript:onResponseFromNative($callbackID,$isSuccess,$callbackArgs,$keppCallback)"); }
    }

    fun showMenu(actionArgs: String) {
        try {
            val command = JSONObject(actionArgs);
            val title = command.getString("titles");
            Log.d(TAG, "Show Menu Title : $title");
        }
        catch(e: Exception) {
            Log.d(TAG,"Show Menu Args Parsing Error : $e");
        }
    }

    fun scanQRcode(actionArgs: String = ""):String {
        val jsonString:String = "{\"round\":10, \"numbers\": [1,2,3,4,5,6]}"
        return jsonString;
    }
}
