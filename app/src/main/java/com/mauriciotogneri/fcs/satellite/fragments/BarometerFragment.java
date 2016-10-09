package com.mauriciotogneri.fcs.satellite.fragments;

import android.widget.TextView;

import com.mauriciotogneri.fcs.R;
import com.mauriciotogneri.fcs.base.BaseFragment;
import com.mauriciotogneri.fcs.model.BarometerData;
import com.mauriciotogneri.fcs.satellite.sensors.BarometerSensor.BarometerListener;

public class BarometerFragment extends BaseFragment implements BarometerListener
{
    private TextView lastValuePressure;

    @Override
    public void initialize()
    {
        lastValuePressure = (TextView) findViewById(R.id.last_barometer_pressure);
    }

    @Override
    public int layout()
    {
        return R.layout.subscreen_barometer;
    }

    @Override
    public void onBarometerData(BarometerData data)
    {
        lastValuePressure.setText(String.valueOf(data.pressure()));
    }
}