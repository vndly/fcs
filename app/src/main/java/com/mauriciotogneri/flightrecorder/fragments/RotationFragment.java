package com.mauriciotogneri.flightrecorder.fragments;

import android.widget.TextView;

import com.mauriciotogneri.flightrecorder.R;
import com.mauriciotogneri.flightrecorder.sensors.RotationSensor.RotationListener;

public class RotationFragment extends BaseFragment implements RotationListener
{
    private TextView lastValueX;
    private TextView lastValueY;
    private TextView lastValueZ;

    @Override
    public void initialize()
    {
        lastValueX = (TextView) getView().findViewById(R.id.last_rotation_x);
        lastValueY = (TextView) getView().findViewById(R.id.last_rotation_y);
        lastValueZ = (TextView) getView().findViewById(R.id.last_rotation_z);
    }

    @Override
    public int layout()
    {
        return R.layout.screen_rotation;
    }

    @Override
    public void onRotationData(long timestamp, float x, float y, float z)
    {
        lastValueX.setText(String.valueOf(x));
        lastValueY.setText(String.valueOf(y));
        lastValueZ.setText(String.valueOf(z));
    }
}