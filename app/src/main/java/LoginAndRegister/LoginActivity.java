package LoginAndRegister;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luckzhang.R;

public class LoginActivity extends AppCompatActivity {

    private String string_phone;
    private String string_password;
    private EditText editText_phone;
    private EditText editText_password;
    private TextView textView_forgetpassword;
    private Button button_login;
    private Button button_register;

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

    }

}
