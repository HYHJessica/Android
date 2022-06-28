package b19060209.iot.njupt.edu.cn.fighting;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
Button click_btn;
ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        click_btn = findViewById(R.id.main_btn);//关联
        imageView = findViewById(R.id.img_);
        click_btn.setOnClickListener(this);
        img_show();


    }

    /**
     *设置点击事件,从当前页面跳至登录页面
     */
    @Override
    public void onClick(View v){
        Log.i("MainActivity", "从初始页面进入登录页面:[MainActivity---->sign_in_Activity] ");
        Intent intent = new Intent(MainActivity.this,
                sign_in_Activity.class);
        startActivityForResult(intent, 1);

    }
    /**
     * 设置动画效果
     */
    public void img_show(){
        Animation alpha= AnimationUtils.loadAnimation(this,R.anim.alpha);
        imageView.startAnimation(alpha);
    }
}
