package com.example.luckzhang;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ImageView;

import org.litepal.LitePal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import Data_Class.Report_detail;

public class Record_detail_Activity extends AppCompatActivity {

    private String strPxtr;
    private Report_detail report_detail;
    private Bitmap bitmap_zheng;
    private Bitmap bitmap_ce;
    private Bitmap copyBitmap;
    private Canvas canvas;
    private Paint paint;
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_detail);
        try {
            initialize();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
    private void initialize() throws FileNotFoundException {
        strPxtr=getIntent().getStringExtra("id");
        List<Report_detail> report_details= LitePal.findAll(Report_detail.class);
        for(Report_detail r:report_details){
            if(r.getReport_time().equals(strPxtr)){
                report_detail=r;
                break;
            }
        }
        FileInputStream fs_zheng=new FileInputStream(Environment.getExternalStorageDirectory().getAbsolutePath()+"/aphysique/data/temppicture/"+report_detail.getPathzheng());
        FileInputStream fs_ce=new FileInputStream(Environment.getExternalStorageDirectory().getAbsolutePath()+"/aphysique/data/temppicture/"+report_detail.getPahtce());
        bitmap_zheng=BitmapFactory.decodeStream(fs_zheng);
        bitmap_ce=BitmapFactory.decodeStream(fs_ce);
        imageView=findViewById(R.id.imageView7);
        imageView.setImageBitmap(bitmap_zheng);
        copyBitmap=Bitmap.createBitmap(bitmap_zheng.getWidth(),bitmap_zheng.getHeight(), bitmap_zheng.getConfig());
        paint=new Paint();
        paint.setColor(Color.YELLOW);
        paint.setStrokeWidth(8.000f);
        canvas=new Canvas(copyBitmap);
        imageView.setImageBitmap(copyBitmap);
        canvas.drawBitmap(bitmap_zheng,new Matrix(),paint);
        //两个眼睛水平
        canvas.drawLine(Float.parseFloat(report_detail.getZheng_left_eye_x()),Float.parseFloat(report_detail.getZheng_left_eye_y()),
                Float.parseFloat(report_detail.getZheng_right_eye_x()),Float.parseFloat(report_detail.getZheng_right_eye_y()),paint);
        //两个肩膀水平
        canvas.drawLine(Float.parseFloat(report_detail.getZheng_left_shoulder_x()),Float.parseFloat(report_detail.getZheng_left_shoulder_y()),
                Float.parseFloat(report_detail.getZheng_right_shoulder_x()),Float.parseFloat(report_detail.getZheng_right_shoulder_y()),paint);
        //左髋右髋水平
        canvas.drawLine(Float.parseFloat(report_detail.getZheng_left_hip_x()),Float.parseFloat(report_detail.getZheng_left_hip_y()),
                Float.parseFloat(report_detail.getZheng_right_hip_x()),Float.parseFloat(report_detail.getZheng_right_hip_y()),paint);
        //左耳左眼
        canvas.drawLine(Float.parseFloat(report_detail.getZheng_left_ear_x()),Float.parseFloat(report_detail.getZheng_left_ear_y()),
                Float.parseFloat(report_detail.getZheng_left_eye_x()),Float.parseFloat(report_detail.getZheng_left_eye_y()),paint);
        //右耳右眼
        canvas.drawLine(Float.parseFloat(report_detail.getZheng_right_ear_x()),Float.parseFloat(report_detail.getZheng_right_ear_y()),
                Float.parseFloat(report_detail.getZheng_right_eye_x()),Float.parseFloat(report_detail.getZheng_right_eye_y()),paint);
        //脖子和脸部
        canvas.drawLine((Float.parseFloat(report_detail.getZheng_left_eye_x())+Float.parseFloat(report_detail.getZheng_right_eye_x()))/2,
                (Float.parseFloat(report_detail.getZheng_left_eye_y())+Float.parseFloat(report_detail.getZheng_right_eye_y()))/2,
                Float.parseFloat(report_detail.getZheng_neck_x()),Float.parseFloat(report_detail.getZheng_neck_y()),paint);
        //脖子和两肩
        canvas.drawLine((Float.parseFloat(report_detail.getZheng_left_shoulder_x())+Float.parseFloat(report_detail.getZheng_right_shoulder_x()))/2,
                (Float.parseFloat(report_detail.getZheng_left_shoulder_y())+Float.parseFloat(report_detail.getZheng_right_shoulder_y()))/2,
                Float.parseFloat(report_detail.getZheng_neck_x()),Float.parseFloat(report_detail.getZheng_neck_y()),paint);
        //两肩中间和两髋中间
        canvas.drawLine((Float.parseFloat(report_detail.getZheng_left_shoulder_x())+Float.parseFloat(report_detail.getZheng_right_shoulder_x()))/2,
                (Float.parseFloat(report_detail.getZheng_left_shoulder_y())+Float.parseFloat(report_detail.getZheng_right_shoulder_y()))/2,
                (Float.parseFloat(report_detail.getZheng_left_hip_x())+Float.parseFloat(report_detail.getZheng_right_hip_x()))/2,
                (Float.parseFloat(report_detail.getZheng_left_hip_y())+Float.parseFloat(report_detail.getZheng_right_hip_y()))/2,paint);

        //左髋和左膝盖
        canvas.drawLine(Float.parseFloat(report_detail.getZheng_left_knee_x()),Float.parseFloat(report_detail.getZheng_left_knee_y()),
                Float.parseFloat(report_detail.getZheng_left_hip_x()),Float.parseFloat(report_detail.getZheng_left_hip_y()),paint);
        //左膝盖和左脚踝
        canvas.drawLine(Float.parseFloat(report_detail.getZheng_left_knee_x()),Float.parseFloat(report_detail.getZheng_left_knee_y()),
                Float.parseFloat(report_detail.getZheng_left_ankle_x()),Float.parseFloat(report_detail.getZheng_left_ankle_y()),paint);

        //右髋和右膝盖
        canvas.drawLine(Float.parseFloat(report_detail.getZheng_right_knee_x()),Float.parseFloat(report_detail.getZheng_right_knee_y()),
                Float.parseFloat(report_detail.getZheng_right_hip_x()),Float.parseFloat(report_detail.getZheng_right_hip_y()),paint);
        //右膝盖和右脚踝
        canvas.drawLine(Float.parseFloat(report_detail.getZheng_right_knee_x()),Float.parseFloat(report_detail.getZheng_right_knee_y()),
                Float.parseFloat(report_detail.getZheng_right_ankle_x()),Float.parseFloat(report_detail.getZheng_right_ankle_y()),paint);



    }

}
