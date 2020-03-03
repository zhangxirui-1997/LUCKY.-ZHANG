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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import com.example.luckzhang.R;
import com.mob.wrappers.UMSSDKWrapper;

import org.litepal.LitePal;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import Data_Class.Report_Some_ills;
import Data_Class.Report_detail;
import Data_Class.User_Info;
import SomeTools.MyReportDetailListAdapter;

public class My_ReportDetail_ViewPagerAdapter extends PagerAdapter {
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

    private ListView listView;
    private ArrayList<Report_Some_ills> items=new ArrayList<>();
    private MyReportDetailListAdapter myReportDetailListAdapter=null;

    private int normal_number=0;
    private int error_number=0;
    private int warn_number=0;
    private float fenshu=0f;
    private String zhaungtai="";
    private String idtime="";

    public My_ReportDetail_ViewPagerAdapter(Context context, String s){
        this.context=context;
        idtime=s;
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
        repotr_mid_init();
        report_right_init();
    }

    public void save_five(){
        User_Info user_info=LitePal.findFirst(User_Info.class);
        user_info.setUser_five_fen(fenshu+"");
        user_info.setUser_five_ci(zhaungtai);
        user_info.setUser_five_zheng(normal_number+"");
        user_info.setUser_five_yu(warn_number+"");
        user_info.setUser_five_yi(error_number+"");
        user_info.setUser_five_id_time(idtime);
        user_info.save();
    }

    public void report_right_init(){

        for(Report_Some_ills r:items){
            fenshu=fenshu+(r.getIll_max()-1-r.getIlls_statue())*100/9/(r.getIll_max()-1);
            if(r.getIlls_statue()<r.getIll_max()*0.3){
                normal_number++;
            }else if(r.getIlls_statue()<r.getIll_max()*0.6){
                warn_number++;
            }else {
                error_number++;
            }
        }

        TextView textView=view3.findViewById(R.id.textView14);
        textView.setText("您已经超过全国："+"的人");

        TextView t1=view3.findViewById(R.id.tt1);
        TextView t2=view3.findViewById(R.id.tt2);
        TextView t3=view3.findViewById(R.id.tt3);
        TextView t4=view3.findViewById(R.id.tt4);
        TextView t5=view3.findViewById(R.id.tt5);

        TextView t6=view3.findViewById(R.id.textView16);

        t1.setText(fenshu+"");
        if(fenshu>95){
            zhaungtai="完美";
            t2.setText("完美");
            t6.setText("结论：努力保持当前状态");
        }else if(fenshu>80){
            zhaungtai="健康";
            t2.setText("健康");
            t6.setText("结论：体态正常，还能更好");
        }else if(fenshu>60){
            zhaungtai="亚健康";
            t2.setText("亚健康");
            t6.setText("结论：需要及时调整体态，可以选择锻炼或者就医");
        }else {
            zhaungtai="差";
            t2.setText("差");
            t6.setText("结论：请及时就医");
        }
        t3.setText(normal_number+"");
        t4.setText(warn_number+"");
        t5.setText(error_number+"");

    }

