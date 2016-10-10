package com.mauriciotogneri.fcs.satellite.fragments;

import android.widget.TextView;

import com.mauriciotogneri.fcs.widgets.CompassView;
import com.mauriciotogneri.fcs.R;
import com.mauriciotogneri.fcs.base.BaseFragment;
import com.mauriciotogneri.fcs.model.RotationData;
import com.mauriciotogneri.fcs.satellite.sensors.RotationSensor.RotationListener;

public class RotationFragment extends BaseFragment implements RotationListener
{
    private TextView lastValueX;
    private TextView lastValueY;
    private TextView lastValueZ;
    private CompassView compassView;

    @Override
    public void initialize()
    {
        lastValueX = (TextView) findViewById(R.id.last_rotation_x);
        lastValueY = (TextView) findViewById(R.id.last_rotation_y);
        lastValueZ = (TextView) findViewById(R.id.last_rotation_z);
        compassView = (CompassView) findViewById(R.id.compassView);
    }

    @Override
    public int layout()
    {
        return R.layout.subscreen_rotation;
    }

    @Override
    public void onRotationData(RotationData data)
    {
        lastValueX.setText(String.format("%sº", (int)(data.x() * 180)));
        lastValueY.setText(String.format("%sº", (int)(data.y() * 180)));
        lastValueZ.setText(String.format("%sº", (int)(data.z() * 180)));

        compassView.setPitch(data.x() * -180);
        compassView.setRoll(data.y() * 180);
        compassView.setBearing((data.z() * -180) - 180);
        compassView.invalidate();
    }
}