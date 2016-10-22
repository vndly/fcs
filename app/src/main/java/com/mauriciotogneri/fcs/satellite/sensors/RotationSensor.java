package com.mauriciotogneri.fcs.satellite.sensors;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.mauriciotogneri.fcs.model.RotationData;

public class RotationSensor implements SensorEventListener
{
    private final SensorManager sensorManager;
    private final Sensor sensor;
    private final RotationListener listener;

    private static final int rate = 0;
    private long lastTimestamp = 0;

    public RotationSensor(SensorManager sensorManager, RotationListener listener)
    {
        this.sensorManager = sensorManager;
        this.sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        this.listener = listener;
    }

    @Override
    public void onSensorChanged(SensorEvent event)
    {
        /*float[] orientation = new float[3];
        float[] rotationMatrix = new float[9];
        float[] adjustedRotationMatrix = new float[9];

        int worldAxisForDeviceAxisX = SensorManager.AXIS_X;
        int worldAxisForDeviceAxisY = SensorManager.AXIS_Z;

        // Remap the axes as if the device screen was the instrument panel,
        // and adjust the rotation matrix for the device orientation.
        switch (windowManager.getDefaultDisplay().getRotation())
        {
            case Surface.ROTATION_0:
                worldAxisForDeviceAxisX = SensorManager.AXIS_X;
                worldAxisForDeviceAxisY = SensorManager.AXIS_Z;
                break;

            case Surface.ROTATION_90:
                worldAxisForDeviceAxisX = SensorManager.AXIS_Z;
                worldAxisForDeviceAxisY = SensorManager.AXIS_MINUS_X;
                break;

            case Surface.ROTATION_180:
                worldAxisForDeviceAxisX = SensorManager.AXIS_MINUS_X;
                worldAxisForDeviceAxisY = SensorManager.AXIS_MINUS_Z;
                break;

            case Surface.ROTATION_270:
                worldAxisForDeviceAxisX = SensorManager.AXIS_MINUS_Z;
                worldAxisForDeviceAxisY = SensorManager.AXIS_X;
                break;
        }

        SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values);
        SensorManager.remapCoordinateSystem(rotationMatrix, worldAxisForDeviceAxisX, worldAxisForDeviceAxisY, adjustedRotationMatrix);
        SensorManager.getOrientation(adjustedRotationMatrix, orientation);

        // Optionally convert the result from radians to degrees
        orientation[0] = (float) Math.toDegrees(orientation[0]);
        orientation[1] = (float) Math.toDegrees(orientation[1]);
        orientation[2] = (float) Math.toDegrees(orientation[2]);

        //listener.onRotationData(System.currentTimeMillis(), orientation[0], orientation[1], orientation[2]);*/

        // TODO
        // https://github.com/kibotu/KalmanRx

        long now = System.currentTimeMillis();

        if (now - lastTimestamp > rate)
        {
            lastTimestamp = now;

            float x = ((float) Math.round(event.values[0] * 1000.0f)) / 1000.0f;
            float y = ((float) Math.round(event.values[1] * 1000.0f)) / 1000.0f;
            float z = ((float) Math.round(event.values[2] * 1000.0f)) / 1000.0f;

            listener.onRotationData(new RotationData(System.currentTimeMillis(), x, y, z));
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

    public interface RotationListener
    {
        void onRotationData(RotationData data);
    }
}