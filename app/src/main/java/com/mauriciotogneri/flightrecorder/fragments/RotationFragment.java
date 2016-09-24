package com.mauriciotogneri.flightrecorder.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mauriciotogneri.flightrecorder.R;
import com.mauriciotogneri.flightrecorder.sensors.RotationSensor.RotationListener;

public class RotationFragment extends Fragment implements RotationListener
{
    private TextView lastValueX;
    private TextView lastValueY;
    private TextView lastValueZ;

    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.screen_rotation, container, false);
    }

    @Override
    public final void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        lastValueX = (TextView) getView().findViewById(R.id.last_rotation_x);
        lastValueY = (TextView) getView().findViewById(R.id.last_rotation_y);
        lastValueZ = (TextView) getView().findViewById(R.id.last_rotation_z);
    }

    @Override
    public void onRotationData(long timestamp, float x, float y, float z)
    {
        lastValueX.setText(String.valueOf(x));
        lastValueY.setText(String.valueOf(y));
        lastValueZ.setText(String.valueOf(z));
    }
}