package b19060209.iot.njupt.edu.cn.fighting;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class url_3_Activity extends AppCompatActivity {
    SQLiteHelper mSQLiteHelper_3;
    String flag = "url_flag";
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置为全屏(隐藏状态栏)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_url_3_);
        this.createWebView();
        initData();

    }

    private void initData() {
        mSQLiteHelper_3= new SQLiteHelper(this); //创建数据库
    }


    /* 创建webview实例*/
    private void createWebView() {
        final WebView webView = (WebView) findViewById(R.id.webView_3);
        //允许webview执行javascrapt脚本
        webView.getSettings().setJavaScriptEnabled(true);
        //  确保跳转后仍在当前webview中显示
        webView.setWebViewClient(new WebViewClient());
        //通传入的网址加载网页
        Intent intent = getIntent();
        String url_ = intent.getStringExtra("url_");
        String url= url_;
        Log.i("url_", "createWebView: url_"+url_);
        webView.loadUrl(url);// 指定要加载的网页，上网需要申请权限，否则会出现“net::ERR_CACHE_MISS”的错误





    }
    public void showToast(String message){
        Toast.makeText(url_3_Activity.this,message,Toast.LENGTH_SHORT).show();
    }

}
