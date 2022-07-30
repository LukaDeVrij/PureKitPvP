package me.lifelessnerd.purekitpvp.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

// https://stackoverflow.com/questions/2808535/round-a-double-to-2-decimal-places

public class DoubleUtils {

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

}
