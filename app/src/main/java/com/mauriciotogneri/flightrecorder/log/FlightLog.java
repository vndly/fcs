package com.mauriciotogneri.flightrecorder.log;

import com.mauriciotogneri.flightrecorder.database.AccelerometerData;
import com.mauriciotogneri.flightrecorder.database.LocationData;
import com.mauriciotogneri.flightrecorder.database.RotationData;
import com.mauriciotogneri.flightrecorder.sensors.AccelerometerSensor.AccelerometerListener;
import com.mauriciotogneri.flightrecorder.sensors.LocationSensor.LocationListener;
import com.mauriciotogneri.flightrecorder.sensors.RotationSensor.RotationListener;

import java.io.File;
import java.io.IOException;

public class FlightLog implements AccelerometerListener, RotationListener, LocationListener
{
    private final SensorLog accelerometerLog;
    private final SensorLog rotationLog;
    private final SensorLog locationLog;

    public FlightLog(File parent) throws IOException
    {
        this.accelerometerLog = new SensorLog(new File(parent, "accelerometer.csv"));
        this.rotationLog = new SensorLog(new File(parent, "rotation.csv"));
        this.locationLog = new SensorLog(new File(parent, "location.csv"));
    }

    @Override
    public void onAccelerometerData(AccelerometerData data)
    {
        accelerometerLog.log(data.timestamp(), data.x(), data.y(), data.z());
    }

    @Override
    public void onRotationData(RotationData data)
    {
        rotationLog.log(data.timestamp(), data.x(), data.y(), data.z());
    }

    @Override
    public void onLocationData(LocationData data)
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

    public void close()
    {
        accelerometerLog.close();
        rotationLog.close();
    }
}