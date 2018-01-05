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
    private static Process process;
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
                process = Runtime.getRuntime().exec("su");
                outputStream = process.getOutputStream();
                Log.e("Process", "" + process.toString());
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("OutputStream", "打开OS失败");
            }
        }
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public void exec(String cmd) {
        try {
            outputStream.write((cmd + "\n").getBytes());
            outputStream.flush();

            //添加等待命令，确保命令执行完成
//            outputStream.wait();
//            process.waitFor();
            Log.e("OutputStream", "执行命令成功");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("OutputStream", "执行命令失败");
        }
    }

    public void closeAndDestroy() {
        try {
            if (outputStream != null)
                outputStream.close();
            if (process != null)
                process.destroy();
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
