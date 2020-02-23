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

import com.example.luckzhang.MainActivity;
import com.example.luckzhang.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import Data_Class.User_Info;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    private String string_phone;
    private String string_password;
    private EditText editText_phone;
    private EditText editText_password;
    private TextView textView_forgetpassword;
    private Button button_login;
    private Button button_register;

    private Handler LoginHandler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            if(msg.what==-1){//登录失败
                Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
            }else if(msg.what==1){//登录成功
                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                Intent intenttoMain=new Intent();
                intenttoMain.setClass(LoginActivity.this, MainActivity.class);
                startActivity(intenttoMain);
                finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initialize();
    }

    //各参数初始化
    private void initialize(){
        editText_phone=findViewById(R.id.editText);
        editText_password=findViewById(R.id.editText2);
        textView_forgetpassword=findViewById(R.id.textView5);
        button_login=findViewById(R.id.button3);
        button_register=findViewById(R.id.button2);

        textView_forgetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToRe=new Intent();
                intentToRe.setClass(LoginActivity.this,RenewPasswordActivity.class);
                startActivity(intentToRe);
            }
        });
        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentRegis=new Intent();
                intentRegis.setClass(LoginActivity.this,RegisterActivity.class);
                startActivity(intentRegis);
            }
        });
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(judge()){//如果此处判断合法，则开始进行上传数据
                    updata();
                }else{
                    Toast.makeText(LoginActivity.this, "输入框不能为空", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    //判断输入是否合法（是否为空）
    private boolean judge(){
        string_password=editText_password.getText().toString();
        string_phone=editText_phone.getText().toString();
        if(string_phone==null||string_phone.equals("")||
                string_password==null||string_password.equals("")){
            return false;
        }
        return true;
    }

    //上传数据并获取回执
    private void updata(){
        /*loginThread=new LoginThread();
        new Thread(loginThread).start();*/
        Log.d("LoginActivity","111111111111111111");
        OkHttpClient client = new OkHttpClient();
        Log.d("LoginActivity","111111111111122222");
        FormBody body = new FormBody.Builder()
                .add("User_phonenumber",editText_phone.getText().toString())
                .add("User_password",editText_password.getText().toString())
                .build();
        Log.d("LoginActivity","1111111111111333333");
        Request request = new Request.Builder()
                .url("http://192.168.43.96:8085/TheBestServe/LoginServlet")
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //...
                Message message=new Message();
                message.what=-1;
                LoginHandler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    String result = response.body().string();
                    if(result.equals("no")){
                        Message message=new Message();
                        message.what=-1;
                        LoginHandler.sendMessage(message);
                    }else{
                        /*
                        * 此处需要从json数据中获取信息填充到本地的数据库，先放一放，晚点写
                        * 因为一旦这里写了，调试起来可能就不那么容易了
                        * */
                        Message message2=new Message();
                        message2.what=1;
                        LoginHandler.sendMessage(message2);

                        //string转json,再转到本地数据库中
                        JSONObject re= null;
                        LitePal.deleteAll(User_Info.class);
                        try {
                            re = new JSONObject(result);
                            User_Info user_info=new User_Info();
                            user_info.setUser_phonenumber((String) re.get("User_phonenumber"));
                            user_info.setUser_fakename((String) re.get("User_fakename"));
                            user_info.setUser_sex((String) re.get("User_sex"));
                            user_info.setUser_Reallyname((String) re.get("User_reallyname"));
                            user_info.setUser_age(Integer.parseInt((String) re.get("User_age")));
                            user_info.setUser_birthday((String) re.get("User_birthday"));
                            user_info.setUser_useDay((String) re.get("User_useday"));

                            user_info.setUser_five_fen((String)re.getString("User_five_fen"));
                            user_info.setUser_five_ci((String)re.getString("User_five_ci"));
                            user_info.setUser_five_zheng((String)re.getString("User_five_zheng"));
                            user_info.setUser_five_yi((String)re.getString("User_five_yi"));
                            user_info.setUser_five_yu((String)re.getString("User_five_yu"));
                            user_info.save();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    //处理UI需要切换到UI线程处理
                }
            }
        });
    }
}
