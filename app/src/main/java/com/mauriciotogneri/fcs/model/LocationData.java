package com.mauriciotogneri.fcs.model;

public class LocationData
{
    private final long timestamp;
    private final float latitude;
    private final float longitude;
    private final float altitude;
    private final float accuracy;
    private final float speed;
    private final float bearing;

    public LocationData(long timestamp, double latitude, double longitude, double altitude, float accuracy, float speed, float bearing)
    {
        this.timestamp = timestamp;
        this.latitude = (float) latitude;
        this.longitude = (float) longitude;
        this.altitude = (float) altitude;
        this.accuracy = accuracy;
        this.speed = speed;
        this.bearing = bearing;
    }

    public long timestamp()
    {
        return timestamp;
    }

    public float latitude()
    {
        return latitude;
    }

    public float longitude()
    {
        return longitude;
    }

    public float altitude()
    {
        return altitude;
    }

    public float accuracy()
    {
        return accuracy;
    }

    public float speed()
    {
        return speed;
    }

    public float bearing()
    {
        return bearing;
    }
}