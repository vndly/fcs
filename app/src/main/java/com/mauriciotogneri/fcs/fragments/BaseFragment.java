package com.mauriciotogneri.fcs.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment
{
    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(layout(), container, false);
    }

    @Override
    public final void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        initialize();
    }

    @SuppressWarnings("ConstantConditions")
    protected View findViewById(int id)
    {
        return getView().findViewById(id);
    }

    public abstract void initialize();

    public abstract int layout();
}