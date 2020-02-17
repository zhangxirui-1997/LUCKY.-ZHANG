package LoginAndRegister;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luckzhang.R;


/**
 * 注意：有三个位置没有写：其中两个是短信验证码的，另一个是网络上传接口
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initialize();
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

            }
        });
        button_register=findViewById(R.id.button2);
        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//点击注册按钮
                if(judge()==1){//正常情况

                }else if(judge()==2){//有空
                    Toast.makeText(RegisterActivity.this, "输入框不能为空", Toast.LENGTH_LONG).show();
                }else if(judge()==3){//验证码不正确
                    Toast.makeText(RegisterActivity.this, "验证码不正确，请重新获取", Toast.LENGTH_LONG).show();
                }else if(judge()==4){//两次密码不一致
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

    private int judge(){
        string_phone=editText_phone.getText().toString();
        string_check=editText_check.getText().toString();
        string_password=editText_password.getText().toString();
        string_passwordagain=editText_passwordagain.getText().toString();
        if(string_phone==null||string_phone.equals("")||string_check==null||string_check.equals("")||
        string_password==null||string_password.equals("")||string_passwordagain==null||string_passwordagain.equals("")){
            return 2;
        }
        /**
         *此处添加验证码验证环节，如有异常return 3；
         */
        if(!string_password.equals(string_passwordagain)){
            //两次验证码不正确
            return 4;
        }
        return 1;
    }

}
