package com.example.lib2;

import java.awt.Color;
import java.util.Arrays;

/**
 * @author Administrator
 */
public class MyClass {
    public static void main(String[] args) {
        int[] num = {1, 2, 3, 4, 5, 6, 7, 1};
//        long start = System.currentTimeMillis();
//        System.out.println("Start----------" + start);
//        System.out.println(containsDuplicate(num));
//        System.out.println("End  ----------" + System.currentTimeMillis());

        for (int i = 0; i < 7; i++) {
            System.out.println(num[i % num.length]);
        }
        System.out.println(Math.random() * 255);
    }

    /**
     * 给你一个数组，将数组中的元素向右轮转 k 个位置，其中 k 是非负数。
     *
     * @param num
     * @param k
     */
    private static void rotate(int[] num, int k) {
        if (k == 0 || null == num || num.length == 0) {
            return;
        }
        if (k % num.length == 0) {
            return;
        }
        int[] tmp = new int[num.length];
        System.arraycopy(num, 0, tmp, 0, num.length);
        for (int i = 0; i < tmp.length; i++) {
            num[(k + i) % tmp.length] = tmp[i];
        }
    }

    /**
     * 给定一个整数数组，判断是否存在重复元素。
     * 如果存在一值在数组中出现至少两次，函数返回 true 。如果数组中每个元素都不相同，则返回 false 。
     *
     * @param nums
     * @return
     */
    private static boolean containsDuplicate(int[] nums) {
        Arrays.sort(nums);
        for (int i = 0; i < nums.length - 1; i++) {
            if (nums[i] == nums[i + 1]) {
                return true;
            }
        }
        return false;
    }
}