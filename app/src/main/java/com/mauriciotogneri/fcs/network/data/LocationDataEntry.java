package com.mauriciotogneri.fcs.network.data;

import com.mauriciotogneri.fcs.model.LocationData;
import com.mauriciotogneri.fcs.util.NumberUtil;

public class LocationDataEntry
{
    public int longitude;
    public int latitude;
    public int altitude;
    public int accuracy;
    public int speed;
    public int bearing;

    public LocationDataEntry()
    {
    }

    public LocationDataEntry(LocationData data)
    {
        this.longitude = NumberUtil.asInt(data.longitude());
        this.latitude = NumberUtil.asInt(data.latitude());
        this.altitude = NumberUtil.asInt(data.altitude());
        this.accuracy = NumberUtil.asInt(data.accuracy());
        this.speed = NumberUtil.asInt(data.speed());
        this.bearing = NumberUtil.asInt(data.bearing());
    }

    public LocationData data(long timestamp)
    {
        return new LocationData(
                timestamp,
                NumberUtil.asFloat(longitude),
                NumberUtil.asFloat(latitude),
                NumberUtil.asFloat(altitude),
                NumberUtil.asFloat(accuracy),
                NumberUtil.asFloat(speed),
                NumberUtil.asFloat(bearing)
        );
    }
}