package com.cooloongwu.jumphelper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cooloongwu.jumphelper.view.FloatView;

import ezy.assist.compat.SettingsCompat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView textMsg;
    private Button btnAttach;
    private EditText editSpeed;

    private FloatView floatView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //获取Root权限
        OSUtils.getInstance();
        findViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        btnAttach.setVisibility(
                checkPermission() ? View.VISIBLE : View.GONE
        );
    }

    private void findViews() {
        floatView = (FloatView) getLayoutInflater().inflate(R.layout.view_float, null);

        textMsg = findViewById(R.id.text_msg);
        editSpeed = findViewById(R.id.edit_speed);
        btnAttach = findViewById(R.id.btn_attach);
        Button btnModify = findViewById(R.id.btn_modify);
        btnAttach.setOnClickListener(this);
        btnModify.setOnClickListener(this);
    }

    private boolean checkPermission() {
        if (SettingsCompat.canDrawOverlays(this)) {
            textMsg.setText("悬浮窗权限已获取");
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
                floatView.initWindowManager();
                floatView.attach();
                goHome();
                break;
            case R.id.btn_modify:
                String str = editSpeed.getText().toString().trim();
                if (!TextUtils.isEmpty(str)) {
                    float speed = Float.parseFloat(str);
                    if (speed > 0) {
                        floatView.setSpeed(speed);
                        Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OSUtils.getInstance().close();
    }

    /**
     * 监听返回按键【当点击返回键时执行Home键效果】
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            goHome();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void goHome() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }
}
