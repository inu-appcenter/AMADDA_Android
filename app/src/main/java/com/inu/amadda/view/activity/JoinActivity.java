package com.inu.amadda.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.inu.amadda.R;

public class JoinActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        WebView webView = findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient());

        webView.getSettings().setUseWideViewPort(true);

        webView.loadUrl("http://117.16.191.242:7003/signUp");
    }
}