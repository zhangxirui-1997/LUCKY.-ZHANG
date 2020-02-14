package com.example.luckzhang;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

/*欢迎界面
* 这是用户看到的第一个界面
* 可以放一些图片或者其他的什么的东西，预计2秒中
* */

public class Welcome_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }
}
