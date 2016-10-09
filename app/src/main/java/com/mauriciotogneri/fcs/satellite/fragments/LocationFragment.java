package com.mauriciotogneri.fcs.satellite.fragments;

import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mauriciotogneri.fcs.R;
import com.mauriciotogneri.fcs.base.BaseFragment;
import com.mauriciotogneri.fcs.model.LocationData;
import com.mauriciotogneri.fcs.satellite.sensors.LocationSensor.LocationListener;

public class LocationFragment extends BaseFragment implements OnMapReadyCallback,LocationListener
{
    private GoogleMap map;

    private Marker marker;

    private TextView lastValueAltitude;
    private TextView lastValueAccuracy;
    private TextView lastValueSpeed;
    private TextView lastValueBearing;

    @Override
    public void initialize()
    {
        lastValueAltitude = (TextView) findViewById(R.id.last_location_altitude);
        lastValueAccuracy = (TextView) findViewById(R.id.last_location_accuracy);
        lastValueSpeed = (TextView) findViewById(R.id.last_location_speed);
        lastValueBearing = (TextView) findViewById(R.id.last_location_bearing);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void center(float longitude, float latitude)
    {
        if (map != null)
        {
            LatLng position = new LatLng(longitude, latitude);

            if (marker != null)
            {
                map.animateCamera(CameraUpdateFactory.newLatLng(position));

                marker.setPosition(position);
            }
            else
            {
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 17));

                marker = map.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.airplane))
                        .position(position));
            }
        }
    }

    @Override
    public void onLocationData(LocationData data)
    {
        lastValueAltitude.setText(String.format("%s m", data.altitude()));
        lastValueAccuracy.setText(String.format("%s m", data.accuracy()));
        lastValueSpeed.setText(String.format("%s m/s", data.speed()));
        lastValueBearing.setText(String.format("%sº", data.bearing()));

        center(data.latitude(), data.longitude());
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        map = googleMap;
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setMapToolbarEnabled(false);
        map.getUiSettings().setMyLocationButtonEnabled(false);
    }

    @Override
    public int layout()
    {
        return R.layout.subscreen_location;
    }
}