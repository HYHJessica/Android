package b19060209.iot.njupt.edu.cn.fighting;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class countdown_Activity extends AppCompatActivity implements View.OnClickListener{
    ImageView note_back;
    TextView deadline;
    CalendarView calendarView;
    ListView listView;
    List<NotepadBean> list;
    SQLiteHelper mSQLiteHelper_time;
    NotepadAdapter adapter;
    String flag = "countdown_flag";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown_);
        note_back = (ImageView) findViewById(R.id.note_back);
        deadline = findViewById(R.id.tv_deadline);
        listView = (ListView) findViewById(R.id.listview_countdown);
        calendarView = (CalendarView) findViewById(R.id.calenderView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange( CalendarView view, int year, int month, int dayOfMonth) {
                //显示用户选择的日期
                Toast.makeText(countdown_Activity.this,year + "年" + (month+1) + "月" + dayOfMonth + "日",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(countdown_Activity.this,
                        RecordActivity.class);
                String deadline_string = year + "年" + (month+1) + "月" + dayOfMonth + "日";
                intent.putExtra("deadline",deadline_string);
                intent.putExtra("flag",flag);//在创建新的记录的时候传入flag,此时是在主页面创建的，故传入countdown_flag
                startActivityForResult(intent, 1);
            }
        });
        note_back.setOnClickListener(this);
        Toast.makeText(this, R.string.countdown, Toast.LENGTH_SHORT).show();
        initData();

    }
    /**
     * initData(),初始化界面，设置一些操作函数
     */
    private void initData() {
        mSQLiteHelper_time
                = new SQLiteHelper(this); //创建数据库
        showQueryData();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,View view,int position,long id){
                NotepadBean notepadBean = list.get(position);
                Intent intent = new Intent(countdown_Activity.this, RecordActivity.class);

                String content = notepadBean.getNotepadContent();
                intent.putExtra("id", notepadBean.getId());
                intent.putExtra("time", notepadBean.getNotepadTime()); //记录的时间
                intent.putExtra("content", notepadBean.getNotepadContent()); //记录的内容
                intent.putExtra("flag", flag);
                intent.putExtra("deadline",notepadBean.getDeadline());
                Log.i("测试", "从倒计时页面点击倒计时事务，查看deadline是否读取正常  "+deadline);
                countdown_Activity.this.startActivityForResult(intent, 1);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int
                    position, long id) {
                AlertDialog dialog;
                AlertDialog.Builder builder = new AlertDialog.Builder( countdown_Activity.this)
                        .setMessage("是否删除此事件？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                NotepadBean notepadBean = list.get(position);
                                if(mSQLiteHelper_time.deleteData(notepadBean.getId())){
                                    list.remove(position);
                                    adapter.notifyDataSetChanged();
                                    Toast.makeText(countdown_Activity.this,"删除成功",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                dialog =  builder.create();
                dialog.show();
                return true;
            }
        });
    }
    /*
     * onclick(),点击事件，如果点击返回按钮，则结束当前进程，返回主界面
     */
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.note_back:
                finish();
                break;
        }
    }
    /*
    showQueryData()读取数据库中的数据，并根据flag判断读取相关信息，最后显示概要信息
     */
    private void showQueryData(){
        if (list!=null){
            list.clear();
        }
        //从数据库中查询数据(保存的标签)
        list = mSQLiteHelper_time.query(flag);

        adapter = new NotepadAdapter(this, list);
        listView.setAdapter(adapter);
    }
    /**
     * onActivityResult(),如果是从recordactivity页面返回的，那么就显示记录
     * */
    protected void onActivityResult(int requestCode,int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1&&resultCode==4){
            showQueryData();
        }
    }
}
