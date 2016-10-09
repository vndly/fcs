package com.mauriciotogneri.fcs.satellite;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.mauriciotogneri.fcs.R;
import com.mauriciotogneri.fcs.adapters.ViewPagerAdapter;
import com.mauriciotogneri.fcs.model.AccelerometerData;
import com.mauriciotogneri.fcs.model.LocationData;
import com.mauriciotogneri.fcs.model.RotationData;
import com.mauriciotogneri.fcs.network.FirebaseNetwork;
import com.mauriciotogneri.fcs.satellite.DataService.ServiceBinder;
import com.mauriciotogneri.fcs.satellite.fragments.AccelerometerFragment;
import com.mauriciotogneri.fcs.satellite.fragments.LocationFragment;
import com.mauriciotogneri.fcs.satellite.fragments.RotationFragment;
import com.mauriciotogneri.fcs.satellite.fragments.SessionFragment;
import com.mauriciotogneri.fcs.satellite.log.FlightLog;
import com.mauriciotogneri.fcs.satellite.sensors.AccelerometerSensor.AccelerometerListener;
import com.mauriciotogneri.fcs.satellite.sensors.LocationSensor.LocationListener;
import com.mauriciotogneri.fcs.satellite.sensors.RotationSensor.RotationListener;
import com.mauriciotogneri.fcs.util.DateUtil;

import java.io.File;
import java.io.IOException;

public class SatelliteActivity extends AppCompatActivity implements AccelerometerListener, RotationListener, LocationListener
{
    private ServiceConnection serviceConnection;
    private FlightLog flightLog;
    private SessionFragment sessionFragment;
    private AccelerometerFragment accelerometerFragment;
    private RotationFragment rotationFragment;
    private LocationFragment locationFragment;
    private FirebaseNetwork firebaseNetwork;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.screen_satellite);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setupLog();
        setupFragments();
        setupService();

        this.firebaseNetwork = new FirebaseNetwork();
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
        sessionFragment = new SessionFragment();
        accelerometerFragment = new AccelerometerFragment();
        rotationFragment = new RotationFragment();
        locationFragment = new LocationFragment();

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(sessionFragment, getString(R.string.screen_satellite_session));
        adapter.addFragment(accelerometerFragment, getString(R.string.screen_satellite_accelerometer));
        adapter.addFragment(rotationFragment, getString(R.string.screen_satellite_rotation));
        adapter.addFragment(locationFragment, getString(R.string.screen_satellite_location));

        viewPager.setOffscreenPageLimit(3);
        viewPager.setCurrentItem(0);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
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

        sessionFragment.start();
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
        firebaseNetwork.onAccelerometerData(data);
    }

    @Override
    public void onRotationData(RotationData data)
    {
        rotationFragment.onRotationData(data);
        flightLog.onRotationData(data);
        firebaseNetwork.onRotationData(data);
    }

    @Override
    public void onLocationData(LocationData data)
    {
        locationFragment.onLocationData(data);
        flightLog.onLocationData(data);
        firebaseNetwork.onLocationData(data);
    }

    @Override
    protected void onDestroy()
    {
        unbindService(serviceConnection);
        flightLog.close();

        super.onDestroy();
    }
}