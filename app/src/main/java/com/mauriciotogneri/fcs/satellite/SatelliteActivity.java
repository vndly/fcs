package com.mauriciotogneri.fcs.satellite;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.mauriciotogneri.fcs.R;
import com.mauriciotogneri.fcs.adapters.ViewPagerAdapter;
import com.mauriciotogneri.fcs.model.AccelerometerData;
import com.mauriciotogneri.fcs.model.BarometerData;
import com.mauriciotogneri.fcs.model.LocationData;
import com.mauriciotogneri.fcs.model.RotationData;
import com.mauriciotogneri.fcs.model.Session;
import com.mauriciotogneri.fcs.network.FirebaseNetworkSatellite;
import com.mauriciotogneri.fcs.satellite.fragments.AccelerometerFragment;
import com.mauriciotogneri.fcs.satellite.fragments.BarometerFragment;
import com.mauriciotogneri.fcs.satellite.fragments.LocationFragment;
import com.mauriciotogneri.fcs.satellite.fragments.RotationFragment;
import com.mauriciotogneri.fcs.satellite.fragments.SessionFragment;
import com.mauriciotogneri.fcs.satellite.log.FlightLog;
import com.mauriciotogneri.fcs.satellite.sensors.SensorListener;

public class SatelliteActivity extends AppCompatActivity implements SensorListener
{
    private FlightLog flightLog;
    private FlightRecorder flightRecorder;
    private AccelerometerFragment accelerometerFragment;
    private RotationFragment rotationFragment;
    private BarometerFragment barometerFragment;
    private LocationFragment locationFragment;
    private FirebaseNetworkSatellite firebaseNetwork;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.screen_satellite);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Session session = new Session();

        setupNetwork(session);
        setupLog(session);
        setupFragments(session);
        setupFlightRecorder();
    }

    public void setupNetwork(Session session)
    {
        this.firebaseNetwork = new FirebaseNetworkSatellite(session);
    }

    private void setupLog(Session session)
    {
        this.flightLog = FlightLog.create(session);
    }

    private void setupFragments(Session session)
    {
        SessionFragment sessionFragment = SessionFragment.newInstance(session);
        this.accelerometerFragment = new AccelerometerFragment();
        this.rotationFragment = new RotationFragment();
        this.barometerFragment = new BarometerFragment();
        this.locationFragment = new LocationFragment();

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(sessionFragment, getString(R.string.screen_satellite_session));
        adapter.addFragment(accelerometerFragment, getString(R.string.screen_satellite_accelerometer));
        adapter.addFragment(rotationFragment, getString(R.string.screen_satellite_rotation));
        adapter.addFragment(barometerFragment, getString(R.string.screen_satellite_barometer));
        adapter.addFragment(locationFragment, getString(R.string.screen_satellite_location));

        viewPager.setOffscreenPageLimit(4);
        viewPager.setCurrentItem(0);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupFlightRecorder()
    {
        this.flightRecorder = new FlightRecorder(this, this);
        this.flightRecorder.start();
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
    public void onBarometerData(BarometerData data)
    {
        barometerFragment.onBarometerData(data);
        flightLog.onBarometerData(data);
        firebaseNetwork.onBarometerData(data);
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
        flightLog.close();
        flightRecorder.stop();

        super.onDestroy();
    }
}