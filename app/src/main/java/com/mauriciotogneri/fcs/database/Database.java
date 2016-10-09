package com.mauriciotogneri.fcs.database;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mauriciotogneri.fcs.sensors.AccelerometerSensor.AccelerometerListener;
import com.mauriciotogneri.fcs.sensors.LocationSensor.LocationListener;
import com.mauriciotogneri.fcs.sensors.RotationSensor.RotationListener;
import com.mauriciotogneri.fcs.util.DateUtil;

public class Database implements AccelerometerListener, RotationListener, LocationListener
{
    private DatabaseReference accelerometer;
    private DatabaseReference rotation;
    private DatabaseReference location;

    public Database()
    {
        long timestamp = System.currentTimeMillis();
        String sessionId = DateUtil.format(timestamp);

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference index = database.getReference("index");
        index.child(String.valueOf(timestamp)).setValue(sessionId);

        DatabaseReference sessions = database.getReference("sessions");
        DatabaseReference session = sessions.child(sessionId);

        accelerometer = session.child("accelerometer");
        rotation = session.child("rotation");
        location = session.child("location");
    }

    @Override
    public void onAccelerometerData(AccelerometerData data)
    {
        accelerometer.child(String.valueOf(data.timestamp())).setValue(data);
    }

    @Override
    public void onRotationData(RotationData data)
    {
        rotation.child(String.valueOf(data.timestamp())).setValue(data);
    }

    @Override
    public void onLocationData(LocationData data)
    {
        location.child(String.valueOf(data.timestamp())).setValue(data);
    }
}