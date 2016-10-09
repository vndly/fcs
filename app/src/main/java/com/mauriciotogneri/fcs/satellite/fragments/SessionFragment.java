package com.mauriciotogneri.fcs.satellite.fragments;

import android.os.Bundle;
import android.widget.Chronometer;
import android.widget.TextView;

import com.mauriciotogneri.fcs.R;
import com.mauriciotogneri.fcs.base.BaseFragment;
import com.mauriciotogneri.fcs.model.Session;

public class SessionFragment extends BaseFragment
{
    private static final String PARAMETER_SESSION_ID = "session.id";

    public static SessionFragment newInstance(Session session)
    {
        SessionFragment sessionFragment = new SessionFragment();

        Bundle bundle = new Bundle();
        bundle.putString(PARAMETER_SESSION_ID, session.id());
        sessionFragment.setArguments(bundle);

        return sessionFragment;
    }

    @Override
    public void initialize()
    {
        Bundle args = getArguments();
        String sessionId = args.getString(PARAMETER_SESSION_ID);

        TextView session = (TextView) findViewById(R.id.session_id);
        session.setText(sessionId);

        Chronometer chronometer = (Chronometer) findViewById(R.id.chronometer);
        chronometer.start();
    }

    @Override
    public int layout()
    {
        return R.layout.subscreen_session;
    }
}