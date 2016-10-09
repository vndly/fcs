package com.mauriciotogneri.fcs.model;

public class AccelerometerData
{
    private final long timestamp;
    private final float x;
    private final float y;
    private final float z;

    public AccelerometerData(long timestamp, float x, float y, float z)
    {
        this.timestamp = timestamp;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public long timestamp()
    {
        return timestamp;
    }

    public float x()
    {
        return x;
    }

    public float y()
    {
        return y;
    }

    public float z()
    {
        return z;
    }
}