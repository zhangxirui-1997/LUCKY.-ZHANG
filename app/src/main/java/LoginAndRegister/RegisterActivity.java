package LoginAndRegister;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luckzhang.R;

import java.io.IOException;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static java.lang.Thread.sleep;


/**
 * 注意：有三个位置没有写：短信的写完了，还差一个是网络上传接口
 */

public class RegisterActivity extends AppCompatActivity {

    private EditText editText_phone;
    private EditText editText_password;
    private EditText editText_passwordagain;
    private EditText editText_check;
    private TextView textView_rememberPassword;
    private Button button_check;
    private Button button_register;
    private String string_phone;
    private String string_password;
    private String string_passwordagain;
    private String string_check;

    private boolean judge60s=true;
    private boolean judgeCheckisright=false;
    private RegisterActivity.ThisThread myThread;

    private EventHandler eh=new EventHandler(){
        @Override
        public void afterEvent(int event, int result, Object data) {
            if (result == SMSSDK.RESULT_COMPLETE) {
                //回调完成
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    judgeCheckisright=true;
                    //提交验证码成功
                }else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                    //获取验证码成功
                }else if (event ==SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){
                    //返回支持发送验证码的国家列表
                }
            }else{
                ((Throwable)data).printStackTrace();
            }
        }
    };

    //子线程60s倒数
    //3秒倒数子线程
    class ThisThread implements Runnable{
        @Override
        public void run() {
            for(int i=60;i>=0;i--){
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Message message=new Message();
                message.what=i;
                handler1.sendMessage(message);
            }
        }
    }

    private Handler handler1=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            if(msg.what<=60){
                button_check.setText("等待"+msg.what+"s");
                if(msg.what==0){
                    judge60s=true;
                    button_check.setText("获取验证码");
                }
            }else if(msg.what==100){//注册成功
                Toast.makeText(RegisterActivity.this, "注册成功,返回登录", Toast.LENGTH_SHORT).show();
                finish();
            }else if(msg.what==101){//手机号已经被注册
                Toast.makeText(RegisterActivity.this, "手机号已注册", Toast.LENGTH_SHORT).show();

            }else if(msg.what==102){//网络连接失败
                Toast.makeText(RegisterActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initialize();
    }

    @Override
    protected void onStart() {
        Log.d("Activity_process_report","注册短信验证已经注册");
        SMSSDK.registerEventHandler(eh); //注册短信回调
        judgeCheckisright=false;
        super.onStart();
    }

    @Override
    protected void onStop() {
        SMSSDK.unregisterEventHandler(eh);
        Log.d("Activity_process_report","注册短信验证已经销毁");
        super.onStop();
    }

    private void initialize(){
        editText_phone=findViewById(R.id.editText);
        editText_check=findViewById(R.id.editText1);
        editText_password=findViewById(R.id.editText2);
        editText_passwordagain=findViewById(R.id.editText3);
        button_check=findViewById(R.id.button3);
        button_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//获取验证码
                if(editText_phone.getText().toString()==null||editText_phone.getText().toString().equals("")){
                    Toast.makeText(RegisterActivity.this, "输入手机号", Toast.LENGTH_SHORT).show();
                }else{
                    if(judge60s){
                        //先发验证码，1毫秒都不耽误
                        SMSSDK.getVerificationCode("86",editText_phone.getText().toString());
                        Toast.makeText(RegisterActivity.this, "验证码已发送", Toast.LENGTH_LONG).show();
                        judge60s=false;
                        myThread=new ThisThread();
                        new Thread(myThread).start();
                    }else{
                        Toast.makeText(RegisterActivity.this, "请您耐心等待验证码", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        button_register=findViewById(R.id.button2);
        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//点击注册按钮
                int i=judge();
                if(i==1){//正常情况
                    Toast.makeText(RegisterActivity.this, "注册中", Toast.LENGTH_LONG).show();
                    //此处进行网络验证
                    RegisterUpData();

                }else if(i==2){//有空
                    Toast.makeText(RegisterActivity.this, "输入框不能为空", Toast.LENGTH_LONG).show();
                }else if(i==3){//验证码不正确
                    Toast.makeText(RegisterActivity.this, "验证码错误或网络差 请重试", Toast.LENGTH_LONG).show();
                }else if(i==4){//两次密码不一致
                    Toast.makeText(RegisterActivity.this, "两次输入密码不一致", Toast.LENGTH_LONG).show();
                }
            }
        });
        textView_rememberPassword=findViewById(R.id.textView5);
        textView_rememberPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    //判断是否正确
    private int judge(){
        Log.d("RegisterActivity","1111111111111111111");
        SMSSDK.submitVerificationCode("86", editText_phone.getText().toString(),editText_check.getText().toString());
        Log.d("RegisterActivity","1111111111111111111");
        string_phone=editText_phone.getText().toString();
        string_check=editText_check.getText().toString();
        string_password=editText_password.getText().toString();
        string_passwordagain=editText_passwordagain.getText().toString();
        if(string_phone==null||string_phone.equals("")||string_check==null||string_check.equals("")||
        string_password==null||string_password.equals("")||string_passwordagain==null||string_passwordagain.equals("")){
            return 2;
        }
        //这里必须睡1s，不然子线程跑不过主线程
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        /**
         *此处添加验证码验证环节，如有异常return 3；
         */
        if(!judgeCheckisright){
            return 3;
        }
        if(!string_password.equals(string_passwordagain)){
            //两次密码不一致
            return 4;
        }
        return 1;
    }

    //进行网络注册
    private void RegisterUpData(){

        OkHttpClient client = new OkHttpClient();
        FormBody body = new FormBody.Builder()
                .add("User_phonenumber",editText_phone.getText().toString())
                .add("User_password",editText_password.getText().toString())
                .build();
        Request request = new Request.Builder()
                .url("http://192.168.43.96:8085/TheBestServe/RegisterServlet")
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //...
                Message message2=new Message();
                message2.what=102;
                handler1.sendMessage(message2);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    String result = response.body().string();
                    if(result.equals("no")){
                        Message message1=new Message();
                        message1.what=101;
                        handler1.sendMessage(message1);
                    }else{
                        Message message0=new Message();
                        message0.what=100;
                        handler1.sendMessage(message0);

                        /*
                         * 此处需要从json数据中获取信息填充到本地的数据库，先放一放，晚点写
                         * 因为一旦这里写了，调试起来可能就不那么容易了
                         *
                         *
                         *new:
                         * 这里只是负责云端注册，不负责本地数据库的填写，这里接触了直接返回登录界面
                         * */



                    }
                    //处理UI需要切换到UI线程处理
                }
            }
        });
    }

}
