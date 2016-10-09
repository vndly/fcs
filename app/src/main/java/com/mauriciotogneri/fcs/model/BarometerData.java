package com.mauriciotogneri.fcs.model;

public class BarometerData
{
    private final long timestamp;
    private final float pressure;

    public BarometerData(long timestamp, float pressure)
    {
        this.timestamp = timestamp;
        this.pressure = pressure;
    }

    public long timestamp()
    {
        return timestamp;
    }

    public float pressure()
    {
        return pressure;
    }
}