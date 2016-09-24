package com.mauriciotogneri.flightrecorder;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.WindowManager;
import android.widget.Chronometer;

import com.mauriciotogneri.flightrecorder.DataService.ServiceBinder;
import com.mauriciotogneri.flightrecorder.fragments.AccelerometerFragment;
import com.mauriciotogneri.flightrecorder.fragments.BaseFragment;
import com.mauriciotogneri.flightrecorder.fragments.LocationFragment;
import com.mauriciotogneri.flightrecorder.fragments.RotationFragment;
import com.mauriciotogneri.flightrecorder.log.FlightLog;
import com.mauriciotogneri.flightrecorder.sensors.AccelerometerSensor.AccelerometerListener;
import com.mauriciotogneri.flightrecorder.sensors.LocationSensor.LocationListener;
import com.mauriciotogneri.flightrecorder.sensors.RotationSensor.RotationListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class MainActivity extends FragmentActivity implements AccelerometerListener, RotationListener, LocationListener
{
    private ServiceConnection serviceConnection;
    private FlightLog flightLog;
    private AccelerometerFragment accelerometerFragment;
    private RotationFragment rotationFragment;
    private LocationFragment locationFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setupLog();
        setupFragments();
        setupService();
    }

    private void setupLog()
    {
        try
        {
            SimpleDateFormat sourceDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.getDefault());
            File folder = new File(Environment.getExternalStorageDirectory() + "/flightrecorder", sourceDateFormat.format(System.currentTimeMillis()));
            folder.mkdirs();

            flightLog = new FlightLog(folder);
        }
        catch (IOException e)
        {
            // TODO
            e.printStackTrace();
        }
    }

    private void setupFragments()
    {
        accelerometerFragment = new AccelerometerFragment();
        rotationFragment = new RotationFragment();
        locationFragment = new LocationFragment();

        String[] names = new String[3];
        names[0] = getString(R.string.screen_accelerometer);
        names[1] = getString(R.string.screen_rotation);
        names[2] = getString(R.string.screen_location);

        BaseFragment[] fragments = new BaseFragment[3];
        fragments[0] = accelerometerFragment;
        fragments[1] = rotationFragment;
        fragments[2] = locationFragment;

        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager(), names, fragments));
        viewPager.setOffscreenPageLimit(3);
        viewPager.setCurrentItem(0);
    }

    private void setupService()
    {
        serviceConnection = new ServiceConnection()
        {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service)
            {
                onConnected(service);
            }

            @Override
            public void onServiceDisconnected(ComponentName name)
            {
                onDisconnected();
            }
        };

        Intent intent = new Intent(this, DataService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private void onConnected(IBinder service)
    {
        ServiceBinder binder = (ServiceBinder) service;
        DataService dataService = binder.getService();
        dataService.startRecording(5, this, 5, this, 1000, this);

        Chronometer chronometer = (Chronometer) findViewById(R.id.chronometer);
        chronometer.start();
    }

    private void onDisconnected()
    {
        finish();
    }

    @Override
    public void onAccelerometerData(long timestamp, float x, float y, float z)
    {
        accelerometerFragment.onAccelerometerData(timestamp, x, y, z);
        flightLog.onAccelerometerData(timestamp, x, y, z);
    }

    @Override
    public void onRotationData(long timestamp, float x, float y, float z)
    {
        rotationFragment.onRotationData(timestamp, x, y, z);
        flightLog.onRotationData(timestamp, x, y, z);
    }

    @Override
    public void onLocationData(long timestamp, double latitude, double longitude, double altitude, float accuracy, float speed, float bearing)
    {
        locationFragment.onLocationData(timestamp, latitude, longitude, altitude, accuracy, speed, bearing);
        flightLog.onLocationData(timestamp, latitude, longitude, altitude, accuracy, speed, bearing);
    }

    @Override
    protected void onDestroy()
    {
        unbindService(serviceConnection);
        flightLog.close();

        super.onDestroy();
    }
}