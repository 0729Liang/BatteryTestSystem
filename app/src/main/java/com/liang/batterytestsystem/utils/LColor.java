package com.liang.batterytestsystem.utils;

import android.graphics.Color;

import java.util.Random;

/**
 * @author : Amarao
 * CreateAt : 14:36 2019/3/18
 * Describe :
 */
public class LColor {

    /**
     * 生成随机颜色
     * <p>
     * 参考：https://blog.csdn.net/qq_21036939/article/details/51282778?locationNum=15&fps=1
     * 参考：https://blog.csdn.net/coder_nice/article/details/50679781
     *
     * @return
     */
    public static int getRandomColor() {
        Random random = new Random();
        int a = random.nextInt(256);
        int r = random.nextInt(256);
        int g = random.nextInt(256);
        int b = random.nextInt(256);
        return Color.argb(a, r, g, b);
    }

//
//    public static int getRandomColor() {
//        int num=(int) (Math.random()*16777216);
//        String hex = Integer.toHexString(-num);
//        return LResourceX.parseColor(hex);
//    }

}
