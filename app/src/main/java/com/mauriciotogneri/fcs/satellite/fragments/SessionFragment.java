package com.mauriciotogneri.fcs.satellite.fragments;

import android.widget.Chronometer;

import com.mauriciotogneri.fcs.R;

public class SessionFragment extends BaseFragment
{
    @Override
    public void initialize()
    {
    }

    public void start()
    {
        Chronometer chronometer = (Chronometer) findViewById(R.id.chronometer);
        chronometer.start();
    }

    @Override
    public int layout()
    {
        return R.layout.subscreen_session;
    }
}