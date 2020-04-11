package AllService;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import Data_Class.AboutRankingList;
import Data_Class.Report_detail;
import Data_Class.Report_item;
import Data_Class.User_Info;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.os.SystemClock.sleep;

public class OnlyService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {

        CreatDicument();
        try {
            DownloadWelcomeImg();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //更新排行榜的信息
        UpdataRankingList();
        Find_Finish_Order_Class find_finish_order_class=new Find_Finish_Order_Class();
        new Thread(find_finish_order_class).start();

        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    //创建文件夹目录
    /*这个是存储地址的开头Environment.getExternalStorageDirectory().getAbsolutePath()
    * -aphysique
    * --data
    * ---resources
    * ---temppicture//这个是为了放我们暂时存储的图片
    *
    * */
    private void CreatDicument(){
        String path=Environment.getExternalStorageDirectory().getAbsolutePath()+"/aphysique/data/resources";
        if(new File(path).mkdirs()){
            Log.d("OnlyService","1111111111创建欢迎界面照片目录成功");

        }else{
            Log.d("OnlyService","111111111创建欢迎界面照片目录失败");
        }
        String path1=Environment.getExternalStorageDirectory().getAbsolutePath()+"/aphysique/data/temppicture";
        if(new File(path1).mkdirs()){
            Log.d("OnlyService","1111111111创建暂时界面照片目录成功");

        }else{
            Log.d("OnlyService","111111111创建暂时界面照片目录失败");
        }
    }

    //下载欢迎界面图片的后台服务中的子线程
    private void DownloadWelcomeImg() throws IOException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient okHttpClient = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("http://123.57.235.123:8080/img/WelcomeImg.jpg")
                        .build();
                Response response = null;
                try {
                    response = okHttpClient.newCall(request).execute();
                    byte[] bytes = new byte[0];
                    bytes = response.body().bytes();
                    File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/aphysique/data/resources/WelcomeImg.png");
                    Log.d("OnlyService.class","1111111"+bytes+"\n"+Environment.getExternalStorageDirectory().getAbsolutePath());
                    FileOutputStream fos = null;
                    fos = new FileOutputStream(file);
                    fos.write(bytes, 0, bytes.length);
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    class Find_Finish_Order_Class implements Runnable{
        @Override
        public void run(){
            for(;;){
                sleep(5000);
                while (LitePal.count(User_Info.class)==0){
                    sleep(10000);
                }
                List<Report_item> list=LitePal.findAll(Report_item.class);
                if (list!=null){
                    for(Report_item report_item:list){
                        if(report_item.getStatue_now().equals("未完成")){
                            //查找请求，Android发送请求，服务器端接收到请求后直接从待定表里查找，然后直接把表中的记录删掉
                            User_Info user_info=LitePal.findFirst(User_Info.class);
                            try {
                                if(find_report(user_info.getUser_phonenumber(),report_item.getItem_time(),
                                        report_item.getZhengpath(),report_item.getCepath())){
                                    report_item.setStatue_now("完成");
                                    report_item.save();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }

    /*//监听是否有新订单完成
    private void Find_Finish_Order(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(;;){
                    sleep(5000);
                    while (LitePal.count(User_Info.class)==0){
                        sleep(10000);
                    }
                    List<Report_item> list=LitePal.findAll(Report_item.class);
                    if (list!=null){
                        for(Report_item report_item:list){
                            if(report_item.getStatue_now().equals("未完成")){
                                //查找请求，Android发送请求，服务器端接收到请求后直接从待定表里查找，然后直接把表中的记录删掉
                                User_Info user_info=LitePal.findFirst(User_Info.class);
                                try {
                                    if(find_report(user_info.getUser_phonenumber(),report_item.getItem_time(),
                                            report_item.getZhengpath(),report_item.getCepath())){
                                        report_item.setStatue_now("完成");
                                        report_item.save();
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }
        }).start();
    }*/
    //查找数据是否已经完成
    private boolean find_report(String User_phonenumber,String what_time,String zhengpath,String cepath) throws IOException, JSONException {
        OkHttpClient okHttpClient1=new OkHttpClient.Builder()
                .connectTimeout(30,TimeUnit.SECONDS)
                .readTimeout(30,TimeUnit.SECONDS)
                .writeTimeout(30,TimeUnit.SECONDS)
                .build();
        FormBody.Builder builder1=new FormBody.Builder();
        builder1.add("User_phonenumber",User_phonenumber)
                .add("time",what_time);
        Request request1=new Request.Builder()
                .url("http://123.57.235.123:8080/TheBestServe/Renew_Report_DetailServlet")
                .post(builder1.build())
                .build();
        Response response1=null;
        JSONObject jsonObject1=null;
        try {
            response1 = okHttpClient1.newCall(request1).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(response1==null){
            return false;
        }
        String ks=response1.body().string();
        if(ks.equals("no")){
            return false;
        }
        Log.d("RenewService",ks);
        jsonObject1=new JSONObject(ks);
        Report_detail reportDetail=new Report_detail();
        reportDetail.setReport_time(what_time);
        reportDetail.setPathzheng(zhengpath);
        reportDetail.setPahtce(cepath);

        reportDetail.setZheng_left_eye_x(jsonObject1.getString("zheng_left_eye_x"));
        reportDetail.setZheng_left_eye_y(jsonObject1.getString("zheng_left_eye_y"));
        reportDetail.setZheng_right_eye_x(jsonObject1.getString("zheng_right_eye_x"));
        reportDetail.setZheng_right_eye_y(jsonObject1.getString("zheng_right_eye_y"));
        reportDetail.setZheng_left_ear_x(jsonObject1.getString("zheng_left_ear_x"));
        reportDetail.setZheng_left_ear_y(jsonObject1.getString("zheng_left_ear_y"));
        reportDetail.setZheng_right_ear_x(jsonObject1.getString("zheng_right_ear_x"));
        reportDetail.setZheng_right_ear_y(jsonObject1.getString("zheng_right_ear_y"));
        reportDetail.setZheng_left_shoulder_x(jsonObject1.getString("zheng_left_shoulder_x"));
        reportDetail.setZheng_left_shoulder_y(jsonObject1.getString("zheng_left_shoulder_y"));
        reportDetail.setZheng_right_shoulder_x(jsonObject1.getString("zheng_right_shoulder_x"));
        reportDetail.setZheng_right_shoulder_y(jsonObject1.getString("zheng_right_shoulder_y"));
        reportDetail.setZheng_left_hip_x(jsonObject1.getString("zheng_left_hip_x"));
        reportDetail.setZheng_left_hip_y(jsonObject1.getString("zheng_left_hip_y"));
        reportDetail.setZheng_right_hip_x(jsonObject1.getString("zheng_right_hip_x"));
        reportDetail.setZheng_right_hip_y(jsonObject1.getString("zheng_right_hip_y"));
        reportDetail.setZheng_left_knee_x(jsonObject1.getString("zheng_left_knee_x"));
        reportDetail.setZheng_left_knee_y(jsonObject1.getString("zheng_left_knee_y"));
        reportDetail.setZheng_right_knee_x(jsonObject1.getString("zheng_right_knee_x"));
        reportDetail.setZheng_right_knee_y(jsonObject1.getString("zheng_right_knee_y"));
        reportDetail.setZheng_left_ankle_x(jsonObject1.getString("zheng_left_ankle_x"));
        reportDetail.setZheng_left_ankle_y(jsonObject1.getString("zheng_left_ankle_y"));
        reportDetail.setZheng_right_ankle_x(jsonObject1.getString("zheng_right_ankle_x"));
        reportDetail.setZheng_right_ankle_y(jsonObject1.getString("zheng_right_ankle_y"));
        reportDetail.setZheng_neck_x(jsonObject1.getString("zheng_neck_x"));
        reportDetail.setZheng_neck_y(jsonObject1.getString("zheng_neck_y"));

        reportDetail.setZheng_nose_x(jsonObject1.getString("zheng_nose_x"));
        reportDetail.setZheng_nose_y(jsonObject1.getString("zheng_nose_y"));
        reportDetail.setZheng_left_elbow_x(jsonObject1.getString("zheng_left_elbow_x"));
        reportDetail.setZheng_left_elbow_y(jsonObject1.getString("zheng_left_elbow_y"));
        reportDetail.setZheng_right_elbow_x(jsonObject1.getString("zheng_right_elbow_x"));
        reportDetail.setZheng_right_elbow_y(jsonObject1.getString("zheng_right_elbow_y"));
        reportDetail.setZheng_left_wrist_x(jsonObject1.getString("zheng_left_wrist_x"));
        reportDetail.setZheng_left_wrist_y(jsonObject1.getString("zheng_left_wrist_y"));
        reportDetail.setZheng_right_wrist_x(jsonObject1.getString("zheng_right_wrist_x"));
        reportDetail.setZheng_right_wrist_y(jsonObject1.getString("zheng_right_wrist_y"));

        reportDetail.setCe_right_ear_x(jsonObject1.getString("ce_right_ear_x"));
        reportDetail.setCe_right_ear_y(jsonObject1.getString("ce_right_ear_y"));
        reportDetail.setCe_right_shoulder_x(jsonObject1.getString("ce_right_shoulder_x"));
        reportDetail.setCe_right_shoulder_y(jsonObject1.getString("ce_right_shoulder_y"));
        reportDetail.setCe_right_hip_x(jsonObject1.getString("ce_right_hip_x"));
        reportDetail.setCe_right_hip_y(jsonObject1.getString("ce_right_hip_y"));
        reportDetail.setCe_right_knee_x(jsonObject1.getString("ce_right_knee_x"));
        reportDetail.setCe_right_knee_y(jsonObject1.getString("ce_right_knee_y"));
        reportDetail.setCe_right_ankle_x(jsonObject1.getString("ce_right_ankle_x"));
        reportDetail.setCe_right_ankle_y(jsonObject1.getString("ce_right_ankle_y"));
        reportDetail.setCe_neck_x(jsonObject1.getString("ce_neck_x"));
        reportDetail.setCe_neck_y(jsonObject1.getString("ce_neck_y"));
        reportDetail.save();
        return true;
    }

    //此处更新排行榜的信息
    private void UpdataRankingList(){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://123.57.235.123:8080/TheBestServe/UpdataRankingListServlet")
                .build();
        Call call=okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d("This","获取排行榜参数失败");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String data=response.body().string();
                Log.d("This","获取排行榜参数成功"+data);
                if(LitePal.count(AboutRankingList.class)==0){
                    AboutRankingList aboutRankingList=new AboutRankingList();
                    aboutRankingList.initJSON(data);
                    aboutRankingList.save();
                }else{
                    AboutRankingList aboutRankingList=LitePal.findFirst(AboutRankingList.class);
                    aboutRankingList.initJSON(data);
                    aboutRankingList.save();
                }
            }
        });
    }
}
