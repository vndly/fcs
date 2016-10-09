package com.mauriciotogneri.fcs.satellite.fragments;

import android.graphics.Color;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.mauriciotogneri.fcs.R;
import com.mauriciotogneri.fcs.base.BaseFragment;
import com.mauriciotogneri.fcs.model.AccelerometerData;
import com.mauriciotogneri.fcs.satellite.sensors.AccelerometerSensor.AccelerometerListener;

import static android.R.attr.x;
import static android.R.attr.y;

public class AccelerometerFragment extends BaseFragment implements AccelerometerListener
{
    private long initialTime;

    private final LineGraphSeries<DataPoint> seriesX = new LineGraphSeries<>();
    private final LineGraphSeries<DataPoint> seriesY = new LineGraphSeries<>();
    private final LineGraphSeries<DataPoint> seriesZ = new LineGraphSeries<>();

    private TextView lastValueX;
    private TextView lastValueY;
    private TextView lastValueZ;

    private static final int MAX_DATA_LENGTH = 200;
    private static final int AXIS_X_RESOLUTION = 1000;
    private static final int AXIS_Y_RESOLUTION = 5;

    @Override
    public void initialize()
    {
        initialTime = System.currentTimeMillis();

        lastValueX = (TextView) findViewById(R.id.last_accelerometer_x);
        lastValueY = (TextView) findViewById(R.id.last_accelerometer_y);
        lastValueZ = (TextView) findViewById(R.id.last_accelerometer_z);

        configureGraph(R.id.graph_accelerometer_x, seriesX, Color.RED);
        configureGraph(R.id.graph_accelerometer_y, seriesY, Color.argb(255, 30, 190, 50));
        configureGraph(R.id.graph_accelerometer_z, seriesZ, Color.BLUE);
    }

    @Override
    public int layout()
    {
        return R.layout.subscreen_accelerometer;
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

        graph.getGridLabelRenderer().setGridColor(Color.argb(255, 200, 200, 200));

        graph.addSeries(series);

        series.setColor(color);
        series.setThickness(4);
    }

    @Override
    public void onAccelerometerData(AccelerometerData data)
    {
        long time = data.timestamp() - initialTime;

        seriesX.appendData(new DataPoint(time, data.x()), true, MAX_DATA_LENGTH);
        seriesY.appendData(new DataPoint(time, data.y()), true, MAX_DATA_LENGTH);
        seriesZ.appendData(new DataPoint(time, data.z()), true, MAX_DATA_LENGTH);

        lastValueX.setText(String.valueOf(x));
        lastValueY.setText(String.valueOf(y));
        lastValueZ.setText(String.valueOf(y));
    }
}