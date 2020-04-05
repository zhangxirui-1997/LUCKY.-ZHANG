package com.example.luckzhang;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;

public class WebActivity extends AppCompatActivity {
    private String url="http://123.57.235.123:8080/TheBestServe/";
    private String string;
    private WebView webView;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        string=getIntent().getStringExtra("key");
        toolbar=findViewById(R.id.toolbar51);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        webView=findViewById(R.id.wb);
        webView.loadUrl(url+string+".html");
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
