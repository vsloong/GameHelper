package com.cooloongwu.jumphelper.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cooloongwu.jumphelper.MyApplication;
import com.cooloongwu.jumphelper.R;
import com.cooloongwu.jumphelper.finder.CurrPosFinder;
import com.cooloongwu.jumphelper.finder.NextPosFinder;
import com.cooloongwu.jumphelper.utils.Config;
import com.cooloongwu.jumphelper.utils.OSUtils;

import java.io.File;

/**
 * 悬浮窗，自动寻找位置跳动的悬浮窗
 * Created by CooLoongWu on 2018-1-4 10:34.
 */

public class AutoFloatView extends LinearLayout implements View.OnClickListener {

    private boolean isJumping = false;
    private View btnAuto;

    public AutoFloatView(Context context) {
        super(context);
    }

    public AutoFloatView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        btnAuto = findViewById(R.id.btn_auto);

        Button btnClose = findViewById(R.id.btn_close);
        btnClose.setOnClickListener(this);
        btnAuto.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_auto:
                if (isJumping) {
                    isJumping = false;
                    btnAuto.setBackground(ContextCompat.getDrawable(this.getContext(), R.mipmap.btn_jump_normal));
                } else {
                    isJumping = true;
                    btnAuto.setBackground(ContextCompat.getDrawable(this.getContext(), R.mipmap.btn_stop));
                    new FindAndJump().execute();
                }
                break;
            case R.id.btn_close:
                if (isJumping) {
                    Toast.makeText(this.getContext(), "请先结束自动跳动", Toast.LENGTH_SHORT).show();
                    return;
                }
                detach();
                break;
            default:
                break;
        }
    }

    public void detach() {
        try {
            MyApplication.getInstance().detach(this);
            //关闭悬浮窗时强制退出App，为了结束自动跳的脚本
//            if (isExit)
//                System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Bitmap getBitmap() {
        File file = new File(Config.IMG_PATH + Config.IMG_NAME);
        try {
            if (file.exists()) {
                Log.e("获取图片", "文件存在" + file.getAbsolutePath());
                return BitmapFactory.decodeFile(file.getAbsolutePath());
            }
            Log.e("获取图片", "文件不存在");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("获取图片", "文件读取失败");
        }
        return null;
    }

    @SuppressLint("StaticFieldLeak")
    class FindAndJump extends AsyncTask<Void, Void, Integer> {
        @Override
        protected Integer doInBackground(Void... voids) {
            //获取到图片
            OSUtils.getInstance().exec(Config.CMD_SCREEN_SHOT);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int time = 10;//默认很小的时间
            Bitmap bitmap = getBitmap();
            if (bitmap != null) {
                int[] currentPos = CurrPosFinder.getCurrentPos(bitmap);
                int[] excepted = {currentPos[0] - 35, currentPos[0] + 35};
                int[] nextPos = NextPosFinder.getNextPos(bitmap, excepted, currentPos[1]);
                if (nextPos == null || nextPos[0] == 0) {
                    Log.e("查找下一次中心", "失败");
                } else {
                    int centerX, centerY;
                    int[] whitePoint = NextPosFinder.find(bitmap, nextPos[0] - 120, nextPos[1], nextPos[0] + 120, nextPos[1] + 180);
                    if (whitePoint != null) {
                        centerX = whitePoint[0];
                        centerY = whitePoint[1];
                        Log.e("查找白色中心点", "成功：（" + centerX + ", " + centerY + "）");
                        System.out.println("find whitePoint, succ, (" + centerX + ", " + centerY + ")");
                    } else {
                        if (nextPos[2] != Integer.MAX_VALUE && nextPos[4] != Integer.MIN_VALUE) {
                            centerX = (nextPos[2] + nextPos[4]) / 2;
                            centerY = (nextPos[3] + nextPos[5]) / 2;
                        } else {
                            centerX = nextPos[0];
                            centerY = nextPos[1] + 48;
                        }
                    }
                    double dis = Math.sqrt((centerX - currentPos[0]) * (centerX - currentPos[0]) + (centerY - currentPos[1]) * (centerY - currentPos[1]));
                    time = (int) (dis / MyApplication.getInstance().getSpeed());
                    Log.e("计算结果", "距离：" + dis + "；时间：" + time);

                }
            }
            return time;
        }

        @Override
        protected void onPostExecute(Integer time) {
            //查找到位置后开始跳
            OSUtils.getInstance().exec(Config.CMD_TOUCH_LONG.replaceAll("touchY", "200").replace("time", String.valueOf(time)));
            try {
                Thread.sleep(time * 4);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (isJumping)
                new FindAndJump().execute();
        }
    }
}
