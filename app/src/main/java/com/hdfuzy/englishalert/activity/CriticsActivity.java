package com.hdfuzy.englishalert.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.hdfuzy.englishalert.R;

public class CriticsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_critics);

        ImageView backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CriticsActivity.super.onBackPressed();
            }
        });

        WebView webView = findViewById(R.id.webView);
        String url = "https://docs.google.com/forms/d/e/1FAIpQLSfnwYe3dtRw3ifhyFlf3XN7XP_e9kMyFPBENjcJg4lJ51pEUw/viewform";
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url); // load a web page in a web view

    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}