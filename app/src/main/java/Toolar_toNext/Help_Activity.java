package Toolar_toNext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;


import com.example.luckzhang.R;

public class Help_Activity extends AppCompatActivity {
    private Toolbar toolbar;
    private WebView webView;
    private String URL="http://123.57.235.123:8080/TheBestServe/Help.html";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_);
        toolbar=findViewById(R.id.toolbar21);
        webView=findViewById(R.id.webviewhelp);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        webView.loadUrl(URL);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}
