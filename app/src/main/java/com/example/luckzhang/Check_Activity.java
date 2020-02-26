package com.example.luckzhang;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.camera2.CameraCharacteristics;
import android.os.Bundle;
import android.os.Environment;
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

import Data_Class.Report_item;
import Data_Class.User_Info;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Check_Activity extends AppCompatActivity {
    private ImageView imageView_zheng;
    private ImageView imageView_ce;
    private Button zheng_button;
    private Button ce_button;
    private Button check_button;
    private String path_zheng;
    private String path_ce;
    private SimpleDateFormat simpleDateFormat;
    private boolean judge=false;
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
        ProgressBar progressBar=findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");// HH:mm:ss
        //获取当前时间
        final Date date = new Date(System.currentTimeMillis());

        OkHttpClient httpClient=new OkHttpClient();
        MediaType mediaType=MediaType.parse("multipart/form-data");
        MultipartBody.Builder builder=new MultipartBody.Builder().setType(MultipartBody.FORM);
        final User_Info user_info=LitePal.findFirst(User_Info.class);
        builder.addFormDataPart("phone",user_info.getUser_phonenumber());
        builder.addFormDataPart("which_number",LitePal.count(Report_item.class)+"");
        builder.addFormDataPart("time",simpleDateFormat.format(date));
        final File zheng_file=new File(path_zheng);
        final File ce_file=new File(path_ce);
        RequestBody zheng_fileBody=RequestBody.Companion.create(zheng_file,mediaType);
        RequestBody ce_fileBody=RequestBody.Companion.create(ce_file,mediaType);
        builder.addFormDataPart("zheng",zheng_file.getName(),zheng_fileBody);
        builder.addFormDataPart("ce",ce_file.getName(),ce_fileBody);
        Request request=new Request.Builder()
                .url("http://192.168.43.96:8085/TheBestServe/Receive_pictureServlet")
                .post(builder.build())
                .build();
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d("Check_Activity","图片上传失败");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Log.d("Check_Activity","图片上传成功");
                Report_item report_item=new Report_item();
                report_item.setItem_title("体态检测");
                report_item.setItem_time(simpleDateFormat.format(date));
                report_item.setItem_name(user_info.getUser_fakename());
                report_item.setStatue_now("未完成");
                report_item.setZhengpath(zheng_file.getName());
                report_item.setCepath(ce_file.getName());
                Log.d("Check_Activity.class","111111111111111"+report_item.getZhengpath()+report_item.getCepath());
                report_item.save();
                judge=true;

            }
        });


    }
}
