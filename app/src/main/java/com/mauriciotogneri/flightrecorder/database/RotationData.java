package com.mauriciotogneri.flightrecorder.database;

import com.mauriciotogneri.flightrecorder.util.NumberUtil;

public class RotationData
{
    private final long timestamp;
    public final int x;
    public final int y;
    public final int z;

    public RotationData(long timestamp, float x, float y, float z)
    {
        this.timestamp = timestamp;
        this.x = NumberUtil.asInt(x);
        this.y = NumberUtil.asInt(y);
        this.z = NumberUtil.asInt(z);
    }

    public long timestamp()
    {
        return timestamp;
    }

    public float x()
    {
        return NumberUtil.asFloat(x);
    }

    public float y()
    {
        return NumberUtil.asFloat(y);
    }

    public float z()
    {
        return NumberUtil.asFloat(z);
    }
}