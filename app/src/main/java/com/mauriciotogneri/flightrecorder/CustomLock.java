package com.mauriciotogneri.flightrecorder;

import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

public class CustomLock
{
    private final WakeLock wakeLock;

    public CustomLock(PowerManager powerManager)
    {
        this.wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, getClass().getName());
    }

    public void acquire()
    {
        wakeLock.acquire();
    }

    public void release()
    {
        try
        {
            wakeLock.release();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}