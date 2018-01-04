package com.cooloongwu.jumphelper.utils;

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 工具类
 * <p>
 * Created by CooLoongWu on 2018-1-2 10:09.
 */

public class OSUtils {

    private volatile static OSUtils osUtils;
    private static OutputStream outputStream;

    public static OSUtils getInstance() {
        if (osUtils == null) {
            synchronized (OSUtils.class) {
                if (osUtils == null) {
                    osUtils = new OSUtils();
                }
            }
        }
        return osUtils;
    }

    private OSUtils() {
        initOS();
    }

    private void initOS() {
        if (outputStream == null) {
            try {
                outputStream = Runtime.getRuntime().exec("su").getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("OutputStream", "打开OS失败");
            }
        }
    }

    public void exec(String cmd) {
        try {
            initOS();
            outputStream.write((cmd + "\n").getBytes());
            outputStream.flush();
            Log.e("OutputStream", "执行命令成功");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("OutputStream", "执行命令失败");
        }
    }

    public void close() {
        try {
            if (outputStream != null)
                outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("OutputStream", "关闭OS失败");
        }
    }

    /**
     * 不能正常检测出是否Root
     *
     * @return boolean
     */
    private boolean isRoot() {
        boolean res = false;
        try {
            res = ((new File("/system/bin/su").exists()) &&
                    (new File("/system/xbin/su").exists()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }
}
