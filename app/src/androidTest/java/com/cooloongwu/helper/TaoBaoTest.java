package com.cooloongwu.helper;

import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
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

    //点赞按钮ID：com.taobao.taobao:id/taolive_favor_icon_config

    @Test
    public void test() throws InterruptedException {
        //点击喜欢按钮
        //按钮范围[618,1179][696,1257]
        while (true) {
            uiDevice.click(620, 1200);
            Thread.sleep(100);
            uiDevice.click(622, 1202);
//            uiDevice.findObject(By.res(
//                    "com.taobao.taobao:id/taolive_favor_icon_config"))
//                    .click();
        }

    }
}
