package com.mauriciotogneri.fcs.satellite.log;

import android.os.Environment;

import com.mauriciotogneri.fcs.model.AccelerometerData;
import com.mauriciotogneri.fcs.model.LocationData;
import com.mauriciotogneri.fcs.model.RotationData;
import com.mauriciotogneri.fcs.model.Session;
import com.mauriciotogneri.fcs.satellite.sensors.SensorListener;

import java.io.File;
import java.io.IOException;

public class FlightLog implements SensorListener
{
    private final SensorLog accelerometerLog;
    private final SensorLog rotationLog;
    private final SensorLog locationLog;
    private boolean closed = false;

    public FlightLog(File parent) throws IOException
    {
        this.accelerometerLog = new SensorLog(new File(parent, "accelerometer.csv"));
        this.rotationLog = new SensorLog(new File(parent, "rotation.csv"));
        this.locationLog = new SensorLog(new File(parent, "location.csv"));
    }

    @Override
    public void onAccelerometerData(AccelerometerData data)
    {
        if (!closed)
        {
            accelerometerLog.log(
                    data.timestamp(),
                    data.x(),
                    data.y(),
                    data.z());
        }
    }

    @Override
    public void onRotationData(RotationData data)
    {
        if (!closed)
        {
            rotationLog.log(
                    data.timestamp(),
                    data.x(),
                    data.y(),
                    data.z());
        }
    }

    @Override
    public void onLocationData(LocationData data)
    {
        if (!closed)
        {
            locationLog.log(
                    data.timestamp(),
                    data.latitude(),
                    data.longitude(),
                    data.altitude(),
                    data.accuracy(),
                    data.speed(),
                    data.bearing()
            );
        }
    }

    public void close()
    {
        closed = true;
        accelerometerLog.close();
        rotationLog.close();
        locationLog.close();
    }

    public static FlightLog create(Session session)
    {
        try
        {
            File folder = new File(Environment.getExternalStorageDirectory() + "/fcs", session.id());

            if (folder.exists() || folder.mkdirs())
            {
                return new FlightLog(folder);
            }
            else
            {
                throw new RuntimeException();
            }
        }
        catch (IOException e)
        {
            throw new RuntimeException();
        }
    }
}