package com.example.luckzhang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.camera2.CameraCharacteristics;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.litepal.LitePal;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import Data_Class.Report_item;
import Data_Class.User_Info;
import LoginAndRegister.LoginActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.os.SystemClock.sleep;

public class Check_Activity extends AppCompatActivity {
    private ImageView imageView_zheng;
    private ImageView imageView_ce;
    private Button zheng_button;
    private Button ce_button;
    private Button check_button;
    private String path_zheng;
    private String path_ce;
    private SimpleDateFormat simpleDateFormat;
    private int judge=0;
    ProgressBar progressBar;

    private Handler LoginHandler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            if(msg.what==-1){//上传失败
                Toast.makeText(Check_Activity.this, "上传失败,请重试", Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
                check_button.setEnabled(true);
            }else if(msg.what==1){//上传成功
                Toast.makeText(Check_Activity.this, "上传成功", Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
                finish();
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_);
        initialize();

    }

    @Override
    protected void onStart() {
        if(fileIsExists(path_zheng)){
            Bitmap bitmap = BitmapFactory.decodeFile(path_zheng);
            imageView_zheng.setImageBitmap(bitmap);
        }
        if(fileIsExists(path_ce)){
            Bitmap bitmap=BitmapFactory.decodeFile(path_ce);
            imageView_ce.setImageBitmap(bitmap);
        }
        super.onStart();
    }

    private void initialize(){
        progressBar=findViewById(R.id.progressBar);
        int i= LitePal.count(Report_item.class);
        path_zheng= Environment.getExternalStorageDirectory().getAbsolutePath()+"/aphysique/data/temppicture/temp_zheng_picture"+i+".jpg";
        path_ce=Environment.getExternalStorageDirectory().getAbsolutePath()+"/aphysique/data/temppicture/temp_ce_picture"+i+".jpg";
        delete(path_zheng);
        delete(path_ce);
        imageView_zheng=findViewById(R.id.imageView5);
        imageView_ce=findViewById(R.id.imageView6);
        zheng_button=findViewById(R.id.zheng_Button);
        ce_button=findViewById(R.id.ce_Button);
        check_button=findViewById(R.id.CheckButton);
        imageView_zheng.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.zheng));
        imageView_ce.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ce));
        imageView_zheng.setVisibility(View.VISIBLE);
        imageView_ce.setVisibility(View.GONE);

        imageView_zheng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(Check_Activity.this, CameraActivity.class);
                intent.putExtra("judge","0");
                startActivity(intent);
            }
        });
        imageView_ce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(Check_Activity.this, CameraActivity.class);
                intent.putExtra("judge","1");
                startActivity(intent);
            }
        });
        zheng_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView_zheng.setVisibility(View.VISIBLE);
                imageView_ce.setVisibility(View.GONE);
            }
        });
        ce_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView_ce.setVisibility(View.VISIBLE);
                imageView_zheng.setVisibility(View.GONE);
            }
        });

        check_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fileIsExists(path_zheng)){
                    if(fileIsExists(path_ce)){
                        Toast.makeText(Check_Activity.this, "正在上传，请稍后", Toast.LENGTH_SHORT).show();
                        UpUpGoGoGo();
                    }else{
                        Toast.makeText(Check_Activity.this, "请先拍摄侧面照", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(Check_Activity.this, "请先拍摄正面照", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    //判断文件是否存在
    private boolean fileIsExists(String filePath) {
        try {
            File f = new File(filePath);
            if(!f.exists()) {
                return false;
            }
        }
        catch (Exception e) {
            return false;
        }
        return true;
    }
    //删除文件
    private void delete(String delFile) {
        File file = new File(delFile);
        if (!file.exists()) {
            return;
        } else {
            if (file.isFile()){
                file.delete();
            }
        }
    }

    //上传文件
    private void UpUpGoGoGo(){
        check_button.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");// HH:mm:ss
        //获取当前时间
        final Date date = new Date(System.currentTimeMillis());
        final String dateString=simpleDateFormat.format(date);
        OkHttpClient httpClient=new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30,TimeUnit.SECONDS)
                .writeTimeout(30,TimeUnit.SECONDS)
                .build();;
        MediaType mediaType=MediaType.parse("multipart/form-data");
        MultipartBody.Builder builder=new MultipartBody.Builder().setType(MultipartBody.FORM);
        final User_Info user_info=LitePal.findFirst(User_Info.class);
        builder.addFormDataPart("phone",user_info.getUser_phonenumber());
        builder.addFormDataPart("which_number",LitePal.count(Report_item.class)+"");
        builder.addFormDataPart("time",dateString);
        final File zheng_file=new File(path_zheng);
        final File ce_file=new File(path_ce);
        RequestBody zheng_fileBody=RequestBody.Companion.create(zheng_file,mediaType);
        RequestBody ce_fileBody=RequestBody.Companion.create(ce_file,mediaType);
        builder.addFormDataPart("zheng",zheng_file.getName(),zheng_fileBody);
        builder.addFormDataPart("ce",ce_file.getName(),ce_fileBody);
        Request request=new Request.Builder()
                .url("http://123.57.235.123:8080/TheBestServe/Receive_pictureServlet")
                .post(builder.build())
                .build();
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d("Check_Activity","图片上传失败");
                judge=2;
                Message message=new Message();
                message.what=-1;
                LoginHandler.sendMessage(message);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Log.d("Check_Activity","图片上传成功");
                Report_item report_item=new Report_item();
                report_item.setItem_title("体态检测");
                report_item.setItem_time(dateString);
                report_item.setItem_name(user_info.getUser_fakename());
                report_item.setStatue_now("未完成");
                report_item.setZhengpath(zheng_file.getName());
                report_item.setCepath(ce_file.getName());
                Log.d("Check_Activity.class","111111111111111"+report_item.getZhengpath()+report_item.getCepath());
                report_item.save();
                judge=1;
                Message message=new Message();
                message.what=1;
                LoginHandler.sendMessage(message);
            }
        });



    }
}
