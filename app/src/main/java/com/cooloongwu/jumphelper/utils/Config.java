package com.cooloongwu.jumphelper.utils;

import android.os.Environment;

import java.io.File;

/**
 * 一些简单的配置
 * Created by CooLoongWu on 2018-1-4 14:10.
 */

public class Config {

    public static String IMG_PATH = Environment.getExternalStorageDirectory() + File.separator;
    public static String IMG_NAME = "jumphelper.png";

    public static String CMD_SCREEN_SHOT = "screencap -p /sdcard/" + IMG_NAME;
    public static String CMD_TOUCH_LONG = "input swipe 100 touchY 300 touchY time";
}
