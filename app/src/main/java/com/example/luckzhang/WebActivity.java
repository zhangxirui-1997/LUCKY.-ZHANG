package com.example.luckzhang;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

public class WebActivity extends AppCompatActivity {
    private String url="http://123.57.235.123:8080/TheBestServe/";
    private String string;
    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        string=getIntent().getStringExtra("key");
        webView=findViewById(R.id.wb);
        webView.loadUrl(url+string+".html");
    }
}
