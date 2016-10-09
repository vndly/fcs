package com.mauriciotogneri.fcs.satellite;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import com.mauriciotogneri.fcs.util.CustomLock;
import com.mauriciotogneri.fcs.satellite.database.AccelerometerData;
import com.mauriciotogneri.fcs.satellite.database.LocationData;
import com.mauriciotogneri.fcs.satellite.database.RotationData;
import com.mauriciotogneri.fcs.satellite.sensors.AccelerometerSensor;
import com.mauriciotogneri.fcs.satellite.sensors.AccelerometerSensor.AccelerometerListener;
import com.mauriciotogneri.fcs.satellite.sensors.LocationSensor;
import com.mauriciotogneri.fcs.satellite.sensors.LocationSensor.LocationListener;
import com.mauriciotogneri.fcs.satellite.sensors.RotationSensor;
import com.mauriciotogneri.fcs.satellite.sensors.RotationSensor.RotationListener;

public class DataService extends Service implements AccelerometerListener, RotationListener, LocationListener
{
    private SensorManager sensorManager;
    private AccelerometerSensor accelerometerSensor;
    private RotationSensor rotationSensor;
    private LocationSensor locationSensor;
    private AccelerometerListener accelerometerListener;
    private RotationListener rotationListener;
    private LocationListener locationListener;
    private CustomLock lock;

    @Override
    public IBinder onBind(Intent intent)
    {
        return new ServiceBinder(this);
    }

    @Override
    public void onCreate()
    {
        super.onCreate();

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometerSensor = new AccelerometerSensor(sensorManager, this);
        rotationSensor = new RotationSensor(sensorManager, this);
        locationSensor = new LocationSensor(this, this);

        lock = new CustomLock((PowerManager) this.getSystemService(Context.POWER_SERVICE));
        lock.acquire();
    }

    public void startRecording(int accelerometerSampleRate,
                               AccelerometerListener accelerometerListener,
                               int rotationSampleRate,
                               RotationListener rotationListener,
                               int locationSampleRate,
                               LocationListener locationListener)
    {
        this.accelerometerListener = accelerometerListener;
        this.rotationListener = rotationListener;
        this.locationListener = locationListener;

        accelerometerSensor.setRate(accelerometerSampleRate);
        rotationSensor.setRate(rotationSampleRate);

        sensorManager.registerListener(accelerometerSensor, accelerometerSensor.sensor(), SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(rotationSensor, rotationSensor.sensor(), SensorManager.SENSOR_DELAY_NORMAL);
        locationSensor.requestLocationUpdates(locationSampleRate);
    }

    @Override
    public void onAccelerometerData(AccelerometerData data)
    {
        Log.d("ACCELEROMETER", String.valueOf(data.timestamp()));

        if (accelerometerListener != null)
        {
            accelerometerListener.onAccelerometerData(data);
        }
    }

    @Override
    public void onRotationData(RotationData data)
    {
        if (rotationListener != null)
        {
            rotationListener.onRotationData(data);
        }
    }

    @Override
    public void onLocationData(LocationData data)
    {
        if (locationListener != null)
        {
            locationListener.onLocationData(data);
        }
    }

    @Override
    public void onDestroy()
    {
        accelerometerSensor.stop();
        rotationSensor.stop();
        locationSensor.stop();

        lock.release();

        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        return START_STICKY;
    }

    public static class ServiceBinder extends Binder
    {
        private final DataService dataService;

        public ServiceBinder(DataService dataService)
        {
            this.dataService = dataService;
        }

        public DataService getService()
        {
            return dataService;
        }
    }
}