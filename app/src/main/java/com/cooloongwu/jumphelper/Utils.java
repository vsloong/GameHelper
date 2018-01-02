package com.cooloongwu.jumphelper;

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 工具类
 * <p>
 * Created by CooLoongWu on 2018-1-2 10:09.
 */

public class Utils {

    private static OutputStream outputStream;

    public static void exec(String cmd) {
        try {
            if (outputStream == null) {
                outputStream = Runtime.getRuntime().exec("su").getOutputStream();
            }
            Log.e("OutputStream", "已实例化");
            outputStream.write((cmd + "\n").getBytes());
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("OutputStream", "打开失败");
        }
    }

    /**
     * 不能正常检测出是否Root
     *
     * @return boolean
     */
    public static boolean isRoot() {
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
