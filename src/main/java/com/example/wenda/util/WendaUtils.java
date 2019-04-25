package com.example.wenda.util;

import java.security.MessageDigest;

/**
 * @Author: weilei
 * @CreateTime: 2019/4/24  16:08
 * @Description:
 */

public class WendaUtils {
    public static String MD5(String key) {
        char hexDigits[] = {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
        };
        try {
            byte[] btInput = key.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     *  计算问题的排名分数
     * @param qView 问题浏览数
     * @param qAnswer 问题回答数目
     * @param qScore 问题的赞踩数目差
     * @param sumOfaScore 问题所有回答赞踩差的和
     * @param qAge 问题自发布到现在的时间差
     * @param qUpdated 问题最新的回答时间到现在的时间差
     * @return
     */
    public static Double sortAlgorithm(long qView, long qAnswer, long qScore, long sumOfaScore,
                                       long qAge, long qUpdated) {
        //分子
        double numerator = Math.log10(qView*4)+(qAnswer*qScore)/5+sumOfaScore;
        //分母
        double denominator = Math.pow(1+(qAge+qUpdated)/2,1.5);
        return numerator/denominator;
    }
}
