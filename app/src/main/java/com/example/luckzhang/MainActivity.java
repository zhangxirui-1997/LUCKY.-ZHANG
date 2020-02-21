package com.example.luckzhang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.litepal.LitePal;

import java.util.List;

import AllService.OnlyService;
import Data_Class.Report_item;
import My_ViewPager.MyPageAdapter;
import SomeTools.MyItemAdapter;

/*在本界面进行权限获取
* 以及界面的初始化
* */

public class MainActivity extends AppCompatActivity {
    private String TAG="MainActivity.this";
    private Toolbar toolbar;
    private ViewPager viewPager;
    private Button button_left;
    private Button button_right;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //申请权限模块
        getPermissions();
        //先开启服务
        Intent startServiceIntent=new Intent(MainActivity.this, OnlyService.class);
        startService(startServiceIntent);

        //再初始化本地
        //初始化变量,viewpager也放进去了
        init_variable();

    }
    @Override
    protected void onDestroy() {
        Intent stopServiceIntent=new Intent(MainActivity.this, OnlyService.class);
        stopService(stopServiceIntent);
        super.onDestroy();
    }

    private void init_variable(){
        toolbar=findViewById(R.id.toolbar);
        viewPager = findViewById(R.id.viewpager);
        button_left=findViewById(R.id.button4);
        button_right=findViewById(R.id.button5);
        viewpager_initialize();
        list_initialize();
        button_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0);
            }
        });
        button_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1);
            }
        });
    }

    private void viewpager_initialize(){
        viewPager.setAdapter(new MyPageAdapter(this));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position==0){
                    button_left.setBackground(getResources().getDrawable(R.drawable.button_mainviewpager_1_circle_shape,null));
                    button_right.setBackground(getResources().getDrawable(R.drawable.button_mainviewpager_2_circle_shape,null));
                }else if(position==1){
                    button_left.setBackground(getResources().getDrawable(R.drawable.button_mainviewpager_2_circle_shape,null));
                    button_right.setBackground(getResources().getDrawable(R.drawable.button_mainviewpager_1_circle_shape,null));                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void list_initialize(){

    }















    //获取权限，下面是回调
    private void getPermissions(){
        String[] permissions={Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.READ_PHONE_STATE};
        for(int i=0;i<=3;i++){
            if(ContextCompat.checkSelfPermission(MainActivity.this,permissions[i])
                    != PackageManager.PERMISSION_GRANTED){
                Log.d(TAG,"11111111111111:"+permissions[i]);

                ActivityCompat.requestPermissions(MainActivity.this,
                        permissions,1);
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull final String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==1){
            for(int ii=0;ii<=3;ii++){
                if(grantResults.length>0&&grantResults[ii]!=PackageManager.PERMISSION_GRANTED){
                    AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("提示");
                    switch (ii){
                        case 0:
                            builder.setMessage("如果不授予相机权限，我们将无法进行体态测试");
                            break;
                        case 1:
                            builder.setMessage("如果不授予读写权限，我们将无法处理数据");
                            break;
                        case 2:
                            builder.setMessage("如果不授予定位权限，我们将无法进行数据统计");
                            break;
                        case 3:
                            builder.setMessage("如果不授予访问手机状态权限，我们将无法向您发送验证码");

                    }
                    final int finalIi = ii;
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{permissions[finalIi]},2);
                        }
                    });
                    builder.setCancelable(false);
                    builder.create().show();
                }
            }
        }
    }

}
