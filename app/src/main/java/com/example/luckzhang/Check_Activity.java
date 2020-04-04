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
import android.graphics.Matrix;
import android.hardware.camera2.CameraCharacteristics;
import android.media.ExifInterface;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import Data_Class.Report_detail;
import Data_Class.Report_item;
import Data_Class.User_Info;
import LoginAndRegister.LoginActivity;
import Toolar_toNext.Help_Activity;
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
        AlertDialog.Builder builder=new AlertDialog.Builder(Check_Activity.this)
                .setTitle("使用须知")
                .setMessage("是否已经认真阅读使用帮助？")
                .setPositiveButton("已经阅读",new AlertDialog.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton("尚未阅读",new AlertDialog.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent =new Intent(Check_Activity.this, Help_Activity.class);
                        startActivity(intent);
                        finish();
                    }
                });
        builder.create().show();

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
        changepicture(path_zheng,path_ce);
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
                report_item.setJudge(true);
                Log.d("Check_Activity.class","111111111111111"+report_item.getZhengpath()+report_item.getCepath());

                report_item.save();

                judge=1;
                Message message=new Message();
                message.what=1;
                LoginHandler.sendMessage(message);
            }
        });
    }
    public void changepicture(String zhengP,String ceP){
        //第一步
        Bitmap picture1 = BitmapFactory.decodeFile(zhengP);
        //判断旋转角度
        ExifInterface exifInterface = null;
        try {
            exifInterface=new ExifInterface(zhengP);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(exifInterface==null){
            Toast.makeText(this, "图片旋转发生错误，请联系开发人员", Toast.LENGTH_SHORT).show();
        }
        String jiaodu = exifInterface.getAttribute(ExifInterface.TAG_ORIENTATION);
        int degree=0;
        if(jiaodu.equals("3")){
            degree=180;
        }else if(jiaodu.equals("6")){
            degree=90;
        }else if(jiaodu.equals("8")){
            degree=270;
        }
        //第二步
        Bitmap resizePicture1 = rotatePicture(picture1, degree);
        //第三步
        saveBmpToPath(resizePicture1, zhengP);


        //第一步
        Bitmap picture2 = BitmapFactory.decodeFile(ceP);
        //判断旋转角度
        ExifInterface exifInterface2 = null;
        try {
            exifInterface2=new ExifInterface(ceP);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(exifInterface2==null){
            Toast.makeText(this, "图片旋转发生错误，请联系开发人员", Toast.LENGTH_SHORT).show();
        }
        String jiaodu2 = exifInterface2 .getAttribute(ExifInterface.TAG_ORIENTATION);
        int degree2=0;
        if(jiaodu2.equals("3")){
            degree2=180;
        }else if(jiaodu2.equals("6")){
            degree2=90;
        }else if(jiaodu2.equals("8")){
            degree2=270;
        }
        //第二步
        Bitmap resizePicture2 = rotatePicture(picture2, degree2);
        //第三步
        saveBmpToPath(resizePicture2, ceP);




    }
    /**
     * @Description 旋转图片一定角度
     * @param bitmap 要旋转的图片
     * @param degree 要旋转的角度
     * @return 旋转后的图片
     */
    public Bitmap rotatePicture(final Bitmap bitmap, final int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap resizeBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizeBitmap;
    }
    /**
     * @Description 保存图片到指定路径
     * @param bitmap 要保存的图片
     * @param filePath 目标路径
     * @return 是否成功
     */
    public boolean saveBmpToPath(final Bitmap bitmap, final String filePath) {
        if (bitmap == null || filePath == null) {
            return false;
        }
        boolean result = false; //默认结果
        File file = new File(filePath);
        OutputStream outputStream = null; //文件输出流
        try {
            outputStream = new FileOutputStream(file);
            result = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream); //将图片压缩为JPEG格式写到文件输出流，100是最大的质量程度
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close(); //关闭输出流
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
}
