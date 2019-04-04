package com.liang.batterytestsystem.demo;

import com.liang.liangutils.utils.LLogX;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : Amarao
 * CreateAt : 14:52 2019/3/16
 * Describe :
 */
public class TestJava implements Cloneable {


    List<String> mList = new ArrayList<>();

    public static void listTest() {
        List<String> list1 = new ArrayList<String>();
        List<String> list2 = new ArrayList<String>();
        list1.add("1");
        list1.add("2");
        list1.add("3");
  /*这样做，会改变list1！因为2个list指向同一处
  list2 = list1;
  list2.add("4");
  */
        list2.addAll(list1);
        list2.add("4");
        list1.add("5");
        list1.add("4");
        list2.set(0, "11");
        LLogX.e(list1.toString());
        LLogX.e(list2.toString());
    }

    public void et() {

    }

    @Override
    protected Object clone() {
        TestJava student = null;
        try {
            student = (TestJava) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return student;
    }
}
