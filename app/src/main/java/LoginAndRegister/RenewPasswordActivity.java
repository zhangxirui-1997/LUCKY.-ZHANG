package LoginAndRegister;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

public class RenewPasswordActivity extends AppCompatActivity {

    private RelativeLayout relativeLayout_phone;
    private LinearLayout relativeLayout_check;
    private RelativeLayout relativeLayout_password;
    private RelativeLayout relativeLayout_passwordagain;
    private EditText editText_phone;
    private EditText editText_check;
    private EditText editText_password;
    private EditText editText_passwordagain;
    private String string_phone;
    private String string_check;
    private String string_password;
    private String getString_passwordagain;
    private Button button_check;
    private Button button_replace;
    private boolean judge=true;

    private boolean judge60s=true;
    private boolean judgeCheckisright=false;
    private RenewPasswordActivity.RepalacePasswordThread myThread;

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
    class RepalacePasswordThread implements Runnable{
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
                Toast.makeText(RenewPasswordActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                finish();
            }else if(msg.what==101){//手机号已经被注册
                Toast.makeText(RenewPasswordActivity.this, "未修改成功", Toast.LENGTH_SHORT).show();

            }else if(msg.what==102){//网络连接失败
                Toast.makeText(RenewPasswordActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_renew_password);
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
        relativeLayout_phone=findViewById(R.id.relativelayout2);
        relativeLayout_check=findViewById(R.id.relativelayout5);
        relativeLayout_password=findViewById(R.id.relativelayout3);
        relativeLayout_passwordagain=findViewById(R.id.relativelayout6);
        editText_phone=findViewById(R.id.editText);
        editText_check=findViewById(R.id.editText1);
        editText_password=findViewById(R.id.editText2);
        editText_passwordagain =findViewById(R.id.editText3);
        button_check=findViewById(R.id.button3);
        button_replace=findViewById(R.id.button2);

        button_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//获取验证码
                if(editText_phone.getText().toString()==null||editText_phone.getText().toString().equals("")){
                    Toast.makeText(RenewPasswordActivity.this, "输入手机号", Toast.LENGTH_SHORT).show();
                }else{
                    if(judge60s){
                        judge60s=false;
                        myThread=new RenewPasswordActivity.RepalacePasswordThread();
                        new Thread(myThread).start();
                        //先发验证码，1毫秒都不耽误
                        SMSSDK.getVerificationCode("86",editText_phone.getText().toString());
                        Toast.makeText(RenewPasswordActivity.this, "验证码已发送", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(RenewPasswordActivity.this, "请您耐心等待验证码", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        button_replace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(judge){
                    if(editText_phone.getText().toString()==null||editText_phone.getText().toString().equals("")){
                        Toast.makeText(RenewPasswordActivity.this, "输入注册的手机号", Toast.LENGTH_SHORT).show();
                    }else if(editText_check.getText().toString()==null||editText_check.getText().toString().equals("")){
                        Toast.makeText(RenewPasswordActivity.this, "验证码不能为空", Toast.LENGTH_SHORT).show();
                    }else{
                        SMSSDK.submitVerificationCode("86", editText_phone.getText().toString(),editText_check.getText().toString());
                        Toast.makeText(RenewPasswordActivity.this, "正在验证，请等待", Toast.LENGTH_SHORT).show();
                        try {
                            sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if(judgeCheckisright){
                            judge=false;
                            relativeLayout_phone.setVisibility(View.GONE);
                            relativeLayout_check.setVisibility(View.GONE);
                            relativeLayout_password.setVisibility(View.VISIBLE);
                            relativeLayout_passwordagain.setVisibility(View.VISIBLE);
                            button_replace.setText("修改密码");
                        }else{
                            Toast.makeText(RenewPasswordActivity.this, "验证码错误或网络差", Toast.LENGTH_SHORT).show();
                        }
                    }
                }else{
                    if(editText_password.getText().toString()==null||editText_password.getText().toString().equals("")
                    ||editText_passwordagain.getText().toString()==null||editText_passwordagain.getText().toString().equals("")){
                        Toast.makeText(RenewPasswordActivity.this, "输入框不能为空", Toast.LENGTH_SHORT).show();
                    }else if(!editText_password.getText().toString().equals(editText_passwordagain.getText().toString())){
                        Toast.makeText(RenewPasswordActivity.this, "两次输入密码不一致", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(RenewPasswordActivity.this, "正在修改", Toast.LENGTH_SHORT).show();
                        /**
                         *此处添加网络连接
                         */
                        RePassword();

                    }
                }
            }
        });

    }
    private void RePassword(){
        OkHttpClient client = new OkHttpClient();
        FormBody body = new FormBody.Builder()
                .add("User_phonenumber",editText_phone.getText().toString())
                .add("User_password",editText_password.getText().toString())
                .build();
        Request request = new Request.Builder()
                .url("http://123.57.235.123:8080/TheBestServe/ReplacePasswordServlet")
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

                    }
                }
            }
        });
    }
}
