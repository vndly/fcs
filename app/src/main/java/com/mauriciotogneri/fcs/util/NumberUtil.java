package com.mauriciotogneri.fcs.util;

import java.math.BigDecimal;

public class NumberUtil
{
    private static final double FLOAT_INT_FACTOR = 1e7;

    public static float round(float number)
    {
        return new BigDecimal(number).setScale(5, BigDecimal.ROUND_HALF_UP).floatValue();
    }

    public static int asInt(double number)
    {
        return (int) (number * FLOAT_INT_FACTOR);
    }

    public static float asFloat(long number)
    {
        return (float)number / (float)FLOAT_INT_FACTOR;
    }
}