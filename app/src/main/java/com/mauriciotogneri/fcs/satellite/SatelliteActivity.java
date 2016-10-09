package com.mauriciotogneri.fcs.satellite;

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

import com.mauriciotogneri.fcs.satellite.DataService.ServiceBinder;
import com.mauriciotogneri.fcs.R;
import com.mauriciotogneri.fcs.satellite.database.AccelerometerData;
import com.mauriciotogneri.fcs.satellite.database.Database;
import com.mauriciotogneri.fcs.satellite.database.LocationData;
import com.mauriciotogneri.fcs.satellite.database.RotationData;
import com.mauriciotogneri.fcs.satellite.fragments.AccelerometerFragment;
import com.mauriciotogneri.fcs.satellite.fragments.BaseFragment;
import com.mauriciotogneri.fcs.satellite.fragments.LocationFragment;
import com.mauriciotogneri.fcs.satellite.fragments.RotationFragment;
import com.mauriciotogneri.fcs.satellite.log.FlightLog;
import com.mauriciotogneri.fcs.satellite.sensors.AccelerometerSensor.AccelerometerListener;
import com.mauriciotogneri.fcs.satellite.sensors.LocationSensor.LocationListener;
import com.mauriciotogneri.fcs.satellite.sensors.RotationSensor.RotationListener;
import com.mauriciotogneri.fcs.util.DateUtil;

import java.io.File;
import java.io.IOException;

public class SatelliteActivity extends FragmentActivity implements AccelerometerListener, RotationListener, LocationListener
{
    private ServiceConnection serviceConnection;
    private FlightLog flightLog;
    private AccelerometerFragment accelerometerFragment;
    private RotationFragment rotationFragment;
    private LocationFragment locationFragment;
    private Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.satellite_main);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setupLog();
        setupFragments();
        setupService();

        this.database = new Database();
    }

    private void setupLog()
    {
        try
        {
            String timestamp = DateUtil.format(System.currentTimeMillis());
            File folder = new File(Environment.getExternalStorageDirectory() + "/fcs", timestamp);

            if (folder.exists() || folder.mkdirs())
            {
                flightLog = new FlightLog(folder);
            }
            else
            {
                throw new RuntimeException();
            }
        }
        catch (IOException e)
        {
            throw new RuntimeException();
        }
    }

    private void setupFragments()
    {
        accelerometerFragment = new AccelerometerFragment();
        rotationFragment = new RotationFragment();
        locationFragment = new LocationFragment();

        String[] names = new String[3];
        names[0] = getString(R.string.screen_satellite_accelerometer);
        names[1] = getString(R.string.screen_satellite_rotation);
        names[2] = getString(R.string.screen_satellite_location);

        BaseFragment[] fragments = new BaseFragment[3];
        fragments[0] = accelerometerFragment;
        fragments[1] = rotationFragment;
        fragments[2] = locationFragment;

        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(new PagerAdapter(this, getSupportFragmentManager(), names, fragments));
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
        dataService.startRecording(10, this, 10, this, 500, this);

        Chronometer chronometer = (Chronometer) findViewById(R.id.chronometer);
        chronometer.start();
    }

    private void onDisconnected()
    {
        finish();
    }

    @Override
    public void onAccelerometerData(AccelerometerData data)
    {
        accelerometerFragment.onAccelerometerData(data);
        flightLog.onAccelerometerData(data);
        database.onAccelerometerData(data);
    }

    @Override
    public void onRotationData(RotationData data)
    {
        rotationFragment.onRotationData(data);
        flightLog.onRotationData(data);
        database.onRotationData(data);
    }

    @Override
    public void onLocationData(LocationData data)
    {
        locationFragment.onLocationData(data);
        flightLog.onLocationData(data);
        database.onLocationData(data);
    }

    @Override
    protected void onDestroy()
    {
        unbindService(serviceConnection);
        flightLog.close();

        super.onDestroy();
    }
}