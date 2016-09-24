package com.mauriciotogneri.flightrecorder.sensors;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class AccelerometerSensor implements SensorEventListener
{
    private final SensorManager sensorManager;
    private final Sensor sensor;
    private final AccelerometerListener listener;

    public AccelerometerSensor(SensorManager sensorManager, AccelerometerListener listener)
    {
        this.sensorManager = sensorManager;
        this.sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        this.listener = listener;
    }

    public Sensor sensor()
    {
        return sensor;
    }

    @Override
    public void onSensorChanged(SensorEvent event)
    {
        listener.onAccelerometerData(System.currentTimeMillis(), event.values[0], event.values[1], event.values[2]);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {
    }

    public void stop()
    {
        sensorManager.unregisterListener(this);
    }

    public interface AccelerometerListener
    {
        void onAccelerometerData(long timestamp, float x, float y, float z);
    }
}