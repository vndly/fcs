package com.mauriciotogneri.flightrecorder.fragments;

import android.widget.TextView;

import com.mauriciotogneri.flightrecorder.R;
import com.mauriciotogneri.flightrecorder.sensors.LocationSensor.LocationListener;

public class LocationFragment extends BaseFragment implements LocationListener
{
    private TextView lastValueLatitude;
    private TextView lastValueLongitude;
    private TextView lastValueAltitude;
    private TextView lastValueAccuracy;
    private TextView lastValueSpeed;
    private TextView lastValueBearing;

    @Override
    public void initialize()
    {
        lastValueLatitude = (TextView) findViewById(R.id.last_location_latitude);
        lastValueLongitude = (TextView) findViewById(R.id.last_location_longitude);
        lastValueAltitude = (TextView) findViewById(R.id.last_location_altitude);
        lastValueAccuracy = (TextView) findViewById(R.id.last_location_accuracy);
        lastValueSpeed = (TextView) findViewById(R.id.last_location_speed);
        lastValueBearing = (TextView) findViewById(R.id.last_location_bearing);
    }

    @Override
    public int layout()
    {
        return R.layout.screen_location;
    }

    @Override
    public void onLocationData(long timestamp, double latitude, double longitude, double altitude, float accuracy, float speed, float bearing)
    {
        lastValueLatitude.setText(String.valueOf(latitude));
        lastValueLongitude.setText(String.valueOf(longitude));
        lastValueAltitude.setText(String.valueOf(altitude));
        lastValueAccuracy.setText(String.valueOf(accuracy));
        lastValueSpeed.setText(String.valueOf(speed));
        lastValueBearing.setText(String.valueOf(bearing));
    }
}