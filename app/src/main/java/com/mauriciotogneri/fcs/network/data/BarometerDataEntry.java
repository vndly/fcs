package com.mauriciotogneri.fcs.network.data;

import com.mauriciotogneri.fcs.model.BarometerData;
import com.mauriciotogneri.fcs.util.NumberUtil;

public class BarometerDataEntry
{
    public int pressure;

    public BarometerDataEntry()
    {
    }

    public BarometerDataEntry(BarometerData data)
    {
        this.pressure = NumberUtil.asInt(data.pressure());
    }

    public BarometerData data(long timestamp)
    {
        return new BarometerData(
                timestamp,
                NumberUtil.asFloat(pressure)
        );
    }
}