package b19060209.iot.njupt.edu.cn.fighting;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class sign_in_Activity extends AppCompatActivity implements View.OnClickListener {
    Button btn_sign_in;
    EditText text_account;
    EditText text_code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_);
        btn_sign_in = findViewById(R.id.asi_btn_sign_in);
        text_account = findViewById(R.id.asi_et_account);
        text_code = findViewById(R.id.asi_et_code);
        btn_sign_in.setOnClickListener(this);
    }
    @Override
    public void onClick(View v){
        String account = "hyh";
        String code ="123456";
        Log.i("sign_in_Activity", "1从登录页面进入主页面:[sign_in_Activity---->Note_main_Activity] ");
        if(text_account.getText().toString().equals(account))
        {

//            Log.i("sign_in_Activity", text_account.getText().toString()+"==="+text_code.getText().toString());
            if (text_code.getText().toString().equals(code))
            {
                Toast.makeText(this, R.string.sign_in_succrss, Toast.LENGTH_SHORT).show();
                Log.i("sign_in_Activity", "从登录页面进入主页面:[sign_in_Activity---->Note_main_Activity] ");
                Intent intent = new Intent(sign_in_Activity.this,
                        Note_main_Activity.class);
                startActivity(intent);
                finish();
            }
            else {
                Toast.makeText(this, R.string.sign_in_wrong, Toast.LENGTH_SHORT).show();
                text_code.setText("");
            }
        }
    }
}
