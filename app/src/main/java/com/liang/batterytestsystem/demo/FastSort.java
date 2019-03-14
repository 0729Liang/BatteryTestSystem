package com.liang.batterytestsystem.demo;

/**
 * @author : Amarao
 * CreateAt : 10:56 2019/3/13
 * Describe : 快排
 */
public class FastSort {
    /**
     * 快速排序基本思想是：通过一趟排序将要排序的数据分割成独立的两部分，
     * 其中一部分的所有数据都比另外一部分的所有数据都要小，
     * 然后再按此方法对这两部分数据分别进行快速排序，
     * 整个排序过程可以递归进行，以此达到整个数据变成有序序列。
     * 基于分治的思想，是冒泡排序的改进型。
     *
     * 首先在数组中选择一个基准点（该基准点的选取可能影响快速排序的效率，后面讲解选取的方法），
     * 然后分别从数组的两端扫描数组，设两个指示标志（lo指向起始位置，hi指向末尾)，
     * 首先从后半部分开始，如果发现有元素比该基准点的值小，就交换lo和hi位置的值，
     * 然后从前半部分开始扫秒，发现有元素大于基准点的值，就交换lo和hi位置的值，
     * 如此往复循环，直到lo>=hi,然后把基准点的值放到hi这个位置。一次排序就完成了。
     * 以后采用递归的方式分别对前半部分和后半部分排序，当前半部分和后半部分均有序时该数组就自然有序了。
     * */
    /**
     * 快速排序
     *
     * @param num   排序的数组
     * @param left  数组的前针
     * @param right 数组后针
     */
    public static void quickSort(int[] num, int left, int right) {
        //如果left等于right，即数组只有一个元素，直接返回
        if (left >= right) {
            return;
        }
        //设置最左边的元素为基准值
        int key = num[left];
        //数组中比key小的放在左边，比key大的放在右边，key值下标为i
        int i = left;
        int j = right;
        while (i < j) {
            //j向左移，逐渐减小，直到遇到比key小的值，或者j减小到j<=i
            while (num[j] >= key && i < j) {
                j--;
            }
            //i向右移，逐渐增大，直到遇到比key大的值，或者i增大到i>=j
            while (num[i] <= key && i < j) {
                i++;
            }
            //否则，i和j指向的元素交换
            if (i < j) {
                int temp = num[i];
                num[i] = num[j];
                num[j] = temp;
            }
        }
        num[left] = num[i];
        num[i] = key;

        quickSort(num, left, i - 1);
        quickSort(num, i + 1, right);
    }

    public static void sort(int[] array, int lo, int hi) {
        if (lo >= hi) {
            return;
        }
        int index = partition(array, lo, hi);
        sort(array, lo, index - 1);
        sort(array, index + 1, hi);
    }

    // 划分数组
    private static int partition(int[] array, int lo, int hi) {
        //固定的切分方式,以最左边为基准轴
        int key = array[lo];
        //hi向左移，逐渐减小，直到遇到比key小的值，或者hi减小到hi<=lo
        while (lo < hi) {
            while (array[hi] >= key && hi > lo) {//从后半部分向前扫描
                hi--;
            }
            array[lo] = array[hi];

            //lo向右移，逐渐增大，直到遇到比key大的值，或者lo增大到lo>=hi
            while (array[lo] <= key && hi > lo) {//从前半部分向后扫描
                lo++;
            }
            array[hi] = array[lo];
        }
        array[hi] = key;
        return hi;
    }

    /**
     * 将一个int类型数组转化为字符串
     *
     * @param arr
     * @return
     */
    public static String arrayToString(int[] arr) {
        StringBuilder str = new StringBuilder();
        for (int a : arr) {
            str.append(a).append("\t");
        }
        return str.toString();
    }


}
