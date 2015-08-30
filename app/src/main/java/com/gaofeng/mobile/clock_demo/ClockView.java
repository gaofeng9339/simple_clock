package com.gaofeng.mobile.clock_demo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.Calendar;

/**
 * Created by gaofeng on 15-8-29.
 */
public class ClockView extends View {




    public ClockView(Context context) {
        super(context);
        ringHourBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.ring);
        ringMinBitmap  = BitmapFactory.decodeResource(getResources(),R.drawable.ring_min);
        syncTime();
        setSchedualTime(0);

    }

    public ClockView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ClockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //半径和圆中心
    private static final float Radius = 200;
    private static final float CenterX = 250;
    private static final float CenterY = 250;

    private float secondsDegree = 0;
    private float minDegree = 0;
    private float hourDegree = 0;

    //定时
    private float setHourDegree;
    private float setMinDegree;
    private boolean setSchedual;
    private Bitmap ringHourBitmap;
    private Bitmap ringMinBitmap;
    private int bitmapWidth = 30,bitmapHeight = 30;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setBackgroundColor(Color.GREEN);
        drawCircle(canvas);
        drawClockPoint(canvas);
        drawIndicator(canvas);
        calc();
        drawSchedual(canvas);
        invalidate();
    }

    private void drawSchedual(Canvas canvas) {
        if (setSchedual) {
            Paint p = new Paint();

            //找到小时的坐标
            float rad = (float )Math.toRadians(setHourDegree);
            float x = (float) (Radius * Math.sin(rad)) ;
            float y = (float) (Radius * Math.cos(rad));


            float vx = CenterX + x;
            float vy = CenterY - y;
            //比较笨的方法解决 图片绘制在圆内。
            if (setHourDegree > 0 && setHourDegree <= 90) {
                vx = vx - bitmapWidth;
            }
            if (setHourDegree > 90 && setHourDegree < 180) {
                vx = vx - bitmapWidth;
                vy = vy - bitmapWidth;
            }
            if (setHourDegree > 180 && setHourDegree < 270) {
                vy = vy - bitmapWidth;
            }

            Log.d("","Range x:" + vx + " y:" + vy);
            canvas.drawBitmap(ringHourBitmap,vx ,vy ,p);

            //找到分钟的坐标
            rad = (float )Math.toRadians(setMinDegree);
            x = (float) (Radius * Math.sin(rad)) ;
            y = (float) (Radius * Math.cos(rad))  ;
             vx = CenterX + x;
             vy = CenterY - y;

            if (setMinDegree > 0 && setMinDegree <= 90) {
                vx = vx - bitmapWidth;
            }
            if (setMinDegree > 90 && setMinDegree < 180) {
                vx = vx - bitmapWidth;
                vy = vy - bitmapWidth;
            }
            if (setMinDegree > 180 && setMinDegree < 270) {
                vy = vy - bitmapWidth;
            }

            canvas.drawBitmap(ringMinBitmap,vx,vy ,p);

            p = null;


        }
    }




    private void calc() {
        secondsDegree = secondsDegree + 6;
        if (secondsDegree >=360) { //一圈为一分钟
            secondsDegree = 0;
            minDegree = minDegree + 6;
        }
        if (minDegree >= 360) { //一圈为一个小时
            minDegree = 0;
            hourDegree = hourDegree + 6;
        }
        if (hourDegree >= 360) { //一圈还是从0度开始
            hourDegree = 0;
        }
    }

    //画圆
    private void drawCircle(Canvas canvas) {

        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStrokeWidth(2);
        canvas.drawCircle(CenterX,CenterY,Radius,paint);

        Paint paint2 = new Paint();
        paint2.setColor(Color.YELLOW);
        paint2.setStrokeWidth(15f);
        canvas.drawPoint(CenterX,CenterY,paint2);

    }

    private void drawClockPoint(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.YELLOW);
        paint.setStrokeWidth(10f);
        //画出闹钟上面的刻度值，一圈一共12个点, 360度
        for (float degree = 0; degree <= 330;degree = degree + 30) {
            //数学公式 找圆上面的点坐标
            float rad = (float )Math.toRadians(degree);//转换为度
            float x = (float) (Radius * Math.sin(rad));
            float y = (float) (Radius * Math.cos(rad));
            canvas.drawPoint(x + CenterX, CenterY - y,paint);
        }
    }

    //画指针,转的慢的指针越厚，默认都从0开始转
    private void drawIndicator(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(3f);
        //秒针
        _drawLine(canvas,secondsDegree,paint);

        //分针
        paint.setColor(Color.DKGRAY);
        paint.setStrokeWidth(6f);
        _drawLine(canvas,minDegree,paint);


        //时针
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(12f);
        _drawLine(canvas,hourDegree,paint);
    }


    private void _drawLine(Canvas canvas,float degree,Paint paint) {
        float rad = (float )Math.toRadians(degree);//转换为度
        float x = (float) (Radius * Math.sin(rad));
        float y = (float) (Radius * Math.cos(rad));
        canvas.drawLine(CenterX, CenterY, x + CenterX, CenterY - y, paint);
    }

    public void setSchedualTime(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        //演示一下
        int hour     = 14;
        int min      = 35;
        if (hour > 12) { //是24小时类型的
            hour = hour - 12;
        }
        //计算和前面一样
        setHourDegree = hour * 30;
        setMinDegree  = min * 6;
        setSchedual = true;
    }


    //和手机时间同步一下
    public void syncTime() {
        Calendar cal = Calendar.getInstance();
        int hour     = cal.get(Calendar.HOUR_OF_DAY);
        int min      = cal.get(Calendar.MINUTE);
        int second   = cal.get(Calendar.SECOND);
        Log.d("","Time Now Hour:" + hour + " Min:" + min + " second:" + second);

        if (hour > 12) { //是24小时类型的
            hour = hour - 12;
        }
        //计算弧度 每个步伐都是 60/360 = 6度,Hour 12/360 度
        hourDegree = hour * 30;
        minDegree  = min * 6;
        secondsDegree = second * 6;
    }


}
