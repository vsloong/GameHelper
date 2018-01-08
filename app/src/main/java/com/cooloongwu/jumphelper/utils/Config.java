package com.cooloongwu.jumphelper.utils;

import android.os.Environment;
import android.util.Log;

import com.cooloongwu.jumphelper.MyApplication;

import java.io.File;
import java.util.Random;

/**
 * 一些简单的配置
 * Created by CooLoongWu on 2018-1-4 14:10.
 */

public class Config {

    private static Config config;

    public static String IMG_PATH = Environment.getExternalStorageDirectory() + File.separator;
    public static String IMG_NAME = "jumphelper.png";

    public static String CMD_SCREEN_SHOT = "screencap -p /sdcard/" + IMG_NAME;

    public static Config getInstance() {
        if (config == null) {
            config = new Config();
        }
        return config;
    }

    /**
     * @return 获取[100, 200]之间随机的触摸区域，小于100的话有一个分享按钮可能会被点击
     */
    private int getTouchX() {
        return new Random().nextInt(200) + 100;
    }

    /**
     * @return 获取屏幕高度的85%以下的随机触摸区域
     */
    private int getTouchY() {
        return (MyApplication.getInstance().getScreenHeight() / 100 * (new Random().nextInt(16) + 85));
    }


    public String touchCMD(int time) {
        String CMD_TOUCH_LONG = "input swipe " + getTouchX() + " " + getTouchY() + " " + getTouchX() + " " + getTouchY() + " time";
        String cmd = CMD_TOUCH_LONG.replace("time", String.valueOf(time));
        Log.e("CMD", cmd);
        return cmd;
    }
}
