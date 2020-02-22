package com.example.luckzhang;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.camera2.CameraCharacteristics;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.litepal.LitePal;

import java.io.File;

import Data_Class.Report_item;

public class Check_Activity extends AppCompatActivity {
    private ImageView imageView_zheng;
    private ImageView imageView_ce;
    private Button zheng_button;
    private Button ce_button;
    private Button check_button;
    private String path_zheng;
    private String path_ce;
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


}
