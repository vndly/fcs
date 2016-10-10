package com.mauriciotogneri.fcs.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class DateUtil
{
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());

    public static String format(long timestamp)
    {
        return DATE_FORMAT.format(timestamp);
    }

    public static long parse(String date)
    {
        try
        {
            return DATE_FORMAT.parse(date).getTime();
        }
        catch (ParseException e)
        {
            throw new RuntimeException(e);
        }
    }
}