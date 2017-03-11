package edu.muc.marking.util;

import com.google.common.base.Preconditions;

/**
 * version    date      author
 * ──────────────────────────────────
 * 1.0       17-3-5   wanlong.ma
 * Description: 字符串操作工具
 * Others:
 * Function List:
 * History:
 */
public class OtherUtil {
    /**
     * 将String的答案解析成整形数组
     * @param answer
     * @return
     */
    public static int[] parseStringAnswerToIntArray(String answer){
        Preconditions.checkNotNull(answer);
        int[] array = new int[answer.length()];
        for(int i=0;i<answer.length();i++){
            char ch = answer.charAt(i);
            array[i] = ch - 48;
        }
        return array;
    }

    /**
     * 数组求和
     * @param array
     * @return
     */
    public static int getSumOfArray(int[] array){
        Preconditions.checkNotNull(array);
        int sum = 0;
        for(int i=0;i<array.length;i++){
            sum += array[i];
        }
        return sum;
    }
}
