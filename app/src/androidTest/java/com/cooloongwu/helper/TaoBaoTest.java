package com.cooloongwu.helper;

import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;

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

    //点赞按钮ID：com.taobao.taobao:id/taolive_favor_icon_config     [618,1179][696,1257]

    //点击输入框：com.taobao.taobao:id/taolive_chat_btn_text     [141,1179][408,1257]
    //输入框ID：com.taobao.taobao:id/taolive_edit_text
    //发送按钮ID：com.taobao.taobao:id/taolive_edit_send    [600,646][720,726]

    @Test
    public void test() throws InterruptedException {
//        autoInput();
        autoLike();
    }

    /**
     * 自动发言功能
     *
     * @throws InterruptedException 异常
     */
    private void autoInput() throws InterruptedException {
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

    /**
     * 自动点赞功能
     *
     * @throws InterruptedException 异常
     */
    private void autoLike() throws InterruptedException {
        //按钮范围[618,1179][696,1257]
        while (true) {
            uiDevice.click(620, 1200);
//            uiDevice.click(622, 1202);
//            uiDevice.findObject(By.res(
//                    "com.taobao.taobao:id/taolive_favor_icon_config"))
//                    .click();
        }
    }
}
