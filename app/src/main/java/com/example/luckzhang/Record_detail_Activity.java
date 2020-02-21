package com.example.luckzhang;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class Record_detail_Activity extends AppCompatActivity {
    private int fake_idnumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_detail);
        String stringExtra=getIntent().getStringExtra("id");
        TextView textView=findViewById(R.id.textView8);
        textView.setText(stringExtra);
    }
}
