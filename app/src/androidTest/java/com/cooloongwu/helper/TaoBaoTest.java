package com.cooloongwu.helper;

import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.util.Log;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by CooLoongWu on 2018-10-16 20:57.
 */

@RunWith(AndroidJUnit4.class)
public class TaoBaoTest {

    public Instrumentation instrumentation;
    public UiDevice uiDevice;

    @Before
    public void setUp() {
        instrumentation = InstrumentationRegistry.getInstrumentation();
        uiDevice = UiDevice.getInstance(instrumentation);
    }

    /**
     * 自动点赞功能
     */
    @Test
    public void autoLike() throws InterruptedException {
        int widthPx = uiDevice.getDisplayWidth();
        int widthDp = uiDevice.getDisplaySizeDp().x;
        int p = widthPx / widthDp;

        int x = uiDevice.getDisplayWidth() - 32 * p;
        int y = uiDevice.getDisplayHeight() - 32 * p;

        while (true) {
            uiDevice.click(x, y);
//            Thread.sleep(800);
        }
    }


    /**
     * 自动发言功能
     *
     * @throws InterruptedException 异常
     */
    public void autoInput() throws InterruptedException {
        while (true) {
            uiDevice.click(400, 1200);
            Thread.sleep(500);
            //这里需要优化，淘宝会提示操作过于频繁
            uiDevice.findObject(By.res("com.taobao.taobao:id/taolive_edit_text"))
                    .setText("非常棒哦！");
            Thread.sleep(200);
            uiDevice.click(700, 700);
            Thread.sleep(300);
        }
    }

    //点赞按钮ID：com.taobao.taobao:id/taolive_favor_icon_config     [618,1179][696,1257]
    //点击输入框：com.taobao.taobao:id/taolive_chat_btn_text     [141,1179][408,1257]
    //输入框ID：com.taobao.taobao:id/taolive_edit_text
    //发送按钮ID：com.taobao.taobao:id/taolive_edit_send    [600,646][720,726]


    /**
     * 按钮距离边缘是12dp，按钮的宽高按照是40dp
     * 所以按照点击的位置距离右下角屏幕边缘32dp来计算
     */
    public void checkScreen() {
        Log.e("测试获取屏幕宽高", "分辨率：" + uiDevice.getDisplayWidth() + "*" + uiDevice.getDisplayHeight());
        Log.e("测试获取屏幕", "" + uiDevice.getDisplaySizeDp());

        int widthPx = uiDevice.getDisplayWidth();
        int widthDp = uiDevice.getDisplaySizeDp().x;
        int p = widthPx / widthDp;
        Log.e("比例是", "" + p);

        int x = uiDevice.getDisplayWidth() - 32 * p;
        int y = uiDevice.getDisplayHeight() - 32 * p;

        Log.e("点击位置是", "[" + x + "，" + y + "]");

        //屏幕是720*1280，点赞按钮区域[618,1179][696,1257]
        /**
         * 魅蓝5
         * 720*1280 (360*640)  倍数2
         * [618,1179][696,1257]
         * x：720 - 696 = 24
         * y：1280 - 1257 = 23
         * 所以边缘大概是 24px 除以2也就是12dp
         *
         * 按钮的大小
         * 696 - 618 = 78px 除以2也就是39dp
         * 1257 - 1179 = 78
         */


        /**
         * 三星s8
         * 1080*2076 (360*740) 倍数3
         * [927,2068][1044,2076]
         * x：1080 - 1044 = 36px  除以3也就是12dp
         *
         * 按钮的大小
         * 1044 - 927 = 117px 除以3也就是39dp
         */
    }


}
