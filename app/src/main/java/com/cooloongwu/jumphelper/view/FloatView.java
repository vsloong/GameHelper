package com.cooloongwu.jumphelper.view;

/**
 * 悬浮窗视图
 * Created by CooLongWu on 2017-12-30.
 */

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cooloongwu.jumphelper.R;
import com.cooloongwu.jumphelper.OSUtils;

public class FloatView extends LinearLayout implements View.OnClickListener {

    private View dragView;
    private View dragView2;
    private View text;

    private ViewDragHelper dragHelper;
    private WindowManager windowManager;
    private WindowManager.LayoutParams params = new WindowManager.LayoutParams();
    private int height;

    private float x1 = -1, y1 = -1, x2 = -1, y2 = -1;
    private int releasedId1 = -1;
    private float speed = (float) 0.485;

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public FloatView(Context context) {
        super(context, null);
    }

    public FloatView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initDragHelper();
        //这里执行完后会去执行 onFinishInflate()
    }


    private void initDragHelper() {
        setOrientation(VERTICAL);
        dragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return child == dragView || child == dragView2;
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                return top;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                return left;
            }

            // 当前被捕获的View释放之后回调
            @Override
            public void onViewReleased(View releasedChild, float x, float y) {
                //视图的左上角顶点（releasedChild.getLeft()，releasedChild.getTop()）
                //第一次触摸的视图就当做是Id1
                //Log.e("onViewReleased", releasedChild.getId() + "释放了");
                if (releasedId1 == -1) {
                    releasedId1 = releasedChild.getId();
                }
                if (releasedId1 == releasedChild.getId()) {
                    x1 = releasedChild.getLeft();
                    y1 = releasedChild.getTop();
                } else {
                    x2 = releasedChild.getLeft();
                    y2 = releasedChild.getTop();
                }
            }

            @Override
            public int getViewVerticalDragRange(View child) {
                return getMeasuredHeight() - child.getMeasuredHeight();
            }

            @Override
            public int getViewHorizontalDragRange(View child) {
                return getMeasuredWidth() - child.getMeasuredWidth();
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return dragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        dragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public void computeScroll() {
        if (dragHelper.continueSettling(true)) {
            invalidate();
        }
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        View buttonJump = findViewById(R.id.btn_jump);
        View buttonClose = findViewById(R.id.btn_close);
        text = findViewById(R.id.text);
        dragView = findViewById(R.id.dragView1);
        dragView2 = findViewById(R.id.dragView2);

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
            case R.id.btn_jump:
                //计算两个左上角顶点的距离
                if (x1 == -1 || x2 == -1 || y1 == -1 || y2 == -1)
                    return;
                float dis = (float) Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow(y1 - y2, 2));
                int time = Math.round((dis / getSpeed()));
                Log.e("计算结果", "距离：" + dis + "；速度：" + getSpeed() + "；时间：" + time);
                ((TextView) text).setText("距离" + Math.round(dis) + ";约" + time + "ms");

                int touchY = (int) (height * 0.9);
                Log.e("触摸Y坐标", "" + touchY);
                OSUtils.getInstance().exec("input swipe 200 " + touchY + " 300 " + touchY + " " + time + "\n");
                break;
            case R.id.btn_close:
                this.detach();
                break;
            default:
                break;
        }
    }

    public void attach() {
        if (this.getParent() == null) {
            windowManager.addView(this, params);
        }
    }

    public void detach() {
        try {
            windowManager.removeViewImmediate(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initWindowManager() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        height = displayMetrics.heightPixels;
        float density = displayMetrics.density;

        Log.e("屏幕宽高：", "宽：" + width + "；高：" + height + "；密度：" + density);
        windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);

        params.gravity = Gravity.START | Gravity.TOP;
        params.format = PixelFormat.RGBA_8888;
        params.width = width;
        params.height = (int) (height * 0.8);
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        params.alpha = 0.8f;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            params.type = WindowManager.LayoutParams.TYPE_TOAST;
        } else {
            params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
    }

}