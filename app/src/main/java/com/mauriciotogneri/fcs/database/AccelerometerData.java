package com.mauriciotogneri.fcs.database;

import com.mauriciotogneri.fcs.util.NumberUtil;

public class AccelerometerData
{
    private final long timestamp;
    public final int x;
    public final int y;
    public final int z;

    public AccelerometerData(long timestamp, float x, float y, float z)
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