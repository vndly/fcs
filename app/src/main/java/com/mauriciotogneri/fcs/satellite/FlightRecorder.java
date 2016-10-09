package com.mauriciotogneri.fcs.satellite;

import android.content.Context;
import android.hardware.SensorManager;
import android.os.PowerManager;

import com.mauriciotogneri.fcs.satellite.sensors.AccelerometerSensor;
import com.mauriciotogneri.fcs.satellite.sensors.BarometerSensor;
import com.mauriciotogneri.fcs.satellite.sensors.LocationSensor;
import com.mauriciotogneri.fcs.satellite.sensors.RotationSensor;
import com.mauriciotogneri.fcs.satellite.sensors.SensorListener;
import com.mauriciotogneri.fcs.util.CustomLock;

public class FlightRecorder
{
    private final AccelerometerSensor accelerometerSensor;
    private final RotationSensor rotationSensor;
    private final BarometerSensor barometerSensor;
    private final LocationSensor locationSensor;
    private final CustomLock lock;

    public FlightRecorder(
            Context context,
            SensorListener sensorListener)
    {
        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        this.accelerometerSensor = new AccelerometerSensor(sensorManager, sensorListener);
        this.rotationSensor = new RotationSensor(sensorManager, sensorListener);
        this.barometerSensor = new BarometerSensor(sensorManager, sensorListener);
        this.locationSensor = new LocationSensor(context, sensorListener);

        this.lock = new CustomLock((PowerManager) context.getSystemService(Context.POWER_SERVICE));
        this.lock.acquire();
    }

    public void start()
    {
        accelerometerSensor.start();
        rotationSensor.start();
        barometerSensor.start();
        locationSensor.start();
    }

    public void stop()
    {
        accelerometerSensor.stop();
        rotationSensor.stop();
        barometerSensor.stop();
        locationSensor.stop();

        lock.release();
    }
}