package com.mauriciotogneri.fcs.sensors;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.mauriciotogneri.fcs.database.AccelerometerData;

public class AccelerometerSensor implements SensorEventListener
{
    private final SensorManager sensorManager;
    private final Sensor sensor;
    private final AccelerometerListener listener;

    private int rate = 0;
    private long lastTimestamp = 0;

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

    public void setRate(int rate)
    {
        this.rate = 1000 / rate;
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

    public void stop()
    {
        sensorManager.unregisterListener(this);
    }

    public interface AccelerometerListener
    {
        void onAccelerometerData(AccelerometerData data);
    }
}