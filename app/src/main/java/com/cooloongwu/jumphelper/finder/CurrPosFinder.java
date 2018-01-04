package com.cooloongwu.jumphelper.finder;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

/**
 * 查找到小人当前位置
 * Created by CooLoongWu on 2018-1-4 10:04.
 */

public class CurrPosFinder {

    private static final int R_TARGET = 40;

    private static final int G_TARGET = 43;

    private static final int B_TARGET = 86;

    public int[] find(Bitmap image) {
        if (image == null) {
            return null;
        }
        int width = image.getWidth();
        int height = image.getHeight();
        int pixelColor, a, r, g, b;

        int[] ret = {0, 0};
        int maxX = Integer.MIN_VALUE;
        int minX = Integer.MAX_VALUE;
        int maxY = Integer.MIN_VALUE;
        int minY = Integer.MAX_VALUE;

        //单独提取出来效率更高些
        int heightStart = height / 4;       //从1/4的高度开始识别
        int heightEnd = height * 3 / 4;     //在3/4的位置结束识别

        for (int i = 0; i < width; i++) {
            for (int j = heightStart; j < heightEnd; j++) {
                pixelColor = image.getPixel(i, j);
                a = Color.alpha(pixelColor);
                r = Color.red(pixelColor);
                g = Color.green(pixelColor);
                b = Color.blue(pixelColor);

                if (match(r, g, b, R_TARGET, G_TARGET, B_TARGET, 16) && j > ret[1]) {
                    maxX = Math.max(maxX, i);
                    minX = Math.min(minX, i);
                    maxY = Math.max(maxY, j);
                    minY = Math.min(minY, j);
                }
            }
        }
        ret[0] = (maxX + minX) / 2 + 3;
        ret[1] = maxY;
        Log.e("查找", "当前位置：x：" + ret[0] + "；y：" + ret[1]);
        return ret;
    }

    private static boolean match(int r, int g, int b, int rt, int gt, int bt, int t) {
        return r > rt - t &&
                r < rt + t &&
                g > gt - t &&
                g < gt + t &&
                b > bt - t &&
                b < bt + t;
    }
}
