package com.cooloongwu.helper.like;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cooloongwu.helper.R;
import com.cooloongwu.helper.jump.MyApplication;

import ezy.assist.compat.SettingsCompat;

public class LikeActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnAttach;
    TextView textMsg;
    LikeFloatView likeFloatView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like);

        initToolbar();
        initViews();
    }

    private void initViews() {
        btnAttach = findViewById(R.id.btn_attach);
        btnAttach.setOnClickListener(this);
        likeFloatView = (LikeFloatView) getLayoutInflater().inflate(R.layout.view_float_like, null);

        textMsg = findViewById(R.id.text_msg);
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        btnAttach.setVisibility(
                checkPermission() ? View.VISIBLE : View.GONE
        );
    }

    @Override
    public void onClick(View view) {
        MyApplication.getInstance().attach(likeFloatView);
        goHome();
    }

    private boolean checkPermission() {
        if (SettingsCompat.canDrawOverlays(this)) {
            textMsg.setText("悬浮窗权限已获取");
            return true;
        } else {
            textMsg.setText("请在设置中为该应用开启悬浮窗权限");
            return false;
        }
    }

    private void goHome() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e("点击的位置",""+event.getX()+"、"+event.getY());
        return super.onTouchEvent(event);
    }
}
