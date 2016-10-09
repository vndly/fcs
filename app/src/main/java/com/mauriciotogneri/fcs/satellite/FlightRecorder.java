package com.mauriciotogneri.fcs.satellite;

import android.content.Context;
import android.hardware.SensorManager;
import android.os.PowerManager;

import com.mauriciotogneri.fcs.satellite.sensors.AccelerometerSensor;
import com.mauriciotogneri.fcs.satellite.sensors.AccelerometerSensor.AccelerometerListener;
import com.mauriciotogneri.fcs.satellite.sensors.LocationSensor;
import com.mauriciotogneri.fcs.satellite.sensors.LocationSensor.LocationListener;
import com.mauriciotogneri.fcs.satellite.sensors.RotationSensor;
import com.mauriciotogneri.fcs.satellite.sensors.RotationSensor.RotationListener;
import com.mauriciotogneri.fcs.util.CustomLock;

public class FlightRecorder
{
    private final AccelerometerSensor accelerometerSensor;
    private final RotationSensor rotationSensor;
    private final LocationSensor locationSensor;
    private final CustomLock lock;

    public FlightRecorder(
            Context context,
            AccelerometerListener accelerometerListener,
            RotationListener rotationListener,
            LocationListener locationListener)
    {
        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        this.accelerometerSensor = new AccelerometerSensor(sensorManager, accelerometerListener);
        this.rotationSensor = new RotationSensor(sensorManager, rotationListener);
        this.locationSensor = new LocationSensor(context, locationListener);

        this.lock = new CustomLock((PowerManager) context.getSystemService(Context.POWER_SERVICE));
        this.lock.acquire();
    }

    public void start()
    {
        accelerometerSensor.start();
        rotationSensor.start();
        locationSensor.start();
    }

    public void stop()
    {
        accelerometerSensor.stop();
        rotationSensor.stop();
        locationSensor.stop();

        lock.release();
    }
}