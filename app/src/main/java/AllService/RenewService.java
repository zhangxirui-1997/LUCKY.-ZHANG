package AllService;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;

import java.io.IOException;
import java.util.List;

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
    }
    //监听是否有新订单完成
    private void Find_Finish_Order(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(;;){
                    sleep(1000);
                    List<Report_item> list=LitePal.findAll(Report_item.class);
                    if (list!=null){
                        for(Report_item report_item:list){
                            if(report_item.getStatue_now().equals("未完成")){
                                //查找请求，Android发送请求，服务器端接收到请求后直接从待定表里查找，然后直接把表中的记录删掉

                            }
                        }
                    }
                }
            }
        }).start();
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
