package com.cooloongwu.jumphelper.finder;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

/**
 * 下一个要跳到的位置【目标：可以跳到中心，或者稍微偏离中心，防止跳一跳判定作弊】
 * Created by CooLoongWu on 2018-1-4 10:05.
 */

public class NextPosFinder {

    private static final int TARGET = 245;

    public static int[] getNextPos(Bitmap image, int[] currentPos) {
        if (image == null) {
            return null;
        }

        int width = image.getWidth();
        int height = image.getHeight();
        int pixel = image.getPixel(0, 200);
        int r1 = (pixel & 0xff0000) >> 16;
        int g1 = (pixel & 0xff00) >> 8;
        int b1 = (pixel & 0xff);
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < width; i++) {
            pixel = image.getPixel(i, height - 1);
            int temp = map.containsKey(pixel) && null != map.get(pixel) ? map.get(pixel) : 0;
            map.put(pixel, temp + 1);
        }
        int max = 0;
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            if (entry.getValue() > max) {
                pixel = entry.getKey();
                max = entry.getValue();
            }
        }
        int r2 = (pixel & 0xff0000) >> 16;
        int g2 = (pixel & 0xff00) >> 8;
        int b2 = (pixel & 0xff);

        int t = 16;

        int minR = Math.min(r1, r2) - t;
        int maxR = Math.max(r1, r2) + t;
        int minG = Math.min(g1, g2) - t;
        int maxG = Math.max(g1, g2) + t;
        int minB = Math.min(b1, b2) - t;
        int maxB = Math.max(b1, b2) + t;

        int[] ret = new int[6];
        int targetR = 0, targetG = 0, targetB = 0;
        boolean found = false;
        for (int j = height / 4; j < currentPos[1]; j++) {
            for (int i = 0; i < width; i++) {
                int dx = Math.abs(i - currentPos[0]);
                int dy = Math.abs(j - currentPos[1]);
                if (dy > dx) {
                    continue;
                }
                pixel = image.getPixel(i, j);
                int r = (pixel & 0xff0000) >> 16;
                int g = (pixel & 0xff00) >> 8;
                int b = (pixel & 0xff);
                if (r < minR || r > maxR || g < minG || g > maxG || b < minB || b > maxB) {
                    ret[0] = i;
                    ret[1] = j;
                    for (int k = 0; k < 5; k++) {
                        pixel = image.getPixel(i, j + k);
                        targetR += (pixel & 0xff0000) >> 16;
                        targetG += (pixel & 0xff00) >> 8;
                        targetB += (pixel & 0xff);
                    }
                    targetR /= 5;
                    targetG /= 5;
                    targetB /= 5;
                    found = true;
                    break;
                }
            }
            if (found) {
                break;
            }
        }

        if (targetR == BottleFinder.TARGET && targetG == BottleFinder.TARGET && targetB == BottleFinder.TARGET) {
            return BottleFinder.find(image, ret[0], ret[1]);
        }

        boolean[][] matchMap = new boolean[width][height];
        boolean[][] vMap = new boolean[width][height];
        ret[2] = Integer.MAX_VALUE;
        ret[3] = Integer.MAX_VALUE;
        ret[4] = Integer.MIN_VALUE;
        ret[5] = Integer.MAX_VALUE;

        Queue<int[]> queue = new ArrayDeque<>();
        queue.add(ret);
        while (!queue.isEmpty()) {
            int[] item = queue.poll();
            int i = item[0];
            int j = item[1];

            if (j >= currentPos[1]) {
                continue;
            }

            if (i < Math.max(ret[0] - 300, 0) ||
                    i >= Math.min(ret[0] + 300, width) ||
                    j < Math.max(0, ret[1] - 400) ||
                    j >= Math.max(height, ret[1] + 400) ||
                    vMap[i][j]) {
                continue;
            }
            vMap[i][j] = true;
            pixel = image.getPixel(i, j);
            int r = (pixel & 0xff0000) >> 16;
            int g = (pixel & 0xff00) >> 8;
            int b = (pixel & 0xff);
            matchMap[i][j] = match(r, g, b, targetR, targetG, targetB, 16);
            if (i == ret[0] && j == ret[1]) {
                System.out.println(matchMap[i][j]);
            }
            if (matchMap[i][j]) {
                if (i < ret[2]) {
                    ret[2] = i;
                    ret[3] = j;
                } else if (i == ret[2] && j < ret[3]) {
                    ret[2] = i;
                    ret[3] = j;
                }
                if (i > ret[4]) {
                    ret[4] = i;
                    ret[5] = j;
                } else if (i == ret[4] && j < ret[5]) {
                    ret[4] = i;
                    ret[5] = j;
                }
                if (j < ret[1]) {
                    ret[0] = i;
                    ret[1] = j;
                }
                queue.add(buildArray(i - 1, j));
                queue.add(buildArray(i + 1, j));
                queue.add(buildArray(i, j - 1));
                queue.add(buildArray(i, j + 1));
            }
        }
        Log.e("查找位置", "下一步位置：x：" + ret[0] + "；y：" + ret[1]);
        return ret;
    }

    public static int[] findWhite(Bitmap image, int x1, int y1, int x2, int y2) {
        if (image == null) {
            return null;
        }

        int width = image.getWidth();
        int height = image.getHeight();

        x1 = Math.max(x1, 0);
        x2 = Math.min(x2, width - 1);
        y1 = Math.max(y1, 0);
        y2 = Math.min(y2, height - 1);

        for (int i = x1; i <= x2; i++) {
            for (int j = y1; j <= y2; j++) {
                int pixel = image.getPixel(i, j);
                int r = (pixel & 0xff0000) >> 16;
                int g = (pixel & 0xff00) >> 8;
                int b = (pixel & 0xff);
                if (r == TARGET && g == TARGET && b == TARGET) {
                    boolean[][] vMap = new boolean[width][height];
                    Queue<int[]> queue = new ArrayDeque<>();
                    int[] pos = {i, j};
                    queue.add(pos);
                    int maxX = Integer.MIN_VALUE;
                    int minX = Integer.MAX_VALUE;
                    int maxY = Integer.MIN_VALUE;
                    int minY = Integer.MAX_VALUE;
                    while (!queue.isEmpty()) {
                        pos = queue.poll();
                        int x = pos[0];
                        int y = pos[1];
                        if (x < x1 || x > x2 || y < y1 || y > y2 || vMap[x][y]) {
                            continue;
                        }
                        vMap[x][y] = true;
                        pixel = image.getPixel(x, y);
                        r = (pixel & 0xff0000) >> 16;
                        g = (pixel & 0xff00) >> 8;
                        b = (pixel & 0xff);
                        if (r == TARGET && g == TARGET && b == TARGET) {
                            maxX = Math.max(maxX, x);
                            minX = Math.min(minX, x);
                            maxY = Math.max(maxY, y);
                            minY = Math.min(minY, y);
                            queue.add(buildArray(x - 1, y));
                            queue.add(buildArray(x + 1, y));
                            queue.add(buildArray(x, y - 1));
                            queue.add(buildArray(x, y + 1));
                        }
                    }

                    System.out.println("whitePoint: " + maxX + ", " + minX + ", " + maxY + ", " + minY);
                    if (maxX - minX <= 45 && maxX - minX >= 35 && maxY - minY <= 30 && maxY - minY >= 20) {
                        return new int[]{(minX + maxX) / 2, (minY + maxY) / 2};
                    } else {
                        return null;
                    }

                }
            }
        }
        return null;
    }

    private static int[] buildArray(int i, int j) {
        return new int[]{i, j};
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
