package com.mauriciotogneri.fcs.model;

import com.mauriciotogneri.fcs.util.DateUtil;

public class Session
{
    private final long timestamp;

    public Session()
    {
        this.timestamp = System.currentTimeMillis();
    }

    public String id()
    {
        return DateUtil.format(timestamp);
    }

    public long timestamp()
    {
        return timestamp;
    }
}