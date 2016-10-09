package com.mauriciotogneri.fcs.network.data;

import com.mauriciotogneri.fcs.model.AccelerometerData;
import com.mauriciotogneri.fcs.util.NumberUtil;

public class AccelerometerDataEntry
{
    public int x;
    public int y;
    public int z;

    public AccelerometerDataEntry()
    {
    }

    public AccelerometerDataEntry(AccelerometerData data)
    {
        this.x = NumberUtil.asInt(data.x());
        this.y = NumberUtil.asInt(data.y());
        this.z = NumberUtil.asInt(data.z());
    }

    public AccelerometerData data(long timestamp)
    {
        return new AccelerometerData(
                timestamp,
                NumberUtil.asFloat(x),
                NumberUtil.asFloat(y),
                NumberUtil.asFloat(z)
        );
    }
}