package SomeTools;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.luckzhang.R;

import java.util.List;

import Data_Class.Report_Some_ills;
import Data_Class.Report_item;

public class MyReportDetailListAdapter extends ArrayAdapter<Report_Some_ills> {
    private int resourceId;
    private Context context;

    /**
     *context:当前活动上下文
     *textViewResourceId:ListView子项布局的ID
     *objects：要适配的数据
     */
    public MyReportDetailListAdapter(Context context, int textViewResourceId, List<Report_Some_ills> objects) {
        super(context, textViewResourceId, objects);
        //拿取到子项布局ID
        this.context=context;
        resourceId = textViewResourceId;

    }



    /**
     * LIstView中每一个子项被滚动到屏幕的时候调用
     * position：滚到屏幕中的子项位置，可以通过这个位置拿到子项实例
     * convertView：之前加载好的布局进行缓存
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Report_Some_ills report_some_ills = getItem(position);  //获取当前项的Fruit实例
        //为子项动态加载布局
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);

        MyProgreeBar progressBar=view.findViewById(R.id.progressBar2);
        progressBar.letuspaint(Math.round(report_some_ills.getIlls_statue()));
        TextView textView=view.findViewById(R.id.textView9);
        ImageView imageView=view.findViewById(R.id.imageView8);
        TextView textView1=view.findViewById(R.id.textView13);
        TextView textView2=view.findViewById(R.id.textView_number);
        TextView textViewzhengchang=view.findViewById(R.id.textView10);
        TextView textViewgaofengxian=view.findViewById(R.id.textView11);
        RelativeLayout linearLayout=view.findViewById(R.id.r1);
        //progressBar.setProgress(Math.round(report_some_ills.getIlls_statue())+1);
        Log.d( "My","1111111111"+Math.round(report_some_ills.getIlls_statue()));
        //progressBar.setMax(report_some_ills.getIll_max());
        textView.setText(report_some_ills.getIlls_name());
         textView1.setText(report_some_ills.getIlls_tips());
        imageView.setImageBitmap(report_some_ills.getBitmap());
        textView2.setText("风险系数："+report_some_ills.getIlls_statue()+"");
        textViewzhengchang.setText("0(低风险)");
        textViewgaofengxian.setText("(高风险)"+(report_some_ills.getIll_max()-1));
        if(report_some_ills.getIlls_statue()<report_some_ills.getIll_max()*0.2){
            linearLayout.setBackgroundColor(context.getResources().getColor(R.color.color_healthyGreen_qian,null));
        }else if(report_some_ills.getIlls_statue()<report_some_ills.getIll_max()*0.6){
            linearLayout.setBackgroundColor(context.getResources().getColor(R.color.color_warnOrange_qian,null));
        }else {
            linearLayout.setBackgroundColor(context.getResources().getColor(R.color.color_errorRed_qian,null));
        }
        return view;
    }
}
