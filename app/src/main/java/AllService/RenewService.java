package AllService;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.mob.wrappers.UMSSDKWrapper;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;

import java.io.IOException;
import java.util.List;

import Data_Class.Report_detail;
import Data_Class.Report_item;
import Data_Class.User_Info;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.os.SystemClock.sleep;

public class RenewService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        UpdataUserFive();
        super.onCreate();
        Find_Finish_Order();
    }
    //监听是否有新订单完成
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
    }
    //查找数据是否已经完成
    private boolean find_report(String User_phonenumber,String what_time,String zhengpath,String cepath) throws IOException, JSONException {
        OkHttpClient okHttpClient=new OkHttpClient();
        FormBody.Builder builder=new FormBody.Builder();
        builder.add("User_phonenumber",User_phonenumber)
                .add("time",what_time);
        Request request=new Request.Builder()
                .url("http://192.168.43.96:8085/TheBestServe/Renew_Report_DetailServlet")
                .post(builder.build())
                .build();
        Response response=null;
        JSONObject jsonObject=null;
        try {
            response = okHttpClient.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String ks=response.body().string();
        if(ks.equals("no")){
            return false;
        }
        Log.d("RenewService",ks);
        jsonObject=new JSONObject(ks);
        Report_detail reportDetail=new Report_detail();
        reportDetail.setReport_time(what_time);
        reportDetail.setPathzheng(zhengpath);
        reportDetail.setPahtce(cepath);

        reportDetail.setZheng_left_eye_x(jsonObject.getString("zheng_left_eye_x"));
        reportDetail.setZheng_left_eye_y(jsonObject.getString("zheng_left_eye_y"));
        reportDetail.setZheng_right_eye_x(jsonObject.getString("zheng_right_eye_x"));
        reportDetail.setZheng_right_eye_y(jsonObject.getString("zheng_right_eye_y"));
        reportDetail.setZheng_left_ear_x(jsonObject.getString("zheng_left_ear_x"));
        reportDetail.setZheng_left_ear_y(jsonObject.getString("zheng_left_ear_y"));
        reportDetail.setZheng_right_eye_x(jsonObject.getString("zheng_right_ear_x"));
        reportDetail.setZheng_right_eye_y(jsonObject.getString("zheng_right_ear_y"));
        reportDetail.setZheng_left_shoulder_x(jsonObject.getString("zheng_left_shoulder_x"));
        reportDetail.setZheng_left_shoulder_y(jsonObject.getString("zheng_left_shoulder_y"));
        reportDetail.setZheng_right_shoulder_x(jsonObject.getString("zheng_right_shoulder_x"));
        reportDetail.setZheng_right_shoulder_y(jsonObject.getString("zheng_right_shoulder_y"));
        reportDetail.setZheng_left_hip_x(jsonObject.getString("zheng_left_hip_x"));
        reportDetail.setZheng_left_hip_y(jsonObject.getString("zheng_left_hip_y"));
        reportDetail.setZheng_right_hip_x(jsonObject.getString("zheng_right_hip_x"));
        reportDetail.setZheng_right_hip_y(jsonObject.getString("zheng_right_hip_y"));
        reportDetail.setZheng_left_knee_x(jsonObject.getString("zheng_left_knee_x"));
        reportDetail.setZheng_left_knee_y(jsonObject.getString("zheng_left_knee_y"));
        reportDetail.setZheng_right_knee_x(jsonObject.getString("zheng_right_knee_x"));
        reportDetail.setZheng_right_knee_y(jsonObject.getString("zheng_right_knee_y"));
        reportDetail.setZheng_left_ankle_x(jsonObject.getString("zheng_left_ankle_x"));
        reportDetail.setZheng_left_ankle_y(jsonObject.getString("zheng_left_ankle_y"));
        reportDetail.setZheng_right_ankle_x(jsonObject.getString("zheng_right_ankle_x"));
        reportDetail.setZheng_right_ankle_y(jsonObject.getString("zheng_right_ankle_y"));
        reportDetail.setCe_right_ear_x(jsonObject.getString("ce_right_ear_x"));
        reportDetail.setCe_right_ear_y(jsonObject.getString("ce_right_ear_y"));
        reportDetail.setCe_right_shoulder_x(jsonObject.getString("ce_right_shoulder_x"));
        reportDetail.setCe_right_shoulder_y(jsonObject.getString("ce_right_shoulder_y"));
        reportDetail.setCe_right_hip_x(jsonObject.getString("ce_right_hip_x"));
        reportDetail.setCe_right_hip_y(jsonObject.getString("ce_right_hip_y"));
        reportDetail.setCe_right_knee_x(jsonObject.getString("ce_right_knee_x"));
        reportDetail.setCe_right_knee_y(jsonObject.getString("ce_right_knee_y"));
        reportDetail.setCe_right_ankle_x(jsonObject.getString("ce_right_ankle_x"));
        reportDetail.setCe_right_ankle_y(jsonObject.getString("ce_right_ankle_y"));
        reportDetail.save();
        return true;
    }

    //此处更新用户信息表
    private void UpdataUserFive(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(;;){
                    User_Info user_info=null;
                    while (user_info==null){
                        user_info= LitePal.findFirst(User_Info.class);
                        sleep(5000);
                    }
                    OkHttpClient okHttpClient = new OkHttpClient();
                    FormBody.Builder builder=new FormBody.Builder();
                    builder.add("User_phonenumber",user_info.getUser_phonenumber());
                    Request request = new Request.Builder()
                            .url("http://192.168.43.96:8085/TheBestServe/RenewMainLeftServlet")
                            .post(builder.build())
                            .build();
                    Response response=null;
                    JSONObject jsonObject=null;
                    try {
                        response = okHttpClient.newCall(request).execute();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        jsonObject=new JSONObject(response.body().string());
                        user_info.setUser_five_fen(jsonObject.getString("User_five_fen"));
                        user_info.setUser_five_ci(jsonObject.getString("User_five_ci"));
                        user_info.setUser_five_zheng(jsonObject.getString("User_five_zheng"));
                        user_info.setUser_five_yu(jsonObject.getString("User_five_yi"));
                        user_info.setUser_five_yu(jsonObject.getString("User_five_yu"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    user_info.save();
                    sleep(5000);
                }

            }
        }).start();
    }
}
