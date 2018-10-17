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

//        text = new TextView(this.getContext());
//        ((TextView) text).setText("两点间距离");
//        ((TextView) text).setTextSize(18);
//        text.setLayoutParams(new LinearLayout.LayoutParams(
//                LayoutParams.WRAP_CONTENT,
//                LayoutParams.WRAP_CONTENT));
//        text.setBackgroundColor(getResources().getColor(R.color.colorAccent));
//
//        dragView = new TextView(this.getContext());
//        ((TextView) dragView).setText("拖动图1");
//        ((TextView) dragView).setTextSize(18);
//        dragView.setId(R.id.dragView1);
//        dragView.setLayoutParams(new LinearLayout.LayoutParams(
//                LayoutParams.WRAP_CONTENT,
//                LayoutParams.WRAP_CONTENT));
//        dragView.setBackgroundColor(getResources().getColor(R.color.colorAccent));
//
//        dragView2 = new TextView(this.getContext());
//        ((TextView) dragView2).setText("拖动图2");
//        ((TextView) dragView2).setTextSize(18);
//        dragView2.setId(R.id.dragView2);
//        dragView2.setLayoutParams(new LinearLayout.LayoutParams(
//                LayoutParams.WRAP_CONTENT,
//                LayoutParams.WRAP_CONTENT));
//        dragView2.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
//
//        this.addView(text);
//        this.addView(dragView);
//        this.addView(dragView2);
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
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                OSUtils.getInstance()
                        .exec(Config.getInstance().tapLike());
                handler.postDelayed(this, 1000);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}