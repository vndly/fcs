package com.mauriciotogneri.fcs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.mauriciotogneri.fcs.ground.GroundActivity;
import com.mauriciotogneri.fcs.satellite.SatelliteActivity;
import com.mauriciotogneri.uibinder.UiBinder;
import com.mauriciotogneri.uibinder.annotations.OnClick;

public class MainActivity extends AppCompatActivity
{
    private final UiBinder uiBinder = new UiBinder();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_main);
        uiBinder.bind(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @OnClick(R.id.type_ground)
    public void onGroundButton()
    {
        Intent intent = new Intent(this, GroundActivity.class);
        startActivity(intent);

        finish();
    }

    @OnClick(R.id.type_satellite)
    public void onSatelliteButton()
    {
        Intent intent = new Intent(this, SatelliteActivity.class);
        startActivity(intent);

        finish();
    }
}