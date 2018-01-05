package com.cooloongwu.jumphelper;

import android.app.Application;
import android.content.Context;
import android.graphics.PixelFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.cooloongwu.jumphelper.view.ManualFloatView;

/**
 * 自定义Application，来实现简单临时的数据缓存
 * Created by CooLoongWu on 2018-1-5 10:02.
 */

public class MyApplication extends Application {

    private static MyApplication myApplication;

    //各屏幕速度配置（单位：px/ms）
    private final double SPEED_720P = 0.4851;
    private final double SPEED_1080P = 0.7179;
    private final double SPEED_2K = 1.7114;

    private int screenWidth;
    private int screenHeight;
    private double speed;

    private WindowManager windowManager;
    private WindowManager.LayoutParams params = new WindowManager.LayoutParams();

    public static MyApplication getInstance() {
        return myApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        setScreenHeight(displayMetrics.heightPixels);
        setScreenWidth(displayMetrics.widthPixels);

        Log.e("屏幕分辨率", getScreenWidth() + " * " + getScreenHeight());

        initWindowManager();
    }

    /**
     * 初始化悬浮窗
     */
    private void initWindowManager() {
        windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

        params.format = PixelFormat.RGBA_8888;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        params.alpha = 0.8f;
        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
    }

    /**
     * 设置悬浮窗的宽高参数
     *
     * @param width  宽
     * @param height 高
     */
    private void setWindowParams(int width, int height, int gravity) {
        params.gravity = gravity;
        params.width = width;
        params.height = height;
    }

    /**
     * 开启悬浮窗
     *
     * @param floatView 悬浮视图
     */
    public void attach(View floatView) {
        if (null != floatView && floatView.getParent() == null) {
            if (floatView instanceof ManualFloatView) {
                setWindowParams(
                        getScreenWidth(),
                        (int) (getScreenHeight() * 0.8),
                        Gravity.START | Gravity.TOP);
            } else {
                setWindowParams(
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        Gravity.END | Gravity.BOTTOM);
            }
            windowManager.addView(floatView, params);
        }
    }

    /**
     * 关闭悬浮窗
     *
     * @param floatView 悬浮视图
     */
    public void detach(View floatView) {
        try {
            if (null != floatView)
                windowManager.removeViewImmediate(floatView);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("悬浮窗", "关闭失败");
        }
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public void setScreenWidth(int screenWidth) {
        if (screenWidth == 720) {
            setSpeed(SPEED_720P);
        } else if (screenWidth == 1080) {
            setSpeed(SPEED_1080P);
        } else {
            setSpeed(SPEED_2K);
        }
        this.screenWidth = screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        Log.e("该分辨率下速度", "" + speed);
        this.speed = speed;
    }
}
