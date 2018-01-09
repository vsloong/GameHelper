package com.cooloongwu.jumphelper.utils;

import android.util.Log;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * 工具类
 * <p>
 * Created by CooLoongWu on 2018-1-2 10:09.
 */

public class OSUtils {

    private volatile static OSUtils osUtils;
    private static Process process;
    private static DataOutputStream dos;

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
        if (dos == null) {
            try {
                //TODO 魅蓝5执行su没效果怎么，1月7号上午开始的，之前好好的！
                process = Runtime.getRuntime().exec("su");
                dos = new DataOutputStream(process.getOutputStream());
//                dos.writeBytes("chmod 777 " + MyApplication.getInstance().getPackageCodePath() + "\n");
//                dos.flush();
                Log.e("Process实例化", "" + process.toString());
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("OutputStream", "打开OS失败");
            }
        }
    }

    public void exec(String cmd) {
        try {
            dos.writeBytes(cmd + "\n");
            dos.flush();
            Log.e("OutputStream", "执行命令" + cmd + "成功");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("OutputStream", "执行命令失败");
        }
    }

    public void closeAndDestroy() {
        try {
            if (dos != null)
                dos.close();
            if (process != null)
                process.destroy();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("OutputStream", "关闭OS失败");
        }
    }

    public static int execCmd(String cmd) {
        int result = -1;
        try {
            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream dos = new DataOutputStream(process.getOutputStream());
            dos.writeBytes(cmd + "\n");
            dos.writeBytes("exit\n");
            dos.flush();
            dos.close();
//            InputStream is = process.getErrorStream();//注意注意注意
//            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
//            StringBuilder res = new StringBuilder();
//            String line;
//            while ((line = bufferedReader.readLine()) != null) {
//                res.append(line);
//                System.out.print(line);
//            }
//            bufferedReader.close();
//            is.close();
//
//            Log.e("不知道是啥", "" + res);
//            int s = process.waitFor();
            result = process.exitValue();

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("命令执行失败", "" + e.getMessage());
        }
        Log.e("命令执行结果", "" + result);
        return result;
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
