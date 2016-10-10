package com.mauriciotogneri.fcs.satellite.fragments;

import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.widget.Chronometer;
import android.widget.TextView;

import com.mauriciotogneri.fcs.R;
import com.mauriciotogneri.fcs.base.BaseFragment;
import com.mauriciotogneri.fcs.util.DateUtil;

public class SessionFragment extends BaseFragment
{
    private static final String PARAMETER_SESSION_ID = "session.id";

    public static SessionFragment newInstance(String sessionId)
    {
        SessionFragment sessionFragment = new SessionFragment();

        Bundle bundle = new Bundle();
        bundle.putString(PARAMETER_SESSION_ID, sessionId);
        sessionFragment.setArguments(bundle);

        return sessionFragment;
    }

    @Override
    public void initialize()
    {
        Bundle args = getArguments();
        String sessionId = args.getString(PARAMETER_SESSION_ID);

        if (!TextUtils.isEmpty(sessionId))
        {
            start(sessionId);
        }
    }

    public void start(String sessionId)
    {
        TextView session = (TextView) findViewById(R.id.session_id);
        session.setText(sessionId);

        long base = System.currentTimeMillis() - DateUtil.parse(sessionId);

        Chronometer chronometer = (Chronometer) findViewById(R.id.chronometer);
        chronometer.setBase(SystemClock.elapsedRealtime() - base);
        chronometer.start();
    }

    @Override
    public int layout()
    {
        return R.layout.subscreen_session;
    }
}