package com.example.luckzhang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Adapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aliyun.player.AliPlayer;
import com.aliyun.player.AliPlayerFactory;
import com.aliyun.player.IPlayer;
import com.aliyun.player.source.VidAuth;
import com.aliyun.player.source.VidSts;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import Data_Class.Ten_Items;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class ExerciseActivity extends AppCompatActivity {
    private int index;
    private Toolbar toolbar;
    private Ten_Items ten_items;
    private String aliyunPlayerURL="http://123.57.235.123:8080/TheBestServe/AliplayerServlet";
    private String getVideoPathURL="http://123.57.235.123:8080/TheBestServe/FindVideoPathServlet";
    private AliPlayer aliyunVodPlayer;
    private String video_vid;
    private VidSts aliyunVidSts;
    private VidAuth aliyunVidAuth;
    private SurfaceView surfaceView;
    //private String video_vid;
    private String video_playauth;
    private String video_Title;
    private float video_Duration=0l;
    private String video_Status;
    private RelativeLayout loading;
    private TextView loading_textView;
    private boolean judge=false;
    private boolean playjudge=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        //设置系统状态栏的颜色
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        init();
        Toast.makeText(this, "开始进行网络链接", Toast.LENGTH_SHORT).show();
        FindVideoPathThread findVideoPathThread=new FindVideoPathThread();
        new Thread(findVideoPathThread).start();




    }

    @Override
    protected void onStart() {
        if(judge){
            aliyunVodPlayer.start();
            judge=false;
        }
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        aliyunVodPlayer.release();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        aliyunVodPlayer.stop();
        judge=true;
        super.onStop();
    }

    private void init(){
        loading=findViewById(R.id.loading);
        loading_textView=findViewById(R.id.loading_textView);
        toolbar=findViewById(R.id.toolbar3);
        String i= getIntent().getStringExtra("clicknumber");
        index=Integer.valueOf(i);

        List<Ten_Items> list=LitePal.findAll(Ten_Items.class);
        ten_items= list.get(index);
        toolbar.setTitle(ten_items.getTitle());
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId()==R.id.menu_itemR1){
                    if(index==0){
                        Toast.makeText(ExerciseActivity.this, "没有教程可查看", Toast.LENGTH_SHORT).show();
                    }else{
                        Intent intent=new Intent();
                        String[] tem=new String []{
                                "toubuceqing","toubuqianqing","jingzhuiyiwei","jianbuceqing","jizhuyiwei",
                                "kuanbuceqing","kuanbuyiwei","tuibuyichang","xigaiyichang"
                        };
                        intent.setClass(ExerciseActivity.this, WebActivity.class);
                        intent.putExtra("key",tem[index-1]);
                        startActivity(intent);
                    }

                }else if(item.getItemId()==R.id.menu_itemR2){
                    AlertDialog.Builder builder=new AlertDialog.Builder(ExerciseActivity.this)
                            .setTitle("打卡须知")
                            .setMessage("您是否按照视频的内容，认真地完成了所有的项目？")
                            .setPositiveButton("是的，认真完成",new AlertDialog.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");// HH:mm:ss
                                    // 获取当前时间
                                    Date date = new Date(System.currentTimeMillis());
                                    ten_items.setData(simpleDateFormat.format(date));
                                    ten_items.save();
                                    Toast.makeText(ExerciseActivity.this, "打卡成功："+simpleDateFormat.format(date), Toast.LENGTH_SHORT).show();

                                }
                            })
                            .setNegativeButton("没有完成",new AlertDialog.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(ExerciseActivity.this, "请您继续训练！", Toast.LENGTH_SHORT).show();
                                }
                            });
                    builder.create().show();
                }
                return false;
            }
        });
    }

    private void init_video(){


        surfaceView=findViewById(R.id.surfaceView);

        aliyunVodPlayer= AliPlayerFactory.createAliPlayer(getApplicationContext());

        aliyunVodPlayer.setOnCompletionListener(new IPlayer.OnCompletionListener() {
            @Override
            public void onCompletion() {
                //播放完成事件
            }
        });
        aliyunVodPlayer.setOnPreparedListener(new IPlayer.OnPreparedListener() {
            @Override
            public void onPrepared() {
                //准备成功事件
            }
        });
        aliyunVodPlayer.setOnLoadingStatusListener(new IPlayer.OnLoadingStatusListener() {
            @Override
            public void onLoadingBegin() {
                //缓冲开始。
            }
            @Override
            public void onLoadingProgress(int percent, float kbps) {
                //缓冲进度
            }
            @Override
            public void onLoadingEnd() {
                //缓冲结束
            }
        });

        aliyunVidAuth=new VidAuth();
        aliyunVidAuth.setVid(video_vid);
        aliyunVidAuth.setPlayAuth(video_playauth);
        aliyunVidAuth.setRegion("cn-shanghai");

        //设置播放源
        aliyunVodPlayer.setDataSource(aliyunVidAuth);
        aliyunVodPlayer.setDisplay(surfaceView.getHolder());

        aliyunVodPlayer.prepare();

        //播放目标绑定
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                aliyunVodPlayer.setDisplay(holder);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                aliyunVodPlayer.redraw();
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                aliyunVodPlayer.setDisplay(null);
            }
        });

        aliyunVodPlayer.setOnLoadingStatusListener(new IPlayer.OnLoadingStatusListener() {
            @Override
            public void onLoadingBegin() {
                loading.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingProgress(int i, float v) {
                loading_textView.setText(i+"%");
            }

            @Override
            public void onLoadingEnd() {
                loading.setVisibility(View.GONE);
            }
        });

        //播放状态 播放是3，暂停是4，前两个可能是准备？
        aliyunVodPlayer.setOnStateChangedListener(new IPlayer.OnStateChangedListener() {
            @Override
            public void onStateChanged(int i) {
                if (i==3){
                    playjudge=true;
                    toolbar.setVisibility(View.GONE);
                }else if(i==4){
                    playjudge=false;
                    toolbar.setVisibility(View.VISIBLE);
                }
            }
        });

        //播放结束监听
        aliyunVodPlayer.setOnCompletionListener(new IPlayer.OnCompletionListener() {
            @Override
            public void onCompletion() {
                playjudge=false;
                AlertDialog.Builder builder=new AlertDialog.Builder(ExerciseActivity.this)
                        .setTitle("完成")
                        .setMessage("您已经完成本次锻炼，可以选择打卡，也可以选择再锻炼一次")
                        .setPositiveButton("可以打卡了",new AlertDialog.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(ExerciseActivity.this, "右上角菜单中可以打卡", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("我想再锻炼一次",new AlertDialog.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(ExerciseActivity.this, "准备开始，长时间没有反应可退出重试", Toast.LENGTH_SHORT).show();
                                aliyunVodPlayer.prepare();
                                aliyunVodPlayer.start();
                            }
                        });
                builder.create().show();
            }
        });
        //设置画面缩放模式：宽高比填充，宽高比适应，拉伸填充
        aliyunVodPlayer.setScaleMode(IPlayer.ScaleMode.SCALE_ASPECT_FILL);

        aliyunVodPlayer.start();

        surfaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(playjudge){
                    aliyunVodPlayer.pause();
                }else {
                    aliyunVodPlayer.start();
                }
            }
        });


    }

    private Handler perparhandler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            if(msg.what==0){
                Toast.makeText(ExerciseActivity.this, "查询地址成功", Toast.LENGTH_SHORT).show();
                AboutVideoThread aboutVideoThread= new AboutVideoThread();
                new Thread(aboutVideoThread).start();
            }else if(msg.what==1){
                Toast.makeText(ExerciseActivity.this, "准备播放", Toast.LENGTH_SHORT).show();
                init_video();
            }else if(msg.what==2){
                Toast.makeText(ExerciseActivity.this, "查找视频信息失败", Toast.LENGTH_SHORT).show();
            }else if(msg.what==3){
                Toast.makeText(ExerciseActivity.this, "解析视频信息失败", Toast.LENGTH_SHORT).show();
            }else if(msg.what==4){
                Toast.makeText(ExerciseActivity.this, "查询地址失败", Toast.LENGTH_SHORT).show();
            }
        }
    };

    class FindVideoPathThread implements Runnable{
        @Override
        public void run() {
            OkHttpClient okHttpClient=new OkHttpClient();
            FormBody body=new FormBody.Builder()
                    .add("number",index+"")
                    .build();
            Request request=new Request.Builder()
                    .post(body)
                    .url(getVideoPathURL)
                    .build();
            Response response=null;
            Call call=okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Message message=new Message();
                    message.what=4;
                    perparhandler.sendMessage(message);
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String idstring=response.body().string();
                    if(idstring.equals("no")){
                        Message message=new Message();
                        message.what=4;
                        perparhandler.sendMessage(message);
                    }else{
                        try {
                            JSONObject jsonObject=new JSONObject(idstring);
                            video_vid =jsonObject.getString("path");
                            Message message=new Message();
                            message.what=0;
                            perparhandler.sendMessage(message);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }

    class AboutVideoThread implements Runnable{
        @Override
        public void run() {
            OkHttpClient okHttpClient=new OkHttpClient();
            FormBody body=new FormBody.Builder()
                    .add("VideoId", video_vid)
                    .build();
            Request request=new Request.Builder()
                    .post(body)
                    .url(aliyunPlayerURL)
                    .build();
            Response response=null;
            Call call=okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Message message=new Message();
                    message.what=2;
                    perparhandler.sendMessage(message);
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String re=response.body().string();
                    Log.d("OnlineSchool","接受信息："+re);
                    try {
                        JSONObject jsonObject=new JSONObject(re);
                        JSONObject VideoMeta=new JSONObject(jsonObject.getString("VideoMeta"));
                        video_playauth=jsonObject.getString("PlayAuth");
                        video_Title=VideoMeta.getString("Title");
                        video_Duration=Float.parseFloat(VideoMeta.getString("Duration"));
                        video_Status=VideoMeta.getString("Status");
                        Message message=new Message();
                        message.what=1;
                        perparhandler.sendMessage(message);
                    } catch (JSONException e) {
                        Message message=new Message();
                        message.what=3;
                        perparhandler.sendMessage(message);
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
