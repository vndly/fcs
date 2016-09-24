package com.mauriciotogneri.flightrecorder;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.mauriciotogneri.flightrecorder.DataService.ServiceBinder;
import com.mauriciotogneri.flightrecorder.log.FlightLog;
import com.mauriciotogneri.flightrecorder.sensors.AccelerometerSensor.AccelerometerListener;
import com.mauriciotogneri.flightrecorder.sensors.RotationSensor.RotationListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class MainActivity extends Activity implements AccelerometerListener, RotationListener
{
    private ServiceConnection serviceConnection;
    private FlightLog flightLog;

    private long initialTime;

    private final LineGraphSeries<DataPoint> seriesX = new LineGraphSeries<>();
    private final LineGraphSeries<DataPoint> seriesY = new LineGraphSeries<>();
    private final LineGraphSeries<DataPoint> seriesZ = new LineGraphSeries<>();

    private TextView lastValueX;
    private TextView lastValueY;
    private TextView lastValueZ;

    private static final int MAX_DATA_LENGTH = 50;

    private static final float AXIS_X_RESOLUTION = 2000;
    private static final float AXIS_Y_RESOLUTION = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        try
        {
            SimpleDateFormat sourceDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.getDefault());
            File folder = new File(Environment.getExternalStorageDirectory() + "/flightrecorder", sourceDateFormat.format(System.currentTimeMillis()));
            folder.mkdirs();

            flightLog = new FlightLog(folder);
        }
        catch (IOException e)
        {
            // TODO
            e.printStackTrace();
        }

        lastValueX = (TextView) findViewById(R.id.last_value_x);
        lastValueY = (TextView) findViewById(R.id.last_value_y);
        lastValueZ = (TextView) findViewById(R.id.last_value_z);

        configureGraph(R.id.graph_x, seriesX, Color.RED);
        configureGraph(R.id.graph_y, seriesY, Color.GREEN);
        configureGraph(R.id.graph_z, seriesZ, Color.BLUE);

        serviceConnection = new ServiceConnection()
        {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service)
            {
                onConnected(service);
            }

            @Override
            public void onServiceDisconnected(ComponentName name)
            {
                onDisconnected();
            }
        };

        Intent intent = new Intent(this, DataService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private void onConnected(IBinder service)
    {
        initialTime = System.currentTimeMillis();

        ServiceBinder binder = (ServiceBinder) service;
        DataService dataService = binder.getService();
        dataService.startRecording(5, this, 5, this);

        Chronometer chronometer = (Chronometer) findViewById(R.id.chronometer);
        chronometer.start();
    }

    private void onDisconnected()
    {
        finish();
    }

    private void configureGraph(int id, LineGraphSeries<DataPoint> series, int color)
    {
        GraphView graph = (GraphView) findViewById(id);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(-AXIS_Y_RESOLUTION);
        graph.getViewport().setMaxY(AXIS_Y_RESOLUTION);

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(-AXIS_X_RESOLUTION);
        graph.getViewport().setMaxX(AXIS_X_RESOLUTION);

        graph.addSeries(series);

        series.setColor(color);
        series.setThickness(4);
    }

    @Override
    public void onAccelerometerData(long timestamp, float x, float y, float z)
    {
        long time = timestamp - initialTime;

        seriesX.appendData(new DataPoint(time, x), true, MAX_DATA_LENGTH);
        seriesY.appendData(new DataPoint(time, y), true, MAX_DATA_LENGTH);
        seriesZ.appendData(new DataPoint(time, z), true, MAX_DATA_LENGTH);

        lastValueX.setText(String.valueOf(x));
        lastValueY.setText(String.valueOf(y));
        lastValueZ.setText(String.valueOf(y));

        flightLog.onAccelerometerData(timestamp, x, y, z);
    }

    @Override
    public void onRotationData(long timestamp, float x, float y, float z)
    {
        flightLog.onRotationData(timestamp, x, y, z);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        unbindService(serviceConnection);
    }
}