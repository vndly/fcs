package com.mauriciotogneri.fcs.util;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class DateUtil
{
    public static String format(long timestamp)
    {
        SimpleDateFormat sourceDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());

        return sourceDateFormat.format(timestamp);
    }
}