package com.mauriciotogneri.fcs.database;

import com.mauriciotogneri.fcs.util.NumberUtil;

public class LocationData
{
    private final long timestamp;
    public final int latitude;
    public final int longitude;
    public final int altitude;
    public final int accuracy;
    public final int speed;
    public final int bearing;

    public LocationData(long timestamp, double latitude, double longitude, double altitude, float accuracy, float speed, float bearing)
    {
        this.timestamp = timestamp;
        this.latitude = NumberUtil.asInt(latitude);
        this.longitude = NumberUtil.asInt(longitude);
        this.altitude = NumberUtil.asInt(altitude);
        this.accuracy = NumberUtil.asInt(accuracy);
        this.speed = NumberUtil.asInt(speed);
        this.bearing = NumberUtil.asInt(bearing);
    }

    public long timestamp()
    {
        return timestamp;
    }

    public float latitude()
    {
        return NumberUtil.asFloat(latitude);
    }

    public float longitude()
    {
        return NumberUtil.asFloat(longitude);
    }

    public float altitude()
    {
        return NumberUtil.asFloat(altitude);
    }

    public float accuracy()
    {
        return NumberUtil.asFloat(accuracy);
    }

    public float speed()
    {
        return NumberUtil.asFloat(speed);
    }

    public float bearing()
    {
        return NumberUtil.asFloat(bearing);
    }
}