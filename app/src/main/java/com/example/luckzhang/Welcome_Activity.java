package com.example.luckzhang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import org.litepal.LitePal;

import java.io.File;

import AllService.OnlyService;
import Data_Class.User_Info;
import LoginAndRegister.LoginActivity;
import LoginAndRegister.RegisterActivity;

/*欢迎界面
* 这是用户看到的第一个界面
* 可以放一些图片或者其他的什么的东西，预计3秒中
* 如果已经登录，送到MainActivty，
* 未登录送到login
*
* renew：写完了，把欢迎界面图片的下载放到了服务中的一个子线程里
* */

public class Welcome_Activity extends AppCompatActivity {

    Button button;
    ImageView imageView1,imageView2;
    private static final int UPDATE_BUTTON_TEXT=1;
    private MyThread myThread;
    private boolean judge=true;
    final private String TAG="Welcome_Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Log.d("Activity_process_report","welcome activity is work");
        initialize();
    }

    private void initialize(){
        button=findViewById(R.id.button);
        imageView1=findViewById(R.id.imageView);
        imageView2=findViewById(R.id.imageView2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leaveThis();
            }
        });

        //欢迎界面的图片
        //fileIsExists();
        String url = "http://123.57.235.123:8080/img/WelcomeImg.jpg";
        Glide.with(this).load(url).into(imageView1);
        String url1 = "http://123.57.235.123:8080/img/welcome_titaijiance.png";
        Glide.with(this).load(url1).into(imageView2);

        Log.d("Activity_process_report","welcome activity initialize normally");

        //开子线程的位置
        myThread=new MyThread();
        new Thread(myThread).start();
        /*advertisementDownLoad=new AdvertisementDownLoad();
        new Thread(advertisementDownLoad).start();*/
    }

    //判断文件是否存在,如果存在把欢迎界面的图片也换了
    private void fileIsExists() {
        String welcome_img_path=Environment.getExternalStorageDirectory().getAbsolutePath() + "/aphysique/data/resources/WelcomeImg.png";
        boolean judgefile=true;
        try {
            File f = new File(welcome_img_path);
            if(!f.exists()) {
                Log.d("Welcome_Activity","11111111没有初始化照片");
                judgefile= false;
            }
        }
        catch (Exception e) {
            Log.d("Welcome_Activity","11111111没有初始化照片!!!!!!!!");
            judgefile= false;
        }
        if(judgefile){
            Log.d("Welcome_Activity","11111111有初始化照片");
            Bitmap bitmap= BitmapFactory.decodeFile(welcome_img_path);
            imageView1.setImageBitmap(bitmap);
        }
    }

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            if(msg.what==1){
                button.setText("跳过(1s)");
            }else if(msg.what==2){
                button.setText("跳过(2s)");
            }else if(msg.what==0&&judge){
                leaveThis();
            }
        }
    };

    //3秒倒数子线程
    class MyThread implements Runnable{
        @Override
        public void run() {
            for(int i=3;i>=0;i--){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Message message=new Message();
                message.what=i;
                handler.sendMessage(message);
            }
        }
    }

    //准备离开此页面
    private void leaveThis(){
        judge=false;
        //在这里判断是否登录，未登录直接送到login界面
        int user_info= LitePal.count(User_Info.class);
        Log.d("Activity_process_report","welcome activity is ready to INTENT");
        final Intent intent=new Intent();
        if(user_info==0){//尚未登录
            Log.d("Activity_process_report","welcome activity CHECK: user don't login ,ready to INTENT (LoginActivity)");
            AlertDialog.Builder builder=new AlertDialog.Builder(Welcome_Activity.this);
            builder.setTitle("欢迎进入");
            builder.setMessage("请您先登录账号");
            builder.setCancelable(false);
            builder.setPositiveButton("好的", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    intent.setClass(Welcome_Activity.this, LoginActivity.class);
                    startActivity(intent);
                    dolittle();
                }
            });
            builder.create().show();
        }else if(user_info==1){//已经登录
            Log.d("Activity_process_report","welcome activity CHECK: user login ,ready to INTENT (MainActivity)");
            intent.setClass(Welcome_Activity.this, MainActivity.class);
            startActivity(intent);
            this.finish();
        }else{//数据异常
            Log.d("Activity_process_report","welcome activity CHECK: ERROR Welcome_Activity line:153");
            return;
        }
    }
    private void dolittle(){
        this.finish();
    }
}
