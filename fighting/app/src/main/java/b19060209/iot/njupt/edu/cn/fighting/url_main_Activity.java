package b19060209.iot.njupt.edu.cn.fighting;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class url_main_Activity extends AppCompatActivity implements View.OnClickListener{
TextView url_1;
TextView url_2;
ListView listView;
SQLiteHelper mSQLiteHelper_url;
List<NotepadBean> list;
NotepadAdapter adapter;
String flag = "url_flag";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_url_main_);
        url_1 = findViewById(R.id.url_1);
        url_1.setOnClickListener(this);
        url_2 = findViewById(R.id.url_2);
        url_2.setOnClickListener(this);
        listView = (ListView) findViewById(R.id.listview_url);
        Toast.makeText(this, R.string.www, Toast.LENGTH_SHORT).show();
        initData();
    }

    private void initData() {
        mSQLiteHelper_url
                = new SQLiteHelper(this); //创建数据库
        showQueryData_url();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,View view,int position,long id){
                NotepadBean notepadBean = list.get(position);
                Intent intent = new Intent(url_main_Activity.this, url_3_Activity.class);
                String content = notepadBean.getNotepadContent();
                int index_ = notepadBean.getNotepadContent().indexOf("|")+1;
                String url_ = content.substring(index_);
                Log.i("content", "onItemClick: content[]"+url_);
                intent.putExtra("url_", url_);
                url_main_Activity.this.startActivityForResult(intent, 1);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int
                    position, long id) {
                AlertDialog dialog;
                AlertDialog.Builder builder = new AlertDialog.Builder( url_main_Activity.this)
                        .setMessage("是否删除此事件？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                NotepadBean notepadBean = list.get(position);
                                if(mSQLiteHelper_url.deleteData(notepadBean.getId())){
                                    list.remove(position);
                                    adapter.notifyDataSetChanged();
                                    Toast.makeText(url_main_Activity.this,"删除成功",
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


    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.url_1:
            {
                Intent intent = new Intent(url_main_Activity.this,
                    url_1_Activity.class);
                startActivityForResult(intent, 2);
                break;
            }
            case R.id.url_2:
            {
                Intent intent = new Intent(url_main_Activity.this,
                        url_2_Activity.class);
                startActivityForResult(intent, 2);
                break;
            }

        }
    }

    private void showQueryData_url() {
        if (list!=null){
            list.clear();
        }
        //从数据库中查询数据(保存的标签)
        list = mSQLiteHelper_url.query(flag);
        adapter = new NotepadAdapter(this, list);
        Log.i("111", "showQueryData: omygod");
        listView.setAdapter(adapter);
    }
    @Override
    /**
     * 如果是从url_1_activity页面返回的，那么就显示记录
     * */
    protected void onActivityResult(int requestCode,int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==2&&resultCode==3){
            showQueryData_url();
        }
    }

}
