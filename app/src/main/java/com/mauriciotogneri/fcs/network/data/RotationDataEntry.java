package com.mauriciotogneri.fcs.network.data;

import com.mauriciotogneri.fcs.model.RotationData;
import com.mauriciotogneri.fcs.util.NumberUtil;

public class RotationDataEntry
{
    public int x;
    public int y;
    public int z;

    public RotationDataEntry()
    {
    }

    public RotationDataEntry(RotationData data)
    {
        this.x = NumberUtil.asInt(data.x());
        this.y = NumberUtil.asInt(data.y());
        this.z = NumberUtil.asInt(data.z());
    }
}