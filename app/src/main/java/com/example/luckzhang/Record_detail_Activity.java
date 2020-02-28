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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.litepal.LitePal;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import Data_Class.Report_detail;

public class Record_detail_Activity extends AppCompatActivity {

    private String strPxtr;
    private Report_detail report_detail;
    private Bitmap bitmap_zheng;
    private Bitmap bitmap_ce;
    private Bitmap copy_zheng_Bitmap;
    private Bitmap copy_ce_Bitmap;
    private Canvas canvas_zheng;
    private Canvas canvas_ce;
    private Paint paint_line;
    private Paint paint_point;
    private Paint paint_standard;
    private Paint paint_text;
    private Button button_zheng;
    private Button button_ce;
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
        button_zheng=findViewById(R.id.zheng_Button);
        button_ce=findViewById(R.id.ce_Button);

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

        button_zheng=findViewById(R.id.button_zheng);
        button_zheng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zheng_detail();
            }
        });
        button_ce=findViewById(R.id.button_ce);
        button_ce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ce_detail();
            }
        });

        paint_line =new Paint();
        paint_line.setColor(Color.BLUE);
        paint_line.setStrokeWidth(4.000f);

        paint_point =new Paint();
        paint_point.setColor(Color.BLUE);
        paint_point.setStrokeWidth(16.000f);
        paint_point.setStrokeCap(Paint.Cap.ROUND);

        paint_standard=new Paint();
        paint_standard.setColor(Color.GREEN);
        paint_standard.setStrokeWidth(4.000f);

        paint_text=new Paint();
        paint_text.setColor(Color.BLUE);
        paint_text.setTextSize(25.000f);

        copy_zheng_Bitmap =Bitmap.createBitmap(bitmap_zheng.getWidth(),bitmap_zheng.getHeight(), bitmap_zheng.getConfig());
        canvas_zheng =new Canvas(copy_zheng_Bitmap);

        copy_ce_Bitmap =Bitmap.createBitmap(bitmap_ce.getWidth(),bitmap_ce.getHeight(), bitmap_ce.getConfig());
        canvas_ce=new Canvas(copy_ce_Bitmap);
        zheng_detail();
    }

    private void zheng_detail(){
        //imageView.setImageBitmap(bitmap_zheng);
        imageView.setImageBitmap(copy_zheng_Bitmap);
        canvas_zheng.drawBitmap(bitmap_zheng,new Matrix(), paint_line);
        //两个眼睛水平
        canvas_zheng.drawLine(Float.parseFloat(report_detail.getZheng_left_eye_x()),Float.parseFloat(report_detail.getZheng_left_eye_y()),
                Float.parseFloat(report_detail.getZheng_right_eye_x()),Float.parseFloat(report_detail.getZheng_right_eye_y()), paint_line);
        //两个肩膀水平
        canvas_zheng.drawLine(Float.parseFloat(report_detail.getZheng_left_shoulder_x()),Float.parseFloat(report_detail.getZheng_left_shoulder_y()),
                Float.parseFloat(report_detail.getZheng_right_shoulder_x()),Float.parseFloat(report_detail.getZheng_right_shoulder_y()), paint_line);
        //左髋右髋水平
        canvas_zheng.drawLine(Float.parseFloat(report_detail.getZheng_left_hip_x()),Float.parseFloat(report_detail.getZheng_left_hip_y()),
                Float.parseFloat(report_detail.getZheng_right_hip_x()),Float.parseFloat(report_detail.getZheng_right_hip_y()), paint_line);
        //左耳左眼
        canvas_zheng.drawLine(Float.parseFloat(report_detail.getZheng_left_ear_x()),Float.parseFloat(report_detail.getZheng_left_ear_y()),
                Float.parseFloat(report_detail.getZheng_left_eye_x()),Float.parseFloat(report_detail.getZheng_left_eye_y()), paint_line);
        //右耳右眼
        canvas_zheng.drawLine(Float.parseFloat(report_detail.getZheng_right_ear_x()),Float.parseFloat(report_detail.getZheng_right_ear_y()),
                Float.parseFloat(report_detail.getZheng_right_eye_x()),Float.parseFloat(report_detail.getZheng_right_eye_y()), paint_line);
        //脖子和脸部
        canvas_zheng.drawLine((Float.parseFloat(report_detail.getZheng_left_eye_x())+Float.parseFloat(report_detail.getZheng_right_eye_x()))/2,
                (Float.parseFloat(report_detail.getZheng_left_eye_y())+Float.parseFloat(report_detail.getZheng_right_eye_y()))/2,
                Float.parseFloat(report_detail.getZheng_neck_x()),Float.parseFloat(report_detail.getZheng_neck_y()), paint_line);
        //脖子和两肩
        canvas_zheng.drawLine((Float.parseFloat(report_detail.getZheng_left_shoulder_x())+Float.parseFloat(report_detail.getZheng_right_shoulder_x()))/2,
                (Float.parseFloat(report_detail.getZheng_left_shoulder_y())+Float.parseFloat(report_detail.getZheng_right_shoulder_y()))/2,
                Float.parseFloat(report_detail.getZheng_neck_x()),Float.parseFloat(report_detail.getZheng_neck_y()), paint_line);
        //左肩和左肘子
        canvas_zheng.drawLine(Float.parseFloat(report_detail.getZheng_left_shoulder_x()),Float.parseFloat(report_detail.getZheng_left_shoulder_y()),
                Float.parseFloat(report_detail.getZheng_left_elbow_x()),Float.parseFloat(report_detail.getZheng_left_elbow_y()), paint_line);
        //右肩和右肘子
        canvas_zheng.drawLine(Float.parseFloat(report_detail.getZheng_right_shoulder_x()),Float.parseFloat(report_detail.getZheng_right_shoulder_y()),
                Float.parseFloat(report_detail.getZheng_right_elbow_x()),Float.parseFloat(report_detail.getZheng_right_elbow_y()), paint_line);
        //左肘子和左手腕
        canvas_zheng.drawLine(Float.parseFloat(report_detail.getZheng_left_wrist_x()),Float.parseFloat(report_detail.getZheng_left_wrist_y()),
                Float.parseFloat(report_detail.getZheng_left_elbow_x()),Float.parseFloat(report_detail.getZheng_left_elbow_y()), paint_line);
        //右肘子和右手腕
        canvas_zheng.drawLine(Float.parseFloat(report_detail.getZheng_right_wrist_x()),Float.parseFloat(report_detail.getZheng_right_wrist_y()),
                Float.parseFloat(report_detail.getZheng_right_elbow_x()),Float.parseFloat(report_detail.getZheng_right_elbow_y()), paint_line);
        //两肩中间和两髋中间
        canvas_zheng.drawLine((Float.parseFloat(report_detail.getZheng_left_shoulder_x())+Float.parseFloat(report_detail.getZheng_right_shoulder_x()))/2,
                (Float.parseFloat(report_detail.getZheng_left_shoulder_y())+Float.parseFloat(report_detail.getZheng_right_shoulder_y()))/2,
                (Float.parseFloat(report_detail.getZheng_left_hip_x())+Float.parseFloat(report_detail.getZheng_right_hip_x()))/2,
                (Float.parseFloat(report_detail.getZheng_left_hip_y())+Float.parseFloat(report_detail.getZheng_right_hip_y()))/2, paint_line);
        //左髋和左膝盖
        canvas_zheng.drawLine(Float.parseFloat(report_detail.getZheng_left_knee_x()),Float.parseFloat(report_detail.getZheng_left_knee_y()),
                Float.parseFloat(report_detail.getZheng_left_hip_x()),Float.parseFloat(report_detail.getZheng_left_hip_y()), paint_line);
        //左膝盖和左脚踝
        canvas_zheng.drawLine(Float.parseFloat(report_detail.getZheng_left_knee_x()),Float.parseFloat(report_detail.getZheng_left_knee_y()),
                Float.parseFloat(report_detail.getZheng_left_ankle_x()),Float.parseFloat(report_detail.getZheng_left_ankle_y()), paint_line);
        //右髋和右膝盖
        canvas_zheng.drawLine(Float.parseFloat(report_detail.getZheng_right_knee_x()),Float.parseFloat(report_detail.getZheng_right_knee_y()),
                Float.parseFloat(report_detail.getZheng_right_hip_x()),Float.parseFloat(report_detail.getZheng_right_hip_y()), paint_line);
        //右膝盖和右脚踝
        canvas_zheng.drawLine(Float.parseFloat(report_detail.getZheng_right_knee_x()),Float.parseFloat(report_detail.getZheng_right_knee_y()),
                Float.parseFloat(report_detail.getZheng_right_ankle_x()),Float.parseFloat(report_detail.getZheng_right_ankle_y()), paint_line);

        //下面是标点
        canvas_zheng.drawPoint(Float.parseFloat(report_detail.getZheng_left_eye_x()),Float.parseFloat(report_detail.getZheng_left_eye_y()),paint_point);
        canvas_zheng.drawText("左眼",Float.parseFloat(report_detail.getZheng_left_eye_x())+5.000f,Float.parseFloat(report_detail.getZheng_left_eye_y()),paint_text);
        canvas_zheng.drawPoint(Float.parseFloat(report_detail.getZheng_right_eye_x()),Float.parseFloat(report_detail.getZheng_right_eye_y()),paint_point);
        canvas_zheng.drawText("右眼",Float.parseFloat(report_detail.getZheng_right_eye_x())-55.000f,Float.parseFloat(report_detail.getZheng_right_eye_y()),paint_text);
        canvas_zheng.drawPoint(Float.parseFloat(report_detail.getZheng_left_ear_x()),Float.parseFloat(report_detail.getZheng_left_ear_y()),paint_point);
        canvas_zheng.drawText("左耳",Float.parseFloat(report_detail.getZheng_left_ear_x())+5.000f,Float.parseFloat(report_detail.getZheng_left_ear_y()),paint_text);
        canvas_zheng.drawPoint(Float.parseFloat(report_detail.getZheng_right_ear_x()),Float.parseFloat(report_detail.getZheng_right_ear_y()),paint_point);
        canvas_zheng.drawText("右耳",Float.parseFloat(report_detail.getZheng_right_ear_x())-55.000f,Float.parseFloat(report_detail.getZheng_right_ear_y()),paint_text);
        canvas_zheng.drawPoint(Float.parseFloat(report_detail.getZheng_neck_x()),Float.parseFloat(report_detail.getZheng_neck_y()),paint_point);
        canvas_zheng.drawText("颈部",Float.parseFloat(report_detail.getZheng_neck_x())+5.000f,Float.parseFloat(report_detail.getZheng_neck_y()),paint_text);
        canvas_zheng.drawPoint(Float.parseFloat(report_detail.getZheng_left_shoulder_x()),Float.parseFloat(report_detail.getZheng_left_shoulder_y()),paint_point);
        canvas_zheng.drawText("左肩",Float.parseFloat(report_detail.getZheng_left_shoulder_x())+5.000f,Float.parseFloat(report_detail.getZheng_left_shoulder_y()),paint_text);
        canvas_zheng.drawPoint(Float.parseFloat(report_detail.getZheng_right_shoulder_x()),Float.parseFloat(report_detail.getZheng_right_shoulder_y()),paint_point);
        canvas_zheng.drawText("右肩",Float.parseFloat(report_detail.getZheng_right_shoulder_x())-55.000f,Float.parseFloat(report_detail.getZheng_right_shoulder_y()),paint_text);
        canvas_zheng.drawPoint(Float.parseFloat(report_detail.getZheng_left_elbow_x()),Float.parseFloat(report_detail.getZheng_left_elbow_y()),paint_point);
        canvas_zheng.drawText("左肘",Float.parseFloat(report_detail.getZheng_left_elbow_x())+5.000f,Float.parseFloat(report_detail.getZheng_left_elbow_y()),paint_text);
        canvas_zheng.drawPoint(Float.parseFloat(report_detail.getZheng_right_elbow_x()),Float.parseFloat(report_detail.getZheng_right_elbow_y()),paint_point);
        canvas_zheng.drawText("右肘",Float.parseFloat(report_detail.getZheng_right_elbow_x())-55.000f,Float.parseFloat(report_detail.getZheng_right_elbow_y()),paint_text);
        canvas_zheng.drawPoint(Float.parseFloat(report_detail.getZheng_left_wrist_x()),Float.parseFloat(report_detail.getZheng_left_wrist_y()),paint_point);
        canvas_zheng.drawText("左腕",Float.parseFloat(report_detail.getZheng_left_wrist_x())+5.000f,Float.parseFloat(report_detail.getZheng_left_wrist_y()),paint_text);
        canvas_zheng.drawPoint(Float.parseFloat(report_detail.getZheng_right_wrist_x()),Float.parseFloat(report_detail.getZheng_right_wrist_y()),paint_point);
        canvas_zheng.drawText("右腕",Float.parseFloat(report_detail.getZheng_right_wrist_x())-55.000f,Float.parseFloat(report_detail.getZheng_right_wrist_y()),paint_text);
        canvas_zheng.drawPoint(Float.parseFloat(report_detail.getZheng_left_hip_x()),Float.parseFloat(report_detail.getZheng_left_hip_y()),paint_point);
        canvas_zheng.drawText("左髋",Float.parseFloat(report_detail.getZheng_left_hip_x())+5.000f,Float.parseFloat(report_detail.getZheng_left_hip_y()),paint_text);
        canvas_zheng.drawPoint(Float.parseFloat(report_detail.getZheng_right_hip_x()),Float.parseFloat(report_detail.getZheng_right_hip_y()),paint_point);
        canvas_zheng.drawText("右髋",Float.parseFloat(report_detail.getZheng_right_hip_x())-55.000f,Float.parseFloat(report_detail.getZheng_right_hip_y()),paint_text);
        canvas_zheng.drawPoint(Float.parseFloat(report_detail.getZheng_left_knee_x()),Float.parseFloat(report_detail.getZheng_left_knee_y()),paint_point);
        canvas_zheng.drawText("左膝",Float.parseFloat(report_detail.getZheng_left_knee_x())+5.000f,Float.parseFloat(report_detail.getZheng_left_knee_y()),paint_text);
        canvas_zheng.drawPoint(Float.parseFloat(report_detail.getZheng_right_knee_x()),Float.parseFloat(report_detail.getZheng_right_knee_y()),paint_point);
        canvas_zheng.drawText("右膝",Float.parseFloat(report_detail.getZheng_right_knee_x())-55.000f,Float.parseFloat(report_detail.getZheng_right_knee_y()),paint_text);
        canvas_zheng.drawPoint(Float.parseFloat(report_detail.getZheng_left_ankle_x()),Float.parseFloat(report_detail.getZheng_left_ankle_y()),paint_point);
        canvas_zheng.drawText("左踝",Float.parseFloat(report_detail.getZheng_left_ankle_x())+5.000f,Float.parseFloat(report_detail.getZheng_left_ankle_y()),paint_text);
        canvas_zheng.drawPoint(Float.parseFloat(report_detail.getZheng_right_ankle_x()),Float.parseFloat(report_detail.getZheng_right_ankle_y()),paint_point);
        canvas_zheng.drawText("右踝",Float.parseFloat(report_detail.getZheng_right_ankle_x())-55.000f,Float.parseFloat(report_detail.getZheng_right_ankle_y()),paint_text);
    }

    private void ce_detail(){
        imageView.setImageBitmap(copy_ce_Bitmap);
        canvas_ce.drawBitmap(bitmap_ce,new Matrix(), paint_line);
        //右耳脖子
        canvas_ce.drawLine(Float.parseFloat(report_detail.getCe_right_ear_x()),Float.parseFloat(report_detail.getCe_right_ear_y()),
                Float.parseFloat(report_detail.getCe_neck_x()),Float.parseFloat(report_detail.getCe_neck_y()),paint_line);
        //右肩和脖子
        canvas_ce.drawLine(Float.parseFloat(report_detail.getCe_right_shoulder_x()),Float.parseFloat(report_detail.getCe_right_shoulder_y()),
                Float.parseFloat(report_detail.getCe_neck_x()),Float.parseFloat(report_detail.getCe_neck_y()), paint_line);
        //右肩和右髋
        canvas_ce.drawLine(Float.parseFloat(report_detail.getCe_right_shoulder_x()),Float.parseFloat(report_detail.getCe_right_shoulder_y()),
                Float.parseFloat(report_detail.getCe_right_hip_x()),Float.parseFloat(report_detail.getCe_right_hip_y()), paint_line);
        //右髋和右膝盖
        canvas_ce.drawLine(Float.parseFloat(report_detail.getCe_right_knee_x()),Float.parseFloat(report_detail.getCe_right_knee_y()),
                Float.parseFloat(report_detail.getCe_right_hip_x()),Float.parseFloat(report_detail.getCe_right_hip_y()), paint_line);
        //右膝盖和右脚踝
        canvas_ce.drawLine(Float.parseFloat(report_detail.getCe_right_knee_x()),Float.parseFloat(report_detail.getCe_right_knee_y()),
                Float.parseFloat(report_detail.getCe_right_ankle_x()),Float.parseFloat(report_detail.getCe_right_ankle_y()), paint_line);

        canvas_ce.drawPoint(Float.parseFloat(report_detail.getCe_right_ear_x()),Float.parseFloat(report_detail.getCe_right_ear_y()),paint_point);
        canvas_ce.drawText("右耳",Float.parseFloat(report_detail.getCe_right_ear_x())+5.000f,Float.parseFloat(report_detail.getCe_right_ear_y()),paint_text);
        canvas_ce.drawPoint(Float.parseFloat(report_detail.getCe_neck_x()),Float.parseFloat(report_detail.getCe_neck_y()),paint_point);
        canvas_ce.drawText("颈部",Float.parseFloat(report_detail.getCe_neck_x())+5.000f,Float.parseFloat(report_detail.getCe_neck_y()),paint_text);
        canvas_ce.drawPoint(Float.parseFloat(report_detail.getCe_right_shoulder_x()),Float.parseFloat(report_detail.getCe_right_shoulder_y()),paint_point);
        canvas_ce.drawText("右肩",Float.parseFloat(report_detail.getCe_right_shoulder_x())+5.000f,Float.parseFloat(report_detail.getCe_right_shoulder_y()),paint_text);
        canvas_ce.drawPoint(Float.parseFloat(report_detail.getCe_right_hip_x()),Float.parseFloat(report_detail.getCe_right_hip_y()),paint_point);
        canvas_ce.drawText("右髋",Float.parseFloat(report_detail.getCe_right_hip_x())+5.000f,Float.parseFloat(report_detail.getCe_right_hip_y()),paint_text);
        canvas_ce.drawPoint(Float.parseFloat(report_detail.getCe_right_knee_x()),Float.parseFloat(report_detail.getCe_right_knee_y()),paint_point);
        canvas_ce.drawText("右膝",Float.parseFloat(report_detail.getCe_right_knee_x())+5.000f,Float.parseFloat(report_detail.getCe_right_knee_y()),paint_text);
        canvas_ce.drawPoint(Float.parseFloat(report_detail.getCe_right_ankle_x()),Float.parseFloat(report_detail.getCe_right_ankle_y()),paint_point);
        canvas_ce.drawText("右踝",Float.parseFloat(report_detail.getCe_right_ankle_x())+5.000f,Float.parseFloat(report_detail.getCe_right_ankle_y()),paint_text);

    }

}
