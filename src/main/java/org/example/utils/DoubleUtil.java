package org.example.utils;

import java.math.BigDecimal;

/**
 * @author Yin Zihang
 */
public class DoubleUtil {

    public static double shortDouble(double num, int len) {
        BigDecimal bd = new BigDecimal(num);
        return bd.setScale(len, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
