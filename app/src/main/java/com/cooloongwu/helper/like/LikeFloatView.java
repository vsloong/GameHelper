package com.cooloongwu.helper.like;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.cooloongwu.helper.R;
import com.cooloongwu.helper.jump.MyApplication;
import com.cooloongwu.helper.jump.utils.Config;
import com.cooloongwu.helper.jump.utils.OSUtils;

/**
 * 悬浮窗视图，需要手动操作选择距离的
 * Created by CooLongWu on 2017-12-30.
 */
public class LikeFloatView extends LinearLayout implements View.OnClickListener {

    private Handler handler;
    private Runnable runnable;

    public LikeFloatView(Context context) {
        super(context, null);
    }

    public LikeFloatView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //这里执行完后会去执行 onFinishInflate()
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        View buttonJump = findViewById(R.id.btn_auto);
        View buttonClose = findViewById(R.id.btn_close);

        buttonJump.setOnClickListener(this);
        buttonClose.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_auto:
                autoLike();
                break;
            case R.id.btn_close:
                detach();
                break;
            default:
                break;
        }
    }


    private void autoLike() {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                OSUtils.getInstance()
                        .exec(Config.getInstance().tapLike());
                handler.postDelayed(this, 200);
            }
        };
        handler.post(runnable);
    }

    /**
     * 关闭悬浮窗
     */
    public void detach() {
        try {
            MyApplication.getInstance().detach(this);
            if (handler != null && runnable != null) {
                handler.removeCallbacks(runnable);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}