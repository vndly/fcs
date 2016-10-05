package com.mauriciotogneri.flightrecorder.database;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mauriciotogneri.flightrecorder.sensors.AccelerometerSensor.AccelerometerListener;
import com.mauriciotogneri.flightrecorder.sensors.LocationSensor.LocationListener;
import com.mauriciotogneri.flightrecorder.sensors.RotationSensor.RotationListener;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class Database implements AccelerometerListener, RotationListener, LocationListener
{
    private DatabaseReference accelerometer;
    private DatabaseReference rotation;
    private DatabaseReference location;

    public Database()
    {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss");
        String sessionId = dateTimeFormatter.print(DateTime.now());

        DatabaseReference database = FirebaseDatabase.getInstance().getReference("sessions");

        DatabaseReference session = database.child(sessionId);

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