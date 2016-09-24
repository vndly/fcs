package com.mauriciotogneri.flightrecorder.log;

import com.mauriciotogneri.flightrecorder.sensors.AccelerometerSensor.AccelerometerListener;
import com.mauriciotogneri.flightrecorder.sensors.LocationSensor.LocationListener;
import com.mauriciotogneri.flightrecorder.sensors.RotationSensor.RotationListener;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

public class FlightLog implements AccelerometerListener, RotationListener, LocationListener
{
    private final SensorLog accelerometerLog;
    private final SensorLog rotationLog;
    private final SensorLog locationLog;

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.####");

    public FlightLog(File parent) throws IOException
    {
        this.accelerometerLog = new SensorLog(new File(parent, "accelerometer.csv"));
        this.rotationLog = new SensorLog(new File(parent, "rotation.csv"));
        this.locationLog = new SensorLog(new File(parent, "location.csv"));
    }

    @Override
    public void onAccelerometerData(long timestamp, float x, float y, float z)
    {
        accelerometerLog.log(timestamp, DECIMAL_FORMAT.format(x), DECIMAL_FORMAT.format(y), DECIMAL_FORMAT.format(z));
    }

    @Override
    public void onRotationData(long timestamp, float x, float y, float z)
    {
        rotationLog.log(timestamp, DECIMAL_FORMAT.format(x), DECIMAL_FORMAT.format(y), DECIMAL_FORMAT.format(z));
    }

    @Override
    public void onLocationData(long timestamp, double latitude, double longitude, double altitude, float accuracy, float speed, float bearing)
    {
        locationLog.log(
                timestamp,
                DECIMAL_FORMAT.format(latitude),
                DECIMAL_FORMAT.format(longitude),
                DECIMAL_FORMAT.format(altitude),
                DECIMAL_FORMAT.format(accuracy),
                DECIMAL_FORMAT.format(speed),
                DECIMAL_FORMAT.format(bearing));
    }

    public void close()
    {
        accelerometerLog.close();
        rotationLog.close();
    }
}