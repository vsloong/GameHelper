package com.cooloongwu.jumphelper;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import ezy.assist.compat.SettingsCompat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private WindowManager windowManager;
    private WindowManager.LayoutParams params = new WindowManager.LayoutParams();
    private float density;

    private TextView textMsg;
    private Button btnAttach;

    private FloatView floatView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        initWindowManager();
    }

    @Override
    protected void onResume() {
        super.onResume();
        btnAttach.setVisibility(
                checkPermission() ? View.VISIBLE : View.GONE
        );
    }

    private void findViews() {
        textMsg = findViewById(R.id.text_msg);
        floatView = (FloatView) getLayoutInflater().inflate(R.layout.view_float, null);
        btnAttach = findViewById(R.id.btn_attach);
        btnAttach.setOnClickListener(this);
    }

    private void initWindowManager() {
        density = getResources().getDisplayMetrics().density;
        windowManager = (WindowManager) getApplication().getSystemService(Context.WINDOW_SERVICE);
        params.gravity = Gravity.START | Gravity.TOP;
        params.format = PixelFormat.RGBA_8888;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = dp2px(360);
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        params.alpha = 0.5f;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            params.type = WindowManager.LayoutParams.TYPE_TOAST;
        } else {
            params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
    }

    public void attach() {
        if (floatView.getParent() == null) {
            windowManager.addView(floatView, params);
        }
    }

    public void detach() {
        try {
            windowManager.removeViewImmediate(floatView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int dp2px(int dp) {
        return (int) (dp * density);
    }

    private boolean checkPermission() {
        if (SettingsCompat.canDrawOverlays(this)) {
            textMsg.setText("悬浮窗权限已获取");
            //获取Root权限
            Utils.exec("");
            return true;
        } else {
            textMsg.setText("请在设置中为该应用开启悬浮窗权限");
            SettingsCompat.manageDrawOverlays(this);
            return false;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_attach:
                attach();
                break;
            default:
                break;
        }
    }
}
