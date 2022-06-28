package b19060209.iot.njupt.edu.cn.fighting;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class RecordActivity extends Activity implements View.OnClickListener {
    ImageView note_back;
    TextView note_time;
    TextView deadline;
    EditText content;
    ImageView delete;
    ImageView note_save;
    SQLiteHelper mSQLiteHelper;
    TextView noteName;
    String id;
    String deadline_string;
    String flag ;//表明这是一条类型是“日常”的记录
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        note_back = (ImageView) findViewById(R.id.note_back);
        note_time = (TextView)findViewById(R.id.tv_time);
        deadline = (TextView)findViewById(R.id.tv_deadline);
        content = (EditText) findViewById(R.id.note_content);
        delete = (ImageView) findViewById(R.id.delete);
        note_save = (ImageView) findViewById(R.id.note_save);
        noteName = (TextView) findViewById(R.id.note_name);
        note_back.setOnClickListener(this);
        delete.setOnClickListener(this);
        note_save.setOnClickListener(this);
        initData();
    }
    protected void initData() {
        mSQLiteHelper = new SQLiteHelper(this);
        noteName.setText("添加记录");
        Intent intent = getIntent();
        flag = intent.getStringExtra("flag");
        Log.i("测试", "initData: "+flag);
        if(intent!= null){
            id = intent.getStringExtra("id");
            if (id != null){
                noteName.setText("修改记录");
                content.setText(intent.getStringExtra("content"));
                note_time.setText("创立时间："+intent.getStringExtra("time"));
                note_time.setVisibility(View.VISIBLE);

            }
            if (flag.equals("countdown_flag")){//如果是在倒计时页面进入
                deadline_string = intent.getStringExtra("deadline");
                Log.i("测试", "由倒计时页面进入 ,查看intent中的deadline是否正常：  "+deadline_string);
                deadline.setText("目标日期："+deadline_string);
                deadline.setVisibility(View.VISIBLE);//设置截止日期可见
            }
        }


    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.note_back:
                finish();
                break;
            case R.id.delete:
                content.setText("");
                break;
            case R.id.note_save:
                String noteContent=content.getText().toString().trim();
                if (id != null){//修改操作
                    if (noteContent.length()>0) {
                        if (flag.equals("countdown_flag")) {
                            if (mSQLiteHelper.updateData_deadline(id, noteContent, DBUtils. getTime(),deadline_string,flag)) {
                                showToast("修改成功");
                                setResult(4);
                                finish();
                            } else {
                                showToast("修改失败");
                            }
                        } else
                            {
                            if (mSQLiteHelper.updateData(id, noteContent, DBUtils.getTime(), flag)) {
                            showToast("修改成功");
                            switch (flag) {
                                case "daily_flag":
                                    setResult(2);
                                    finish();
                                case "url_flag":
                                    setResult(3);
                                    finish();
                                case "countdown_flag":
                                    setResult(4);
                                    finish();
                            }
                        } else {
                            showToast("修改失败");
                        }
                    }
                    }else {
                        showToast("修改内容不能为空!");
                    }
                }else {
                    //向数据库中添加数据
                    if (noteContent.length()>0) {
                        if (flag.equals("countdown_flag")) {
                            if (mSQLiteHelper.insertData_deadline(noteContent, DBUtils.getTime(),deadline_string,flag)) {
                                showToast("保存成功");
                                setResult(4);
                                finish();
                            } else {
                                showToast("保存失败");
                            }
                        } else {
                            if (mSQLiteHelper.insertData(noteContent, DBUtils.getTime(),flag)) {
                                showToast("保存成功");
                                switch (flag) {
                                    case "daily_flag":
                                        setResult(2);
                                        finish();
                                    case "url_flag":
                                        setResult(3);
                                        finish();
                                    case "countdown_flag":
                                        setResult(4);
                                        finish();
                                }

                            } else {
                                showToast("保存失败");
                            }
                        }
                        }else{
                            showToast("修改内容不能为空!");
                        }

                }
                break;
        }
    }
    public void showToast(String message){
        Toast.makeText(RecordActivity.this,message,Toast.LENGTH_SHORT).show();
    }
}
