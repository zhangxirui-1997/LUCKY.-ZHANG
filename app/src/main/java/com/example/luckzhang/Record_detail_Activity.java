package com.example.luckzhang;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import org.litepal.LitePal;

import java.io.FileNotFoundException;
import java.util.List;

import Data_Class.Report_detail;
import Data_Class.Report_item;
import My_ViewPager.My_ReportDetail_ViewPagerAdapter;

public class Record_detail_Activity extends AppCompatActivity {

    private Toolbar toolbar;
    private String strPxtr;
    private Report_detail report_detail;
    private Button button1;
    private Button button2;
    private Button button3;
    private ViewPager viewPager;
    private My_ReportDetail_ViewPagerAdapter my_reportDetail_viewPagerAdapter;

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

    private void toolbar_initialize(){
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId()==R.id.menu_itemR1){
                    //此处进行加星操作
                    my_reportDetail_viewPagerAdapter.save_five();
                    Toast.makeText(Record_detail_Activity.this, "已设置该报告为当前状态", Toast.LENGTH_SHORT).show();
                }else if(item.getItemId()==R.id.menu_itemR2){
                    AlertDialog.Builder builder=new AlertDialog.Builder(Record_detail_Activity.this)
                            .setTitle("注意")
                            .setMessage("确认删除此报告吗？")
                            .setPositiveButton("确认",new AlertDialog.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    LitePal.deleteAll(Report_item.class,"item_time=?",strPxtr);
                                    LitePal.deleteAll(Report_detail.class,"Report_time=?",strPxtr);
                                    finish();
                                }
                            })
                            .setNegativeButton("取消",new AlertDialog.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    builder.create().show();

                }
                return false;
            }
        });

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
        
        button1=findViewById(R.id.button7);
        button2=findViewById(R.id.button8);
        button3=findViewById(R.id.button9);
        viewPager=findViewById(R.id.viewpager_report_item);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1);
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(2);
            }
        });
        my_reportDetail_viewPagerAdapter=new My_ReportDetail_ViewPagerAdapter(this,strPxtr);
        viewPager.setAdapter(my_reportDetail_viewPagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                if(position==0){
                    button1.setBackground(getResources().getDrawable(R.drawable.button_mainviewpager_1_circle_shape,null));
                    button2.setBackground(getResources().getDrawable(R.drawable.button_mainviewpager_2_circle_shape,null));
                    button3.setBackground(getResources().getDrawable(R.drawable.button_mainviewpager_2_circle_shape,null));
                }else if(position==1){
                    button1.setBackground(getResources().getDrawable(R.drawable.button_mainviewpager_2_circle_shape,null));
                    button2.setBackground(getResources().getDrawable(R.drawable.button_mainviewpager_1_circle_shape,null));
                    button3.setBackground(getResources().getDrawable(R.drawable.button_mainviewpager_2_circle_shape,null));
                }else if(position==2){
                    button1.setBackground(getResources().getDrawable(R.drawable.button_mainviewpager_2_circle_shape,null));
                    button2.setBackground(getResources().getDrawable(R.drawable.button_mainviewpager_2_circle_shape,null));
                    button3.setBackground(getResources().getDrawable(R.drawable.button_mainviewpager_1_circle_shape,null));

                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        toolbar=findViewById(R.id.toolbar2);
        toolbar_initialize();
    }

}
