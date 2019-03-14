package com.liang.batterytestsystem.demo;

/**
 * @author : Amarao
 * CreateAt : 15:28 2019/3/11
 * Describe :
 */
public class ResverString {


    /**
     * 二分法,递归实现
     *
     * @param s 需要翻转的字符
     * @return
     */
    public static String resverByDichotomy(String s) {
        int length = s.length();
        if (length <= 1) {
            return s;
        }
        String pre = s.substring(0, length / 2);
        String next = s.substring(length / 2, length);
        return resverByDichotomy(next) + resverByDichotomy(pre);
    }

    /**
     * 从头部开始,正序:通过字符串数组实现从尾部开始esrever顺序逐个进入字符串reverse
     * 该方法是通过charAt()方法获得每一个char的字符，i=0时获得第一个字符a然后赋值给reverse
     * 此时reverse="a";i=1时获得第二个字符b然后加上reverse再赋值给reverse，此时reverse="ba";
     * 一次类推
     */
    public static String reverseByCharAt(String s) {
        int length = s.length();
        StringBuilder reverse = new StringBuilder();
        for (int i = 0; i < length; i++) {
            reverse.insert(0, s.charAt(i));
        }
        return reverse.toString();
    }

    //从尾部开始,倒序
    public static String reverse3(String str) {
        char[] arr = str.toCharArray(); //string转换成char数组
        StringBuilder reverse = new StringBuilder();
        for (int i = arr.length - 1; i >= 0; i--) {
            reverse.append(arr[i]);
        }
        return reverse.toString();
    }

    /**
     * 通过StringBuiler的reverse()的方法，最快的方式。
     *
     * @param s
     * @return
     */
    public static String reverseByStringBuilder(String s) {
        StringBuilder sb = new StringBuilder(s);
        return sb.reverse().toString();
    }


}
