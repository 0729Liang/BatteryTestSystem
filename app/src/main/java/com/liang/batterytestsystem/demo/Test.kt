package com.liang.batterytestsystem.demo


import com.liang.liangutils.msg.BusEvent
import com.liang.liangutils.utils.LLogX
import java.util.*

/**
 * @author : Amarao
 * CreateAt : 10:59 2019/2/21
 * Describe :
 */
class Test : BusEvent, Cloneable {

    internal var name: String = ""
    internal var age: Int = 0
    internal var mList: List<String> = ArrayList()

    constructor(name: String) {
        this.name = name
    }

    constructor(vararg s: String)

    fun test(list: List<String>) {
        val list1 = ArrayList<String>()
        // Collections.copy(list, list1)
    }

    @Throws(CloneNotSupportedException::class)
    override fun clone(): Any {
        return super.clone()
    }


    companion object {
        fun listTest() {
            val list1: MutableList<String> = ArrayList()
            val list2: MutableList<String> = ArrayList()

            list1.add("1")
            list1.add("2")
            list1.add("3")
            /**
             * 这样做，会改变list1！因为2个list指向同一处
             * list2 = list1;// 浅复制，list1改变会影响list2
             * list2.add("4");// 深复制
             * */
            list2.addAll(list1)// 深复制
            list2.add("4")

            list1.add("7")
            list1.add("8")
            list2[0] = "11"
            LLogX.e(list1.toString())
            LLogX.e(list2.toString())
        }
    }
    //    @Override
    //    protected Test clone() throws CloneNotSupportedException {
    //        Test address = null;
    //        try {
    //            address = (Test) super.clone();
    //        } catch (CloneNotSupportedException e) {
    //            e.printStackTrace();
    //        }
    //        return address;
    //    }

}
