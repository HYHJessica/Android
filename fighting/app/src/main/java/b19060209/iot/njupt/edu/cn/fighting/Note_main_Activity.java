package b19060209.iot.njupt.edu.cn.fighting;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class Note_main_Activity extends AppCompatActivity {
    ListView listView;
    List<NotepadBean> list;
    SQLiteHelper mSQLiteHelper;
    NotepadAdapter adapter;
    TextView url_main;
    String flag = "daily_flag";
    b19060209.iot.njupt.edu.cn.fighting.countdown_circle time_show;//点击进入
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_main_);
        //用于显示便签的列表
        listView = (ListView) findViewById(R.id.listview);
        time_show = findViewById(R.id.time_);
        url_main = findViewById(R.id.url_main);
        url_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Note_main_Activity.this,
                        url_main_Activity.class);
                startActivity(intent);
            }
        });
                //点击倒计时图案后进入倒计时页面
        time_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Note_main_Activity.this,
                        countdown_Activity.class);
                startActivityForResult(intent, 1);
            }
        });
        ImageView add = (ImageView) findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Note_main_Activity.this,
                        RecordActivity.class);
                intent.putExtra("flag",flag);//在创建新的记录的时候传入flag,此时是在主页面创建的，故传入daily_flag
                startActivityForResult(intent, 1);
            }
        });
        Log.i("测试", "在主页面 ");
        initData();
        Log.i("测试", "初始化结束");

    }
    /**
     * initData()初始化页面，创建数据库，并预设一些点击操作函数
     */
    protected void initData() {
        Log.i("测试", "进入initData ");
        mSQLiteHelper= new SQLiteHelper(this); //创建数据库
        Log.i("测试", "创建数据库");
        showQueryData();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,View view,int position,long id){
                NotepadBean notepadBean = list.get(position);
                Intent intent = new Intent(Note_main_Activity.this, RecordActivity.class);
                intent.putExtra("id", notepadBean.getId());
                intent.putExtra("time", notepadBean.getNotepadTime()); //记录的时间
                intent.putExtra("content", notepadBean.getNotepadContent()); //记录的内容
                intent.putExtra("flag",flag);
                Note_main_Activity.this.startActivityForResult(intent, 1);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int
                    position, long id) {
                AlertDialog dialog;
                AlertDialog.Builder builder = new AlertDialog.Builder( Note_main_Activity.this)
                        .setMessage("是否删除此事件？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                NotepadBean notepadBean = list.get(position);
                                if(mSQLiteHelper.deleteData(notepadBean.getId())){
                                    list.remove(position);
                                    adapter.notifyDataSetChanged();
                                    Toast.makeText(Note_main_Activity.this,"删除成功",
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
    /**
    *showQueryData()读取数据库中的数据，并显示概要信息
     */
    private void showQueryData(){
        Log.i("测试", "进入数据显示函数 ");
        if (list!=null){
            list.clear();
            Log.i("测试", "清除之前列表残留的脏数据 ");
        }
        //从数据库中查询数据(保存的标签)
        Log.i("测试", "准备从数据库中查询数据 ");
        list = mSQLiteHelper.query(flag);
        adapter = new NotepadAdapter(this, list);
        listView.setAdapter(adapter);
    }
    @Override
    /**
    * onActivityResult(),如果是从recordactivity页面返回的，那么就显示记录
    * */
    protected void onActivityResult(int requestCode,int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1&&resultCode==2){
            showQueryData();
        }
    }
}
