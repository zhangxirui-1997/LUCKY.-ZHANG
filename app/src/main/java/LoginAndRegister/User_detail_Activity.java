package LoginAndRegister;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luckzhang.R;

import org.litepal.LitePal;

import Data_Class.User_Info;

public class User_detail_Activity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView textView;
    private EditText editText;
    private Button button;
    private String string;
    private User_Info user_info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail_);
        toolbar=findViewById(R.id.toolbar22);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
