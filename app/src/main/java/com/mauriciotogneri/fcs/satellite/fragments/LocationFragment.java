package com.mauriciotogneri.fcs.satellite.fragments;

import android.graphics.Color;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mauriciotogneri.fcs.R;
import com.mauriciotogneri.fcs.base.BaseFragment;
import com.mauriciotogneri.fcs.model.LocationData;
import com.mauriciotogneri.fcs.model.RotationData;
import com.mauriciotogneri.fcs.satellite.sensors.LocationSensor.LocationListener;
import com.mauriciotogneri.fcs.satellite.sensors.RotationSensor.RotationListener;

public class LocationFragment extends BaseFragment implements OnMapReadyCallback, LocationListener, RotationListener
{
    private GoogleMap map;

    private Marker marker;
    private Circle circle;

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

    private void center(float longitude, float latitude, float accuracy)
    {
        if (map != null)
        {
            LatLng position = new LatLng(longitude, latitude);

            if (marker != null)
            {
                map.animateCamera(CameraUpdateFactory.newLatLng(position));

                marker.setPosition(position);

                circle.setCenter(position);
                circle.setRadius(accuracy);
            }
            else
            {
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 17));

                marker = map.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.airplane))
                        .anchor(0.5f, 0.5f)
                        .position(position));

                CircleOptions circleOptions = new CircleOptions()
                        .center(position)
                        .fillColor(Color.argb(50, 0, 0, 255))
                        .strokeWidth(0)
                        .radius(accuracy);

                circle = map.addCircle(circleOptions);
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

        center(data.latitude(), data.longitude(), data.accuracy());
    }

    @Override
    public void onRotationData(RotationData data)
    {
        if (marker != null)
        {
            marker.setRotation((data.z() * -180) - map.getCameraPosition().bearing);
        }
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