package com.mauriciotogneri.fcs.ground;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.mauriciotogneri.fcs.R;
import com.mauriciotogneri.fcs.adapters.ViewPagerAdapter;
import com.mauriciotogneri.fcs.model.AccelerometerData;
import com.mauriciotogneri.fcs.model.LocationData;
import com.mauriciotogneri.fcs.model.RotationData;
import com.mauriciotogneri.fcs.network.FirebaseNetworkGround;
import com.mauriciotogneri.fcs.satellite.fragments.AccelerometerFragment;
import com.mauriciotogneri.fcs.satellite.sensors.SensorListener;

public class GroundActivity extends AppCompatActivity implements SensorListener
{
    private AccelerometerFragment accelerometerFragment;
    private FirebaseNetworkGround firebaseNetwork;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ground_main);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setupNetwork();
        setupFragments();
    }

    private void setupNetwork()
    {
        this.firebaseNetwork = new FirebaseNetworkGround(this);
    }

    private void setupFragments()
    {
        this.accelerometerFragment = new AccelerometerFragment();

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(accelerometerFragment, "ONE");
        adapter.addFragment(new OneFragment(), "TWO");
        adapter.addFragment(new OneFragment(), "THREE");

        viewPager.setOffscreenPageLimit(3);
        viewPager.setCurrentItem(0);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onAccelerometerData(AccelerometerData data)
    {
        accelerometerFragment.onAccelerometerData(data);
    }

    @Override
    public void onLocationData(LocationData data)
    {

    }

    @Override
    public void onRotationData(RotationData data)
    {

    }
}