    //下面是中间初始化
    public void repotr_mid_init(){
        listView=view2.findViewById(R.id.reprot_detail_list);
        judge_ills();
        myReportDetailListAdapter=new MyReportDetailListAdapter(
                context,R.layout.record_detail_item_layout,items);
        listView.setAdapter(myReportDetailListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    public void judge_ills(){
        //开始判断
        Report_Some_ills head_sealine=new Report_Some_ills();
        head_sealine.setIlls_name("头部侧倾");
        head_sealine.setIlls_statue(angle_jiandu(jian_calculate(report_detail.getZheng_left_eye_y(),report_detail.getZheng_right_eye_y()),
                jian_calculate(report_detail.getZheng_right_eye_x(),report_detail.getZheng_right_eye_x())));
        head_sealine.setIlls_tips("toubuceqing");
        head_sealine.setBitmap(Bitmap.createBitmap(copy_zheng_Bitmap,
                Math.round(Float.valueOf(report_detail.getZheng_right_eye_x()))-100,
                Math.round(Float.valueOf(report_detail.getZheng_right_eye_y()))-100,
                Math.round(jian_calculate(report_detail.getZheng_left_eye_x(),report_detail.getZheng_right_eye_x()))+200,
                Math.round(jian_calculate(report_detail.getZheng_left_eye_y(),report_detail.getZheng_right_eye_y()))+200));
        head_sealine.setIll_max(31);
        items.add(head_sealine);

        Report_Some_ills head_line=new Report_Some_ills();
        head_line.setIlls_name("头部前倾");
        head_line.setIlls_statue((angle_jiandu(jian_calculate(report_detail.getCe_neck_x(),report_detail.getCe_right_ear_x()),
                jian_calculate(report_detail.getCe_neck_y(),report_detail.getCe_right_ear_y())))/2);
        head_line.setIlls_tips("toubuqiangqing");
        head_line.setBitmap(Bitmap.createBitmap(copy_ce_Bitmap,
                Math.round(Float.valueOf(report_detail.getCe_neck_x()))-100,
                Math.round(Float.valueOf(report_detail.getCe_neck_y()))-100,
                200,
                200));
        head_line.setIll_max(31);
        items.add(head_line);

        Report_Some_ills neck_line=new Report_Some_ills();
        neck_line.setIlls_name("颈椎异位");
        float shoulder_average_x=(Float.valueOf(report_detail.getZheng_left_shoulder_x())+Float.valueOf(report_detail.getZheng_right_shoulder_x()))/2;
        float shoulder_average_y=(Float.valueOf(report_detail.getZheng_left_shoulder_x())+Float.valueOf(report_detail.getZheng_right_shoulder_x()))/2;
        neck_line.setIlls_statue(angle_jiandu(jian_calculate(report_detail.getZheng_neck_x(),Float.toString( shoulder_average_x)),
                jian_calculate(report_detail.getZheng_neck_y(),Float.toString(shoulder_average_y))));
        neck_line.setIlls_tips("jizhuiyiwei");
        neck_line.setBitmap(Bitmap.createBitmap(copy_zheng_Bitmap,
                Math.round(Float.valueOf(report_detail.getZheng_neck_x()))-200,
                Math.round(Float.valueOf(report_detail.getZheng_neck_y()))-200,
                400, 400));
        neck_line.setIll_max(31);
        items.add(neck_line);

        Report_Some_ills shoulder_sealine=new Report_Some_ills();
        shoulder_sealine.setIlls_name("肩部侧倾");
        shoulder_sealine.setIlls_statue(angle_jiandu(jian_calculate(report_detail.getZheng_left_shoulder_y(),report_detail.getZheng_right_shoulder_y()),
                jian_calculate(report_detail.getZheng_left_shoulder_x(),report_detail.getZheng_right_shoulder_x())));
        shoulder_sealine.setIlls_tips("jianbuceqing");
        shoulder_sealine.setBitmap(Bitmap.createBitmap(copy_zheng_Bitmap,
                Math.round(Float.valueOf(report_detail.getZheng_right_shoulder_x()))-100,
                Math.round(Float.valueOf(report_detail.getZheng_right_shoulder_y()))-200,
                Math.round(jian_calculate(report_detail.getZheng_left_shoulder_x(),report_detail.getZheng_right_shoulder_x()))+200,
                Math.round(jian_calculate(report_detail.getZheng_left_shoulder_y(),report_detail.getZheng_right_shoulder_y()))+400));
        shoulder_sealine.setIll_max(31);
        items.add(shoulder_sealine);

        Report_Some_ills spine_line=new Report_Some_ills();
        spine_line.setIlls_name("脊柱异位");
        float hip_average_x=(Float.valueOf(report_detail.getZheng_left_hip_x())+Float.valueOf(report_detail.getZheng_right_hip_x()))/2;
        float hip_average_y=(Float.valueOf(report_detail.getZheng_left_hip_y())+Float.valueOf(report_detail.getZheng_right_hip_y()))/2;
        spine_line.setIlls_statue(angle_jiandu(jian_calculate(Float.toString(hip_average_x),Float.toString(shoulder_average_x)),
                jian_calculate(Float.toString(hip_average_y),Float.toString(shoulder_average_y))));
        spine_line.setIlls_tips("jizhuyiwei");
        spine_line.setBitmap(Bitmap.createBitmap(copy_zheng_Bitmap,
                Math.round(Float.valueOf(report_detail.getZheng_right_shoulder_x()))-100,
                Math.round(Float.valueOf(report_detail.getZheng_right_shoulder_y()))-100,
                Math.round(jian_calculate(report_detail.getZheng_left_shoulder_x(),report_detail.getZheng_right_shoulder_x()))+200,
                Math.round(jian_calculate(report_detail.getZheng_left_hip_y(),report_detail.getZheng_left_shoulder_y()))+200));
        spine_line.setIll_max(31);
        items.add(spine_line);

        Report_Some_ills hip_sealine=new Report_Some_ills();
        hip_sealine.setIlls_name("髋部侧倾");
        hip_sealine.setIlls_statue(angle_jiandu(jian_calculate(report_detail.getZheng_left_hip_y(),report_detail.getZheng_right_hip_y()),
                jian_calculate(report_detail.getZheng_right_hip_x(),report_detail.getZheng_right_hip_x())));
        hip_sealine.setIlls_tips("kuanbuceqing");
        hip_sealine.setBitmap(Bitmap.createBitmap(copy_zheng_Bitmap,
                Math.round(Float.valueOf(report_detail.getZheng_right_hip_x()))-100,
                Math.round(Float.valueOf(report_detail.getZheng_right_hip_y()))-300,
                Math.round(jian_calculate(report_detail.getZheng_left_hip_x(),report_detail.getZheng_right_hip_x()))+200,
                Math.round(jian_calculate(report_detail.getZheng_left_hip_y(),report_detail.getZheng_right_hip_y()))+600));
        hip_sealine.setIll_max(31);
        items.add(hip_sealine);

        Report_Some_ills ce_hip_state=new Report_Some_ills();
        float ce_hip_x=Float.valueOf(report_detail.getCe_right_hip_x());
        float ce_hip_y=Float.valueOf(report_detail.getCe_right_hip_y());
        float ce_knee_x=Float.valueOf(report_detail.getCe_right_knee_x());
        float ce_knee_y=Float.valueOf(report_detail.getCe_right_knee_y());
        float ce_shoulder_x=Float.valueOf(report_detail.getCe_right_shoulder_x());
        float ce_shoulder_y=Float.valueOf(report_detail.getCe_right_shoulder_y());
        ce_hip_state.setBitmap(Bitmap.createBitmap(copy_ce_Bitmap,
                Math.round(Float.valueOf(report_detail.getCe_right_hip_x()))-200,
                Math.round(Float.valueOf(report_detail.getCe_right_hip_y()))-200,
                400,
                400));
        if(ce_hip_x>ce_knee_x&&ce_hip_x>ce_shoulder_x){//骨盆前倾
            ce_hip_state.setIlls_name("髋部前倾");
        }else if(ce_hip_x<ce_knee_x&&ce_hip_x<ce_shoulder_x){//骨盆后错
            ce_hip_state.setIlls_name("髋部后移");
        }else{//骨盆异位
            ce_hip_state.setIlls_name("髋部异位");
        }
        ce_hip_state.setIll_max(31);
        ce_hip_state.setIlls_tips("60");
        ce_hip_state.setIlls_statue((angle_jiandu(jian_calculate(report_detail.getCe_right_hip_x(),report_detail.getCe_right_knee_x()),
                jian_calculate(report_detail.getCe_right_hip_y(),report_detail.getCe_right_knee_y()))+
                angle_jiandu(jian_calculate(report_detail.getCe_right_hip_x(),report_detail.getCe_neck_x()),
                        jian_calculate(report_detail.getCe_right_hip_y(),report_detail.getCe_neck_y())))/2);
        items.add(ce_hip_state);

        Report_Some_ills legs_line=new Report_Some_ills();
        float right_hip_x=Float.valueOf(report_detail.getZheng_right_hip_x());
        float right_hip_y=Float.valueOf(report_detail.getZheng_right_hip_y());
        float right_knee_x=Float.valueOf(report_detail.getZheng_right_knee_x());
        float right_knee_y=Float.valueOf(report_detail.getZheng_right_knee_y());
        float right_ankle_x=Float.valueOf(report_detail.getZheng_right_ankle_x());
        float right_ankle_y=Float.valueOf(report_detail.getZheng_right_ankle_y());
        legs_line.setBitmap(Bitmap.createBitmap(copy_zheng_Bitmap,
                Math.round(Float.valueOf(report_detail.getZheng_right_knee_x()))-100,
                Math.round(Float.valueOf(report_detail.getZheng_right_knee_y()))-300,
                Math.round(jian_calculate(report_detail.getZheng_left_knee_x(),report_detail.getZheng_right_knee_x()))+200,
                600));
        if(right_knee_x>right_ankle_x&&right_knee_x>right_hip_x){//x型腿
            legs_line.setIlls_name("X型腿");
            legs_line.setIlls_tips("");
        }else if(right_ankle_x<right_knee_x&&right_hip_x<right_knee_x){//o型腿
            legs_line.setIlls_name("O型腿");
            legs_line.setIlls_tips("");
        }else{
            legs_line.setIlls_name("腿部异常");
            legs_line.setIlls_tips("");
        }
        legs_line.setIll_max(31);
        legs_line.setIlls_statue((angle_jiandu(jian_calculate(report_detail.getZheng_right_hip_x(),report_detail.getZheng_right_knee_x()),
                jian_calculate(report_detail.getZheng_right_hip_y(),report_detail.getZheng_right_knee_y()))+
                angle_jiandu(jian_calculate(report_detail.getZheng_right_ankle_x(),report_detail.getZheng_right_knee_x()),
                jian_calculate(report_detail.getZheng_right_ankle_y(),report_detail.getZheng_right_knee_y()))+
                angle_jiandu(jian_calculate(report_detail.getZheng_left_hip_x(),report_detail.getZheng_left_knee_x()),
                        jian_calculate(report_detail.getZheng_left_hip_y(),report_detail.getZheng_left_knee_y()))+
                angle_jiandu(jian_calculate(report_detail.getZheng_left_ankle_x(),report_detail.getZheng_left_knee_x()),
                        jian_calculate(report_detail.getZheng_left_ankle_y(),report_detail.getZheng_left_knee_y())))/4);
        items.add(legs_line);

        Report_Some_ills ce_knee_state=new Report_Some_ills();
        float ce_ankle_x=Float.valueOf(report_detail.getCe_right_ankle_x());
        float ce_ankle_y=Float.valueOf(report_detail.getCe_right_ankle_y());
        ce_knee_state.setBitmap(Bitmap.createBitmap(copy_ce_Bitmap,
                Math.round(Float.valueOf(report_detail.getCe_right_knee_x()))-200,
                Math.round(Float.valueOf(report_detail.getCe_right_knee_y()))-200,
                400,
                400));
        if(ce_hip_x<ce_knee_x&&ce_knee_x>ce_ankle_x){//膝盖前倾
            ce_knee_state.setIlls_name("膝盖前倾");
        }else if(ce_hip_x>ce_knee_x&&ce_knee_x<ce_ankle_x){//膝盖后错
            ce_knee_state.setIlls_name("膝盖后移");
        }else{//骨盆异位
            ce_knee_state.setIlls_name("膝盖异位");
        }
        ce_knee_state.setIll_max(31);
        ce_knee_state.setIlls_tips("60");
        ce_knee_state.setIlls_statue((angle_jiandu(jian_calculate(report_detail.getCe_right_hip_x(),report_detail.getCe_right_knee_x()),
                jian_calculate(report_detail.getCe_right_hip_y(),report_detail.getCe_right_knee_y()))+
                angle_jiandu(jian_calculate(report_detail.getCe_right_knee_x(),report_detail.getCe_right_ankle_x()),
                        jian_calculate(report_detail.getCe_right_knee_y(),report_detail.getCe_right_ankle_y())))/2);
        items.add(ce_knee_state);
    }


    public float jian_calculate(String f1, String f2){
        Float float1=Float.valueOf(f1);
        Float float2=Float.valueOf(f2);
        if(float1<float2){
            return float2-float1;
        }else{
            return float1-float2;
        }
    }

    public float angle_jiandu(float dui,float lin){
        double tan = Math.atan2(dui,lin);
        double angle=180*tan/Math.PI;
        return (float) angle;
    }

    //下面是左侧初始化
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
        ce_detail();
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
