package b19060209.iot.njupt.edu.cn.fighting;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class countdown_circle extends View {
    private Paint paint; //画笔
    private int max=365;//最大进度
    private int rundwidth=20;//圆弧宽度
    private int measuredWidth;//当前画布宽度
    private int bigNumber = 0; //最大进度
    private int nowNumber= 56; //当前进度
    public countdown_circle(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    /**
     * 在使用时进行调用
     * 设置最大数字，也就是最大百分数,同时开启线程，动态加载View
     * */
    public void startView(int startNumber){
        bigNumber = startNumber;
        thread.start();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //创建画笔
        paint = new Paint();
        //防止画笔画出的画出现锯齿状毛边，影响美观
        paint.setAntiAlias(true);
        //测量当前画板宽度
        measuredWidth = getMeasuredWidth();
        //设置为空心圆
        paint.setStyle(Paint.Style.STROKE);
        //设置画笔宽度
        paint.setStrokeWidth(rundwidth);
        //设置画笔颜色，浅绿色
        paint.setColor(Color.parseColor("#bcc595"));
        //宽度
        float x = measuredWidth / 2;
        //高度
        float y = measuredWidth / 2;
        //半径
        int rd = measuredWidth / 2 - rundwidth / 2;
        /**
         *开始画画，画的是整个圆
         *第一个参数：圆心的x坐标
         *第二个参数：圆心的y坐标
         *第三个参数：圆的半径
         *第四个参数：绘制时所使用的画笔
         * */
        canvas.drawCircle(x, y, rd, paint);
        //画个圆弧模板
        RectF rectF = new RectF(rundwidth/2,rundwidth/2,measuredWidth-rundwidth/2,measuredWidth-rundwidth/2);
        //设置圆弧颜色，此时的画笔颜色就是该圆弧的颜色，深绿色#689581
        paint.setColor(Color.parseColor("#8c4646"));
        /**
         *第一个参数：为确定圆弧区域的矩形，圆弧的中心点为矩形的中心点
         *第二个参数：为圆弧的开始角度（时钟3点的方向为0度，顺时钟方向为正）
         *第三个参数：为圆弧的扫过角度（正数为顺时钟方向，负数为逆时钟方向）
         *第四个参数：表示绘制的圆弧是否与中心点连接成闭合区域
         *第五个参数：为绘制圆弧的画笔
         * */


        try
        {
            DateFormat df = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
            Date startTime = df.parse("2022年12月24日 12:10:10");
            Date endTime = df.parse(DBUtils.getTime());
            long diff_ = startTime.getTime() - endTime.getTime();
            Log.i("测试1", "onDraw: 时间差"+diff_);
            long day = diff_ / (1000 * 60 * 60 * 24);
            nowNumber = (int)day;
        }catch (Exception e)
        {
            Log.i("测试", "onDraw: something wrong："+e.getMessage());
        }
        canvas.drawArc(rectF,-90,nowNumber*360/max,false,paint);
        //设置第一个文字
        String oneText="倒计时"+'\n';
        //设置个文字模板
        Rect oneRect = new Rect();
        //将文字模板转移到画笔上，此时画笔的属性代表了该文字的属性
        paint.getTextBounds(oneText,0,oneText.length(),oneRect);
        //该画笔的宽度也就是该文字的宽度，因为文字已经在画笔上了
        paint.setStrokeWidth(0);
        //该画笔的大小，因为文字已经在画笔上了，所以就是该文字的大小
        paint.setTextSize(28);
        //设置画笔颜色，因为文字已经在画笔上了，所以就是该文字的颜色
        paint.setColor(Color.parseColor("#588c7e"));
        /**
         * 在画板上画出该文字
         * 第一个参数：相关文字
         * 第二个参数：该参数表示text被画的起始x坐标
         * 第三个参数：该参数表示text被画的起始y坐标
         * 第四个参数：画笔
         * 这里之所以用这个参数是因为我找了个固定的数据来设置相关参数，可根据情况自己设置
         * */
        canvas.drawText(oneText,measuredWidth/4-oneRect.width()/2,measuredWidth/2+oneRect.height()/2,paint);

        //设置第二个文字
        String twoText=nowNumber*100/max+"%";
        //设置文字模板
        Rect twoRect = new Rect();
        //将文字模板转移到画笔上，此时画笔的属性代表了该文字的属性
        paint.getTextBounds(twoText,0,twoText.length(),twoRect);
        //设置画笔宽度，也就是文字宽度
        paint.setStrokeWidth(0);
        //设置画笔字体大小，也就是该文字字体大小
        paint.setTextSize(40);
        //设置画笔颜色，也就是该文字的颜色
        paint.setColor(Color.parseColor("#f50000"));
        //把该文字画到画板上
        canvas.drawText(twoText,measuredWidth/2+twoRect.width()/4,measuredWidth/2+twoRect.height()/2,paint);

    }
    /**
     * 通过线程来动态设置View达到动画效果
     * */
    private Thread thread = new Thread(){
        @Override
        public void run() {
            while(true){
                try {
                    /**如果当前数字小于设置的最大数字就一直加*/
                    if (nowNumber < bigNumber){
                        nowNumber = nowNumber+1;
                    }
                    /**休息时间*/
                    Thread.sleep(70);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                /**
                 * 刷新界面 - 无需在UI线程，在工作线程即可被调用，invalidate()必须在UI线程
                 * */
                postInvalidate();
            }
        }
    };
}