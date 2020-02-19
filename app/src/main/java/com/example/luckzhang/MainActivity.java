package com.example.luckzhang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.mob.wrappers.UMSSDKWrapper;

import org.litepal.LitePal;

import AllService.OnlyService;
import Data_Class.User_Info;

/*在本界面进行权限获取
* 以及界面的初始化
* */

public class MainActivity extends AppCompatActivity {
    private String TAG="MainActivity.this";
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getPermissions();

        //开启服务
        Intent startServiceIntent=new Intent(MainActivity.this, OnlyService.class);
        startService(startServiceIntent);

        textView=findViewById(R.id.test);
        User_Info user_info= LitePal.findFirst(User_Info.class);
        textView.setText(user_info.getUser_fakename());
    }

    @Override
    protected void onDestroy() {
        Intent stopServiceIntent=new Intent(MainActivity.this, OnlyService.class);
        stopService(stopServiceIntent);
        super.onDestroy();
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
