package LoginAndRegister;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luckzhang.R;

import org.litepal.LitePal;

import Data_Class.User_Info;

public class User_detail_Activity extends AppCompatActivity {
    private TextView textView;
    private EditText editText;
    private Button button;
    private String string;
    private User_Info user_info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail_);
        textView=findViewById(R.id.textView17);
        editText=findViewById(R.id.editText4);
        user_info= LitePal.findFirst(User_Info.class);
        editText.setText(user_info.getUser_fakename());
        button=findViewById(R.id.buttonchange);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                string =editText.getText().toString();
                if(string==null||string.equals("")){
                    Toast.makeText(User_detail_Activity.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
                }else{

                    user_info.setUser_fakename(string);
                    user_info.save();
                    Toast.makeText(User_detail_Activity.this, "用户名修改成功", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }
}
