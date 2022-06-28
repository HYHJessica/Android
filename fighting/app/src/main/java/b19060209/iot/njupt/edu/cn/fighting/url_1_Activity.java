package b19060209.iot.njupt.edu.cn.fighting;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class url_1_Activity extends AppCompatActivity {
    SQLiteHelper mSQLiteHelper_1;
    String flag = "url_flag";
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置为全屏(隐藏状态栏)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_url_1_);
        this.createWebView();
        initData();

    }

    private void initData() {
        mSQLiteHelper_1= new SQLiteHelper(this); //创建数据库

    }


    /* 创建webview实例*/
    private void createWebView() {
        final WebView webView = (WebView) findViewById(R.id.webView);
        //允许webview执行javascrapt脚本
        webView.getSettings().setJavaScriptEnabled(true);
        //  确保跳转后仍在当前webview中显示
        webView.setWebViewClient(new WebViewClient());
        //加载研招网
        String url="https://yz.chsi.com.cn/";
        webView.loadUrl(url);// 指定要加载的网页，上网需要申请权限，否则会出现“net::ERR_CACHE_MISS”的错误

        ImageView add = (ImageView) findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String noteContent=webView.getTitle();
                String url_now = webView.getUrl();
                Log.i("inf", "onClick: url_ "+noteContent+"   "+url_now);//打印当前网页地址
                noteContent = noteContent+"|"+url_now;//两者合并显示在列表中，使用者能更清楚信息
                if (url_now != null){
                    //向数据库中添加数据
                    if (noteContent.length()>0){
                        if (mSQLiteHelper_1.insertData(noteContent, DBUtils.getTime(),flag)){
                            showToast("保存成功");
                            setResult(3);
                            Intent intent = new Intent(url_1_Activity.this,
                                    url_main_Activity.class);
                            setResult(3,intent);
                            finish();


                        }else {
                            showToast("保存失败");
                        }
                    }else {
                        showToast("未读取到网址");
                    }
                }

            }
        });


    }
    public void showToast(String message){
        Toast.makeText(url_1_Activity.this,message,Toast.LENGTH_SHORT).show();
    }

}
