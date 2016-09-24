package com.mauriciotogneri.flightrecorder;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.mauriciotogneri.flightrecorder.fragments.BaseFragment;

public class PagerAdapter extends FragmentStatePagerAdapter
{
    private final String[] names;
    private final BaseFragment[] fragments;

    public PagerAdapter(FragmentManager fragmentManager, String[] names, BaseFragment[] fragments)
    {
        super(fragmentManager);

        this.names = names;
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position)
    {
        return fragments[position];
    }

    @Override
    public int getCount()
    {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        return names[position];
    }
}