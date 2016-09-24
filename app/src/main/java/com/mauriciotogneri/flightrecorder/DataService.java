package com.mauriciotogneri.flightrecorder;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.os.PowerManager;
import android.view.WindowManager;

import com.mauriciotogneri.flightrecorder.sensors.AccelerometerSensor;
import com.mauriciotogneri.flightrecorder.sensors.AccelerometerSensor.AccelerometerListener;
import com.mauriciotogneri.flightrecorder.sensors.RotationSensor;
import com.mauriciotogneri.flightrecorder.sensors.RotationSensor.RotationListener;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class DataService extends Service implements AccelerometerListener, RotationListener
{
    private SensorManager sensorManager;
    private AccelerometerSensor accelerometerSensor;
    private RotationSensor rotationSensor;

    private CustomLock lock;

    private AccelerometerListener accelerometerListener;
    private BufferedWriter bufferedWriter;

    private DecimalFormat decimalFormat = new DecimalFormat("#.####");

    private static final int SAMPLES_PER_SECOND = 5;
    private static final String COLUMN_SEPARATOR = ",";

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
        rotationSensor = new RotationSensor(sensorManager, this, (WindowManager) getSystemService(Context.WINDOW_SERVICE));

        PowerManager powerManager = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        lock = new CustomLock(powerManager);
        lock.acquire();
    }

    public void startRecording(AccelerometerListener accelerometerListener)
    {
        this.accelerometerListener = accelerometerListener;

        setupFile();

        sensorManager.registerListener(accelerometerSensor, accelerometerSensor.sensor(), (1000 / SAMPLES_PER_SECOND) * 1000);
        sensorManager.registerListener(rotationSensor, rotationSensor.sensor(), (1000 / SAMPLES_PER_SECOND) * 1000);
    }

    @Override
    public void onAccelerometerData(long timestamp, float x, float y, float z)
    {
        writeLine(timestamp + COLUMN_SEPARATOR + decimalFormat.format(x) + COLUMN_SEPARATOR + decimalFormat.format(y) + COLUMN_SEPARATOR + decimalFormat.format(z));

        if (accelerometerListener != null)
        {
            accelerometerListener.onAccelerometerData(timestamp, x, y, z);
        }
    }

    @Override
    public void onRotationData(long timestamp, float x, float y, float z)
    {

    }

    private void setupFile()
    {
        try
        {
            SimpleDateFormat sourceDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
            File file = new File(Environment.getExternalStorageDirectory() + "/" + sourceDateFormat.format(System.currentTimeMillis()) + ".csv");

            if (file.createNewFile())
            {
                bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
                writeLine("TIME" + COLUMN_SEPARATOR + "X" + COLUMN_SEPARATOR + "Y" + COLUMN_SEPARATOR + "Z");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void writeLine(String text)
    {
        if (bufferedWriter != null)
        {
            try
            {
                bufferedWriter.write(text + "\n");
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy()
    {
        accelerometerSensor.stop();
        rotationSensor.stop();

        lock.release();

        try
        {
            bufferedWriter.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

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