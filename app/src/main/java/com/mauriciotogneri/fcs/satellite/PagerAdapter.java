package com.mauriciotogneri.fcs.satellite;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.TypedValue;

import com.mauriciotogneri.fcs.satellite.fragments.BaseFragment;

public class PagerAdapter extends FragmentStatePagerAdapter
{
    private final Context context;
    private final String[] names;
    private final BaseFragment[] fragments;

    public PagerAdapter(Context context, FragmentManager fragmentManager, String[] names, BaseFragment[] fragments)
    {
        super(fragmentManager);

        this.context = context;
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
        String title = names[position];
        int fontSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, context.getResources().getDisplayMetrics());

        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(title);

        ForegroundColorSpan fcs = new ForegroundColorSpan(Color.argb(255, 100, 100, 100));
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(fontSize);
        StyleSpan bss = new StyleSpan(Typeface.BOLD);

        spannableStringBuilder.setSpan(fcs, 0, title.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        spannableStringBuilder.setSpan(bss, 0, title.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        spannableStringBuilder.setSpan(ass, 0, title.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        return spannableStringBuilder;
    }
}