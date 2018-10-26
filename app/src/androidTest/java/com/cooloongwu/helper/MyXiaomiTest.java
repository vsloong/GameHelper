package com.cooloongwu.helper;

import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.util.Log;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by CooLoongWu on 2018-10-24 09:11.
 */
@RunWith(AndroidJUnit4.class)
public class MyXiaomiTest {

    public Instrumentation instrumentation;
    public UiDevice uiDevice;

    @Before
    public void setUp() {
        instrumentation = InstrumentationRegistry.getInstrumentation();
        uiDevice = UiDevice.getInstance(instrumentation);
    }

    @Test
    public void test() {
        Log.e("手机屏幕分辨率", "宽：" + uiDevice.getDisplayWidth() + "高：" + uiDevice.getDisplayHeight());
        //解锁手机屏幕
        uiDevice.swipe(600, 600, 600, 100, 500);
    }
}
