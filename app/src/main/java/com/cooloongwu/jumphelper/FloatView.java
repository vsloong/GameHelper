package com.cooloongwu.jumphelper;

/**
 * 悬浮窗视图
 * Created by CooLongWu on 2017-12-30.
 */

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FloatView extends LinearLayout implements View.OnClickListener {

    ViewDragHelper dragHelper;

    private float x1 = -1, y1 = -1, x2 = -1, y2 = -1;
    private int releasedId1 = -1;

    public FloatView(Context context, AttributeSet attrs) {
        super(context, attrs);
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

//                //计算两个左上角顶点的距离
//                if (x1 == -1 || x2 == -1 || y1 == -1 || y2 == -1)
//                    return;
//                float dis = (float) Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow(y1 - y2, 2));
//                Log.e("两点间距离", "" + dis);
//                ((TextView) text).setText("两点间距离：" + dis);
            }

            @Override
            public void onEdgeDragStarted(int edgeFlags, int pointerId) {
//                dragHelper.captureChildView(edgeDragView, pointerId);
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

    View dragView;
    View dragView2;
    View text;
    View button;

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        button = findViewById(R.id.btn);
        text = findViewById(R.id.text);
        dragView = findViewById(R.id.dragView1);
        dragView2 = findViewById(R.id.dragView2);

        button.setOnClickListener(this);

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
        //计算两个左上角顶点的距离
        if (x1 == -1 || x2 == -1 || y1 == -1 || y2 == -1)
            return;
        float dis = (float) Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow(y1 - y2, 2));
        Log.e("两点间距离", "" + dis);
        int time = (int) Math.round((dis / 0.485));
        ((TextView) text).setText("距离" + Math.round(dis) + ";约" + time + "ms");
        Utils.exec("input swipe 600 1200 600 1200 " + time + "\n");
    }
}