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

public class OnlyService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            DownloadWelcomeImg();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    //下载欢迎界面图片的后台服务中的子线程
    private void DownloadWelcomeImg() throws IOException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient okHttpClient = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("http://123.57.235.123:8080/img/WelcomImg.jpg")
                        .build();
                Response response = null;
                try {
                    response = okHttpClient.newCall(request).execute();
                    byte[] bytes = new byte[0];
                    bytes = response.body().bytes();
                    String path=Environment.getExternalStorageDirectory().getAbsolutePath()+"/aphysique/data/resources";
                    if(new File(path).mkdirs()){
                        Log.d("OnlyService","1111111111创建目录成功");

                    }else{
                        Log.d("OnlyService","111111111创建目录失败");
                    }
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
