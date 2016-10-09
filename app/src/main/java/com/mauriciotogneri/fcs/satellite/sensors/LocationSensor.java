package com.mauriciotogneri.fcs.satellite.sensors;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.mauriciotogneri.fcs.model.LocationData;

public class LocationSensor implements ConnectionCallbacks, OnConnectionFailedListener, LocationListener
{
    private final GoogleApiClient googleApiClient;
    private final LocationListener listener;

    private boolean requestedLocationUpdates = false;
    private static final int rate = 500;

    public LocationSensor(Context context, LocationListener listener)
    {
        this.listener = listener;

        this.googleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        this.googleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle)
    {
        if (requestedLocationUpdates)
        {
            start();
        }
    }

    @Override
    public void onConnectionSuspended(int i)
    {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
    {
    }

    @Override
    public void onLocationChanged(Location location)
    {
        listener.onLocationData(new LocationData(
                System.currentTimeMillis(),
                location.getLatitude(),
                location.getLongitude(),
                location.getAltitude(),
                location.getAccuracy(),
                location.getSpeed(),
                location.getBearing()
        ));
    }

    public void start()
    {
        if (googleApiClient.isConnected())
        {
            LocationRequest locationRequest = new LocationRequest();
            locationRequest.setInterval(rate);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        }
        else
        {
            this.requestedLocationUpdates = true;
        }
    }

    public void stop()
    {
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
    }

    public interface LocationListener
    {
        void onLocationData(LocationData data);
    }
}