package Toolar_toNext;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luckzhang.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import org.litepal.LitePal;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import Data_Class.AboutRankingList;
import Data_Class.User_Info;

public class The_Charts_Activity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView textView18;
    private BarChart barChart;
    private User_Info user_info;
    //实例化一个List用来存储数据
    private List<BarEntry> list=new ArrayList<>();
    private AboutRankingList aboutRankingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_the__charts_);
        init();
        judge();


    }

    public void init(){
        textView18=findViewById(R.id.textView18);
        barChart=findViewById(R.id.mBarChart);
        toolbar=findViewById(R.id.toolbar23);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        aboutRankingList=LitePal.findFirst(AboutRankingList.class);
    }

    public void initChart(){
        //添加数据
        list.add(new BarEntry(0,aboutRankingList.list.get(0)));
        for(int i=1;i<21;i++){
            list.add(new BarEntry(i*5,aboutRankingList.list.get(i)-aboutRankingList.list.get(i-1)));
        }




        BarDataSet barDataSet=new BarDataSet(list,"分数区间占总人数百分比");
        barDataSet.setHighlightEnabled(false);//选中柱子是否高亮显示  默认为true
        BarData barData=new BarData(barDataSet);
        barChart.setData(barData);

        barChart.getXAxis().setDrawGridLines(false);  //是否绘制X轴上的网格线（背景里面的竖线）

        barChart.getDescription().setEnabled(false);//隐藏右下角英文
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);//X轴的位置 默认为上面
        barChart.getAxisRight().setEnabled(true);//隐藏右侧Y轴   默认是左右两侧都有Y轴

        XAxis xAxis=barChart.getXAxis();
        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setLabelCount(21);
        /*xAxis.setValueFormatter(new IAxisValueFormatter() {   //X轴自定义坐标
            @Override
            public String getFormattedValue(float v, AxisBase axisBase) {
                if (v==1){
                    return "5";
                }
                if (v==2){
                    return "10";
                }
                if (v==3){
                    return "15";
                }
                if (v==4){
                    return "20";
                }
                if (v==5){
                    return "25";
                }
                if (v==6){
                    return "30";
                }
                if (v==7){
                    return "35";
                }
                if (v==8){
                    return "40";
                }
                if (v==9){
                    return "45";
                }
                if (v==10){
                    return "50";
                }
                if (v==11){
                    return "55";
                }
                if (v==12){
                    return "60";
                }
                if (v==13){
                    return "65";
                }
                if (v==14){
                    return "70";
                }
                if (v==15){
                    return "75";
                }
                if (v==16){
                    return "80";
                }
                if (v==17){
                    return "85";
                }
                if (v==18){
                    return "90";
                }
                if (v==19){
                    return "95";
                }
                if (v==20){
                    return "100";
                }
                if (v==0){
                    return "0";
                }
                return "";//注意这里需要改成 ""
            }
        });*/
        //xAxis.setLabelCount(21,false);

        barChart.animateXY(3000,3000);
    }

    public void judge(){
        user_info= LitePal.findFirst(User_Info.class);
        if(user_info.getUser_parcent().equals("0")){
            //Toast.makeText(this, "您需要选择一个报告作为当前状态", Toast.LENGTH_SHORT).show();
            AlertDialog.Builder builder=new AlertDialog.Builder(this)
                    .setTitle("注意")
                    .setMessage("您需要选择一个报告作为当前状态(打开报告后在右上角的菜单中设置)")
                    .setCancelable(false)
                    .setPositiveButton("确定",new AlertDialog.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
            builder.create().show();
        }else{
           textView18.setText("您已经超过全国"+user_info.getUser_parcent()+"%的人");
            initChart();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
