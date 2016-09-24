package com.example.myapplication_musicplayer.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.myapplication_musicplayer.R;

/**
 * Created by wansh on 2016/9/21.
 * webView
 */
public class MainActivity_Web extends Activity {
    private WebView mainWebWebView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivity_web);
        mainWebWebView = (WebView) findViewById(R.id.main_web_webView);
        mainWebWebView.getSettings().setJavaScriptEnabled(true);
        mainWebWebView.setWebViewClient(new WebViewClient());
        mainWebWebView.loadUrl(getIntent().getStringExtra("url"));

    }
}
