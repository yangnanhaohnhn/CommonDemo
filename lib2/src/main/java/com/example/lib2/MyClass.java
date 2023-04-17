package com.example.lib2;

import java.awt.Color;
import java.util.Arrays;
import java.util.UUID;

import kotlin.jvm.functions.Function2;

/**
 * @author Administrator
 */
public class MyClass {
    public static void main(String[] args) {
//        int[] num = {1, 2, 3, 4, 5, 6, 7, 1};
//        long start = System.currentTimeMillis();
//        System.out.println("Start----------" + start);
//        System.out.println(containsDuplicate(num));
//        System.out.println("End  ----------" + System.currentTimeMillis());

//        for (int i = 0; i < 7; i++) {
//            System.out.println(num[i % num.length]);
//        }
//        System.out.println(Math.random() * 255);
//        for (int i = 0; i < 1000000; i++) {
//            System.out.println(UUID.randomUUID().toString());
//        }
        Test test = new Test();
        int res = test.fun1(10, 20, new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer invoke(Integer num1, Integer num2) {
                return num1 + num2;
            }
        });
        System.out.println("res----------" + res);

        test.main();

        double lat = 40.06292721;
        double lgt = 116.34095297;
        double lat1 = 40.06304220;
        double lgt1 = 116.34095295;
        System.out.println(getLongDistance(lat, lgt, lat1, lgt1));
    }
    private final static double DEF_PI180 = 0.01745329252; // PI/180.0
    private final static double DEF_R = 6370693.5; // 地球半径，m
    /**
     * 根据球面距离计算两点直接的距离
     */
    private static double getLongDistance(double lat1, double lon1, double lat2, double lon2) {
        double ew1 = lon1 * DEF_PI180;
        double ns1 = lat1 * DEF_PI180;
        double ew2 = lon2 * DEF_PI180;
        double ns2 = lat2 * DEF_PI180;
        // 角度转换为弧度
        // 求大圆劣弧与球心所夹的角(弧度)
        double distance = Math.sin(ns1) * Math.sin(ns2) + Math.cos(ns1) * Math.cos(ns2) * Math.cos(ew1 - ew2);
        // 调整到[-1..1]范围内，避免溢出
        if (distance > 1.0) {
            distance = 1.0;
        } else if (distance < -1.0) {
            distance = -1.0;
        }
        // 求大圆劣弧长度
        return DEF_R * Math.acos(distance);
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