package AllService;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

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
}
