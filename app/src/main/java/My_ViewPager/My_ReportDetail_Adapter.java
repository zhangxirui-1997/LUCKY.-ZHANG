package My_ViewPager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import com.example.luckzhang.R;

import org.litepal.LitePal;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import Data_Class.Report_detail;

public class My_ReportDetail_Adapter extends PagerAdapter {
    private List<View> mViewList = new ArrayList<>();
    private View view1;
    private View view2;
    private View view3;
    private Context context;
    private Report_detail report_detail;

    private Button button_zheng;
    private Button button_ce;
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
    private ImageView imageView;

    public My_ReportDetail_Adapter(Context context,String s){
        this.context=context;
        view1=View.inflate(context,R.layout.report_detail_one,null);
        view2=View.inflate(context,R.layout.reprot_detail_two,null);
        view3=View.inflate(context,R.layout.report_detail_three,null);
        mViewList.add(view1);
        mViewList.add(view2);
        mViewList.add(view3);
        List<Report_detail> report_details= LitePal.findAll(Report_detail.class);
        for(Report_detail r:report_details){
            if(r.getReport_time().equals(s)){
                report_detail=r;
                break;
            }
        }

        report_left_init();

    }

    public void report_left_init(){
        button_zheng=view1.findViewById(R.id.button_zheng);
        button_ce=view1.findViewById(R.id.button_ce);


        FileInputStream fs_zheng= null;
        FileInputStream fs_ce= null;
        try {
            fs_zheng = new FileInputStream(Environment.getExternalStorageDirectory().getAbsolutePath()+"/aphysique/data/temppicture/"+report_detail.getPathzheng());
            fs_ce = new FileInputStream(Environment.getExternalStorageDirectory().getAbsolutePath()+"/aphysique/data/temppicture/"+report_detail.getPahtce());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        bitmap_zheng= BitmapFactory.decodeStream(fs_zheng);
        bitmap_ce=BitmapFactory.decodeStream(fs_ce);
        imageView=view1.findViewById(R.id.imageView7);

        button_zheng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zheng_detail();
            }
        });
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


    // 返回界面数量
    @Override
    public int getCount() {
        return mViewList.size();
    }
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
    // 添加界面，一般会添加当前页和左右两边的页面
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mViewList.get(position));
        return mViewList.get(position);
    }
    // 去除页面
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mViewList.get(position));
    }
}
