package com.mauriciotogneri.fcs.satellite.sensors;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.mauriciotogneri.fcs.model.AccelerometerData;

public class AccelerometerSensor implements SensorEventListener
{
    private final SensorManager sensorManager;
    private final Sensor sensor;
    private final AccelerometerListener listener;

    private static final int rate = 10;
    private long lastTimestamp = 0;

    public AccelerometerSensor(SensorManager sensorManager, AccelerometerListener listener)
    {
        this.sensorManager = sensorManager;
        this.sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        this.listener = listener;
    }

    @Override
    public void onSensorChanged(SensorEvent event)
    {
        long now = System.currentTimeMillis();

        if (now - lastTimestamp > rate)
        {
            lastTimestamp = now;

            listener.onAccelerometerData(new AccelerometerData(
                    System.currentTimeMillis(),
                    event.values[0],
                    event.values[1],
                    event.values[2]));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {
    }

    public void start()
    {
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void stop()
    {
        sensorManager.unregisterListener(this);
    }

    public interface AccelerometerListener
    {
        void onAccelerometerData(AccelerometerData data);
    }
}