package com.example.luckzhang;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import org.litepal.LitePal;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import Data_Class.Report_detail;
import LoginAndRegister.User_detail_Activity;
import My_ViewPager.MyPageAdapter;
import My_ViewPager.My_ReportDetail_Adapter;
import Toolar_toNext.About_soft_Activity;
import Toolar_toNext.Help_Activity;
import Toolar_toNext.The_Charts_Activity;

public class Record_detail_Activity extends AppCompatActivity {

    private Toolbar toolbar;
    private String strPxtr;
    private Report_detail report_detail;
    private Button button1;
    private Button button2;
    private Button button3;
    private ViewPager viewPager;

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
                    Toast.makeText(Record_detail_Activity.this, "已将本报告设置为主要", Toast.LENGTH_LONG).show();
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
        toolbar=findViewById(R.id.toolbar2);
        toolbar_initialize();
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
        viewPager.setAdapter(new My_ReportDetail_Adapter(this,strPxtr));
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
    }



}
