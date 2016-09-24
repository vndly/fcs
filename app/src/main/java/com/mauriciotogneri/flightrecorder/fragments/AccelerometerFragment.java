package com.mauriciotogneri.flightrecorder.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.mauriciotogneri.flightrecorder.R;
import com.mauriciotogneri.flightrecorder.sensors.AccelerometerSensor.AccelerometerListener;

public class AccelerometerFragment extends Fragment implements AccelerometerListener
{
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
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.screen_accelerometer, container, false);
    }

    @Override
    public final void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        initialTime = System.currentTimeMillis();

        lastValueX = (TextView) getView().findViewById(R.id.last_accelerometer_x);
        lastValueY = (TextView) getView().findViewById(R.id.last_accelerometer_y);
        lastValueZ = (TextView) getView().findViewById(R.id.last_accelerometer_z);

        configureGraph(R.id.graph_accelerometer_x, seriesX, Color.RED);
        configureGraph(R.id.graph_accelerometer_y, seriesY, Color.GREEN);
        configureGraph(R.id.graph_accelerometer_z, seriesZ, Color.BLUE);
    }

    private void configureGraph(int id, LineGraphSeries<DataPoint> series, int color)
    {
        GraphView graph = (GraphView) getView().findViewById(id);
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
    }
}