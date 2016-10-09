package com.mauriciotogneri.fcs.satellite.fragments;

import android.widget.TextView;

import com.mauriciotogneri.fcs.R;
import com.mauriciotogneri.fcs.base.BaseFragment;
import com.mauriciotogneri.fcs.model.LocationData;
import com.mauriciotogneri.fcs.satellite.sensors.LocationSensor.LocationListener;

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
        return R.layout.subscreen_location;
    }

    @Override
    public void onLocationData(LocationData data)
    {
        lastValueLatitude.setText(String.valueOf(data.latitude()));
        lastValueLongitude.setText(String.valueOf(data.longitude()));
        lastValueAltitude.setText(String.format("%s m", data.altitude()));
        lastValueAccuracy.setText(String.format("%s m", data.accuracy()));
        lastValueSpeed.setText(String.format("%s m/s", data.speed()));
        lastValueBearing.setText(String.format("%sº", data.bearing()));
    }
}